package com.africaapps.league.service.feed;

import java.util.Date;
import java.util.List;

import org.datacontract.schemas._2004._07.livemediastructs.ActorStruct;
import org.datacontract.schemas._2004._07.livemediastructs.EventMatchFilActionStruct;
import org.datacontract.schemas._2004._07.livemediastructs.MatchFilActionStruct;
import org.datacontract.schemas._2004._07.livemediastructs.SDTS;
import org.datacontract.schemas._2004._07.livemediastructs.SS;
import org.datacontract.schemas._2004._07.livemediastructs.TeamMatchFilActionStruct;
import org.datacontract.schemas._2004._07.livemediastructs.TeamStruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.game.PlayingWeek;
import com.africaapps.league.model.game.Pool;
import com.africaapps.league.model.league.BlockType;
import com.africaapps.league.model.league.League;
import com.africaapps.league.model.league.LeagueSeason;
import com.africaapps.league.model.league.LeagueType;
import com.africaapps.league.model.league.Match;
import com.africaapps.league.model.league.MatchProcessingStatus;
import com.africaapps.league.model.league.Player;
import com.africaapps.league.model.league.PlayerMatch;
import com.africaapps.league.model.league.PlayerMatchStatistic;
import com.africaapps.league.model.league.Position;
import com.africaapps.league.model.league.PositionType;
import com.africaapps.league.model.league.Statistic;
import com.africaapps.league.model.league.Team;
import com.africaapps.league.service.cache.CacheService;
import com.africaapps.league.service.game.player.UserPlayerService;
import com.africaapps.league.service.game.playingweek.PlayingWeekService;
import com.africaapps.league.service.league.LeagueService;
import com.africaapps.league.service.match.MatchService;
import com.africaapps.league.service.player.PlayerService;
import com.africaapps.league.service.pool.PoolService;
import com.africaapps.league.service.team.TeamService;
import com.africaapps.league.service.transaction.ReadTransaction;
import com.africaapps.league.service.transaction.WriteTransaction;
import com.africaapps.league.service.webservice.WebServiceClient;
import com.africaapps.league.util.WebServiceXmlUtil;

@Service("FeedService")
public class FeedServiceImpl implements FeedService {

	private static final Integer SDTS_MATCH_ID = 1;
	private static final Integer GOAL_AGAINST_ID = -1;
	private static final Integer MATCH_WON_ID = -2;
	private static final Integer MATCH_DRAW_ID = -3;
	private static final Integer MATCH_LOST_ID = -4;

	@Autowired
	private LeagueService leagueService;
	@Autowired
	private PlayingWeekService playingWeekService;
	@Autowired
	private MatchService matchService;
	@Autowired
	private TeamService teamService;
	@Autowired
	private PlayerService playerService;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private PoolService poolService;
	@Autowired
	private UserPlayerService userPlayerService;

	private static Logger logger = LoggerFactory.getLogger(FeedServiceImpl.class);

	@Override
	public void processFeed(League league, String wsdlUrl, String username, String password, MatchFilter matchFilter)
			throws LeagueException {
		cacheService.clear();
		cacheService.loadStatistics(league.getLeagueType());

		LeagueSeason leagueSeason = getLeagueSeason(league);
		Pool pool = getPool(leagueSeason);
		WebServiceClient webServiceClient = setupWebServiceClient(wsdlUrl, username, password);

		List<Integer> processedMatchIds = webServiceClient.processMatches(league, leagueSeason, pool, this, matchFilter);
		logger.info("Processed " + processedMatchIds.size() + " matches");
		
		leagueService.setLastFeedRun(league, new Date());
		logger.info("Updated last feed run");
	}

	protected WebServiceClient setupWebServiceClient(String wsdlUrl, String username, String password) throws LeagueException {
		WebServiceClient client = new WebServiceClient(wsdlUrl, username, password);
		if (!client.isServiceReady()) {
			throw new LeagueException("Web service is not ready!");
		} else {
			return client;
		}
	}

	protected LeagueSeason getLeagueSeason(League league) throws LeagueException {
		LeagueSeason season = leagueService.getCurrentSeason(league);
		if (season == null) {
			throw new LeagueException("Unable to get current season for League: " + league);
		} else {
			logger.info("Current season: " + season);
			return season;
		}
	}

	protected Pool getPool(LeagueSeason leagueSeason) throws LeagueException {
		Pool pool = poolService.getPool(leagueSeason);
		if (pool == null) {
			throw new LeagueException("No Pool specified for LeagueSeason: " + leagueSeason);
		} else {
			return pool;
		}
	}

	public boolean isProcessedMatch(long leagueSeasonId, int matchId) throws LeagueException {
		return matchService.isProcessedMatch(leagueSeasonId, matchId);
	}

	@WriteTransaction
	public void saveTeamAndPlayers(League league, LeagueSeason leagueSeason, Pool pool, TeamStruct teamStruct)
			throws LeagueException {
		logger.info("Saving team and players...");
		Team team = saveTeam(league, leagueSeason, teamStruct);
		savePlayers(league, pool, teamStruct, team);
	}

	protected Team saveTeam(League league, LeagueSeason leagueSeason, TeamStruct teamStruct) throws LeagueException {
		Team team = new Team();
		team.setTeamId(teamStruct.getIdTeam());
		team.setCity(teamStruct.getCity().getValue());
		team.setClubName(teamStruct.getClubName().getValue());
		team.setTeamName(teamStruct.getTeamName().getValue());
		team.setLeagueSeason(leagueSeason);
		teamService.saveTeam(team);
		logger.info("Saved team: " + team);
		return team;
	}

	protected void savePlayers(League league, Pool pool, TeamStruct teamStruct, Team team) throws LeagueException {
		if (teamStruct.getLstActorStruct() != null && teamStruct.getLstActorStruct().getValue() != null
				&& teamStruct.getLstActorStruct().getValue().getActorStruct() != null) {
			if (teamStruct.getLstActorStruct().getValue().getActorStruct().size() == 0) {
				throw new LeagueException("No players found in TeamStruct: " + teamStruct.getIdTeam() + " "
						+ teamStruct.getClubName().getValue());
			}
			for (ActorStruct actorStruct : teamStruct.getLstActorStruct().getValue().getActorStruct()) {
				logger.debug("Processing player: " + actorStruct.getIdActor());
				Player player = new Player();
				player.setPlayerId(actorStruct.getIdActor());
				player.setFirstName(actorStruct.getFirstName() != null ? actorStruct.getFirstName().getValue() : "");
				player.setLastName(actorStruct.getSecondName() != null ? actorStruct.getSecondName().getValue() : "");
				player.setPosition(getPosition(league, actorStruct.getIdPosition().getValue()));
				BlockType block = getBlockType(actorStruct);
				player.setBlock(block);
				player.setShirtNumber(actorStruct.getShirtNumber() != null ? actorStruct.getShirtNumber().getValue() : 0);
				player.setTeam(team);
				playerService.savePlayer(player);
				logger.debug("Saved player: " + player);
				poolService.savePlayer(pool, player);
				logger.debug("Saved poolPlayer: " + pool + " " + player);
			}
		} else {
			logger.error("No players to save");
		}
	}

	private BlockType getBlockType(ActorStruct actorStruct) {
		if (actorStruct.getIdBlock().getValue() != null) {
			if (9 == actorStruct.getIdBlock().getValue().intValue()) {
				return BlockType.GOALKEEPER;
			} else if (10 == actorStruct.getIdBlock().getValue().intValue()) {
				return BlockType.DEFENDER;
			} else if (11 == actorStruct.getIdBlock().getValue().intValue()) {
				return BlockType.MIDFIELDER;
			} else if (12 == actorStruct.getIdBlock().getValue().intValue()) {
				return BlockType.STRIKER;
			} else {
				return BlockType.SUBSTITUTE;
			}
		} else {
			String firstName = "";
			String lastName = "";
			if (actorStruct.getFirstName() != null) {
				firstName = actorStruct.getFirstName().getValue();
			}
			if (actorStruct.getSecondName() != null) {
				lastName = actorStruct.getSecondName().getValue();
			}
			logger.error("No block set for player: " + actorStruct.getIdActor() + " , " + firstName + " " + lastName);
			return null;
		}
	}

	private Position getPosition(League league, Integer positionNumber) throws LeagueException {
		if (positionNumber == null) {
			logger.debug("No positionNumber specified");
			return null;
		}
		Position position = cacheService.getPosition(league.getLeagueType().getId(), positionNumber);
		if (position != null) {
			return position;
		} else {
			logger.debug("Getting position for league type: " + league.getLeagueType().getId() + " positionNumber:"
					+ positionNumber);
			position = playerService.getPosition(league.getLeagueType().getId(), positionNumber);
			if (position == null) {
				logger.debug("Unknown position for league: " + league + " and positionNumber: " + positionNumber);
				position = new Position();
				position.setLeagueType(league.getLeagueType());
				position.setPositionNumber(positionNumber);
				position.setPositionType(PositionType.UNKNOWN);
				playerService.savePosition(position);
				logger.debug("Saved position: " + position);
				cacheService.setPosition(league.getLeagueType().getId(), position);
			} else {
				cacheService.setPosition(league.getLeagueType().getId(), position);
			}
		}
		return position;
	}

	@WriteTransaction
	public void processMatch(League league, LeagueSeason leagueSeason, MatchFilActionStruct matchStruct, List<TeamStruct> teams)
			throws LeagueException {
		logger.info("Starting match processing: " + matchStruct.getIdMatch() + "...");
		checkTeams(matchStruct);
		Team team1 = getTeam(leagueSeason.getId(), matchStruct, true);
		Team team2 = getTeam(leagueSeason.getId(), matchStruct, false);
		Match match = saveMatch(leagueSeason, matchStruct, team1, team2);
		savePlayerStatistics(league.getLeagueType(), matchStruct, match, teams);
		calculatePlayersScores(match);
		assignUserPlayerPoints(leagueSeason, match);
		updateMatchStatus(match);
		logger.info("Completed processing match: " + matchStruct.getIdMatch());
	}

	private void checkTeams(MatchFilActionStruct matchStruct) throws LeagueException {
		if (matchStruct.getLstTeamMatchFilActionStruct() != null
				&& matchStruct.getLstTeamMatchFilActionStruct().getValue() != null
				&& matchStruct.getLstTeamMatchFilActionStruct().getValue().getTeamMatchFilActionStruct() != null) {
			List<TeamMatchFilActionStruct> teamStructs = matchStruct.getLstTeamMatchFilActionStruct().getValue()
					.getTeamMatchFilActionStruct();
			if (teamStructs.size() != 2) {
				throw new LeagueException("Expected two teams, found: " + teamStructs.size() + " for match: "
						+ matchStruct.getIdMatch());
			}
		} else {
			throw new LeagueException("No teams found for match: " + matchStruct.getIdMatch());
		}
	}

	private Team getTeam(long leagueSeasonId, MatchFilActionStruct matchStruct, boolean firstTeam) throws LeagueException {
		List<TeamMatchFilActionStruct> teamStructs = matchStruct.getLstTeamMatchFilActionStruct().getValue()
				.getTeamMatchFilActionStruct();
		TeamMatchFilActionStruct teamStruct = null;
		if (firstTeam) {
			teamStruct = teamStructs.get(0);
		} else {
			teamStruct = teamStructs.get(1);
		}
		if (teamStruct != null) {
			Team team = teamService.getTeam(leagueSeasonId, teamStruct.getIdTeam());
			if (team == null) {
				throw new LeagueException("Unknown team for id: " + teamStruct.getIdTeam());
			}
			logger.debug("Found team: " + team);
			return team;
		} else {
			throw new LeagueException("Invalid null team firstTeam: " + firstTeam);
		}
	}

	private Match saveMatch(LeagueSeason leagueSeason, MatchFilActionStruct matchStruct, Team team1, Team team2)
			throws LeagueException {
		Match match = new Match();
		match.setMatchId(matchStruct.getIdMatch());
		match.setStartDateTime(WebServiceXmlUtil.getDate(matchStruct.getDateAndTime()));
		match.setTeam1(team1);
		match.setTeam2(team2);
		match.setStatus(MatchProcessingStatus.INPROGRESS);
		match.setLeagueSeason(leagueSeason);
		Date matchDate = WebServiceXmlUtil.getDate(matchStruct.getDateAndTime());
		PlayingWeek pw = playingWeekService.getPlayingWeek(leagueSeason, matchDate);
		if (pw != null) {
			match.setPlayingWeek(pw);
		} else {
			throw new LeagueException("No playing week found for seasonId:" + leagueSeason.getId() + " matchDate:" + matchDate);
		}
		logger.debug("Saving match: " + match);
		matchService.saveMatch(match);
		logger.debug("Saved match: " + match);
		return match;
	}

	public void updateMatchStatus(Match match) throws LeagueException {
		match.setStatus(MatchProcessingStatus.COMPLETE);
		matchService.saveMatch(match);
		logger.info("Updated match status for id:" + match.getId() + " matchId:" + match.getMatchId());
	}

	@WriteTransaction
	protected PlayerMatch getPlayerMatch(Match match, Integer playerId) throws LeagueException {
		PlayerMatch playerMatch = cacheService.getPlayerMatch(match.getId(), playerId.longValue());
		if (playerMatch != null) {
			logger.debug("Got PlayerMatch from cache: " + playerMatch);
			return playerMatch;
		} else {
			logger.debug("Saving new PlayerMatch:"+match+" "+playerId);
			playerMatch = new PlayerMatch();
			playerMatch.setMatch(match);
			playerMatch.setPlayer(getPlayer(playerId));
			playerMatch.setPlayerScore(Double.valueOf(0));
			matchService.savePlayerMatch(playerMatch);
			cacheService.setPlayerMatch(match.getId(), playerId.longValue(), playerMatch);
			logger.debug("Saved: " + playerMatch);
			return playerMatch;
		}
	}

	@ReadTransaction
	protected Player getPlayer(Integer playerId) throws LeagueException {
		// Integer playerId = playerStruct.getIdActor();
		Player player = cacheService.getPlayer(playerId);
		if (player != null) {
			return player;
		} else {
			player = playerService.getPlayer(playerId);
			if (player == null) {
				throw new LeagueException("Unknown player for id: " + playerId);
			} else {
				cacheService.setPlayer(player);
			}
		}
		return player;
	}

	private void calculatePlayersScores(Match match) throws LeagueException {
		matchService.calculatePlayerScores(match);
	}

	private void assignUserPlayerPoints(LeagueSeason leagueSeason, Match match) throws LeagueException {
		userPlayerService.assignUserPlayerPoints(leagueSeason, match);
	}

	private void savePlayerStatistics(LeagueType leagueType, MatchFilActionStruct matchStruct, Match match,
			List<TeamStruct> teams) throws LeagueException {
		if (teams.size() != 2) {
			logger.error("Only have " + teams.size() + " teams to save statistics for match");
		}

		Integer scoreTeamA = getFinalScore(matchStruct, true);
		Integer scoreTeamB = getFinalScore(matchStruct, false);
		//Save the match's final score
		MatchResult matchResult = saveMatchResult(scoreTeamA, scoreTeamB, match);

		// Per team, per actor - only process match level statistics
		for (TeamStruct team : teams) {
			boolean isTeamA = team.getIdTeam().equals(match.getTeam1().getTeamId());

			if (team.getLstActorStruct() != null && team.getLstActorStruct().getValue() != null
					&& team.getLstActorStruct().getValue().getActorStruct() != null) {
				for (ActorStruct actor : team.getLstActorStruct().getValue().getActorStruct()) {
					BlockType block = getBlockType(actor);
					PlayerMatch playerMatch = getPlayerMatch(match, actor.getIdActor());

					//Goalkeeper's goal against
					if (BlockType.GOALKEEPER.equals(block)) {
						saveGoalsAgainst(scoreTeamA, scoreTeamB, matchResult, isTeamA, actor, playerMatch, leagueType.getId());
					}

					//Player's stats
					if (actor.getLstSDTS() != null && actor.getLstSDTS().getValue() != null
							&& actor.getLstSDTS().getValue().getSDTS() != null) {
						for (SDTS sdts : actor.getLstSDTS().getValue().getSDTS()) {
							if (sdts.getIdDT().equals(SDTS_MATCH_ID)) {
								logger.info("Processing actor's match statistics: " + actor.getIdActor());
								if (sdts.getLstSS() != null && sdts.getLstSS().getValue() != null
										&& sdts.getLstSS().getValue().getSS() != null) {
									for (SS ss : sdts.getLstSS().getValue().getSS()) {
										if (ss.getV() != null && ss.getV().getValue() != null && ss.getV().getValue() > 0) {
											logger.info("Processing actor's stat - id:" + ss.getId() + " value:" + ss.getV().getValue());
											Statistic statistic = cacheService.getStatistic(ss.getId(), block);
											if (statistic != null) {
												logger.info("Got actor's statistic:" + statistic);
												Double points = ss.getV().getValue() * statistic.getPoints();
												PlayerMatchStatistic matchStat = new PlayerMatchStatistic();
												matchStat.setValue(ss.getV().getValue());
												matchStat.setPoints(points);
												matchStat.setPlayerMatch(playerMatch);
												matchStat.setStatistic(statistic);
												playerService.savePlayerMatchStatistic(leagueType.getId(), matchStat);
												logger.info("Saved actor's statistic: " + matchStat);
											} else {
												logger.error("No statistic for: " + ss.getId() + " block:" + block);
											}
										} else {
											Double value = (ss.getV() != null && ss.getV().getValue() != null ? ss.getV().getValue() : 0);
											logger.warn("Skipping actor's stats: " +ss.getId()+" value: "+value);
										}
									}
								} else {
									logger.error("Actor has no match level statistics to process");
								}
								saveMatchResultStatistic(actor, block, matchResult, isTeamA, playerMatch, leagueType.getId());
							}
						}
					} else {
						logger.error("Actor has not SDTS to save statisctics for match");
					}
				}
			} else {
				logger.error("Invalid team actors to save statistics for match");
			}
		}
	}

	private MatchResult saveMatchResult(Integer scoreTeamA, Integer scoreTeamB, Match match) throws LeagueException {
		match.setFinalScore(scoreTeamA + "-" + scoreTeamB);
		matchService.saveMatch(match);
		logger.info("Saved match's score: "+match.getFinalScore());
		if (scoreTeamA == scoreTeamB) {
			return MatchResult.DRAW;
		} else if (scoreTeamA > scoreTeamB) {
			return MatchResult.TEAMA_WON;
		} else {
			return MatchResult.TEAMA_LOST;
		}
	}

	private Integer getFinalScore(MatchFilActionStruct matchStruct, boolean teamA) {
		if (matchStruct.getLstEventMatchFilActionStruct() != null
				&& matchStruct.getLstEventMatchFilActionStruct().getValue() != null
				&& matchStruct.getLstEventMatchFilActionStruct().getValue().getEventMatchFilActionStruct() != null) {
			int last = matchStruct.getLstEventMatchFilActionStruct().getValue().getEventMatchFilActionStruct().size();
			EventMatchFilActionStruct eventStruct = matchStruct.getLstEventMatchFilActionStruct().getValue()
					.getEventMatchFilActionStruct().get(last-1);
			if (eventStruct != null) {
				if (teamA) {
					if (eventStruct.getScoreA() != null) {
						return eventStruct.getScoreA().getValue();
					} else {
						logger.error("No teamA last score");
						return 0;
					}
				} else {
					if (eventStruct.getScoreB() != null) {
						return eventStruct.getScoreB().getValue();
					} else {
						logger.error("No teamB last score");
						return 0;
					}
				}
			}
		}
		logger.error("No last event with final score");
		return 0;
	}

	private void saveGoalsAgainst(int scoreTeamA, int scoreTeamB, MatchResult matchResult, boolean isTeamA, ActorStruct actor, PlayerMatch playerMatch, long leagueTypeId)
			throws LeagueException {
		Statistic stat = cacheService.getStatistic(GOAL_AGAINST_ID, BlockType.GOALKEEPER);
		if (stat != null) {
			if (isTeamA && scoreTeamB > 0) {
				PlayerMatchStatistic matchStat = new PlayerMatchStatistic();
				matchStat.setValue(stat.getPoints());
				matchStat.setPoints(stat.getPoints() * scoreTeamB);
				matchStat.setPlayerMatch(playerMatch);
				matchStat.setStatistic(stat);
				playerService.savePlayerMatchStatistic(leagueTypeId, matchStat);
				logger.info("Saved goalkeeper A's goals against statistic: " + matchStat);
			} else if (!isTeamA && scoreTeamA > 0) {
				PlayerMatchStatistic matchStat = new PlayerMatchStatistic();
				matchStat.setValue(stat.getPoints());
				matchStat.setPoints(stat.getPoints() * scoreTeamA);
				matchStat.setPlayerMatch(playerMatch);
				matchStat.setStatistic(stat);
				playerService.savePlayerMatchStatistic(leagueTypeId, matchStat);
				logger.info("Saved goalkeeper B's goals against statistic: " + matchStat);
			}
		}
	}

	private void saveMatchResultStatistic(ActorStruct actor, BlockType block, MatchResult matchResult, boolean isTeamA,
			PlayerMatch playerMatch, long leagueTypeId) throws LeagueException {
		Statistic stat = null;
		if (MatchResult.DRAW.equals(matchResult)) {
			stat = cacheService.getStatistic(MATCH_DRAW_ID, block);
		} else if (MatchResult.TEAMA_WON.equals(matchResult)) {
			if (isTeamA) {
				stat = cacheService.getStatistic(MATCH_WON_ID, block);
			} else {
				stat = cacheService.getStatistic(MATCH_LOST_ID, block);
			}
		} else if (MatchResult.TEAMA_LOST.equals(matchResult)) {
			if (isTeamA) {
				stat = cacheService.getStatistic(MATCH_LOST_ID, block);
			} else {
				stat = cacheService.getStatistic(MATCH_WON_ID, block);
			}
		}
		if (stat != null) {
			PlayerMatchStatistic matchStat = new PlayerMatchStatistic();
			matchStat.setValue(stat.getPoints());
			matchStat.setPoints(stat.getPoints());
			matchStat.setPlayerMatch(playerMatch);
			matchStat.setStatistic(stat);
			playerService.savePlayerMatchStatistic(leagueTypeId, matchStat);
			logger.debug("Saved actor's matchResult statistic: " + matchStat);
		}
	}
}