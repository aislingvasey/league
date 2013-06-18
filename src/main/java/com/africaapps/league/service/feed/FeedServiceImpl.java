package com.africaapps.league.service.feed;

import java.util.List;

import org.datacontract.schemas._2004._07.livemediastructs.ActorMatchFilActionStruct;
import org.datacontract.schemas._2004._07.livemediastructs.ActorStruct;
import org.datacontract.schemas._2004._07.livemediastructs.EventMatchFilActionStruct;
import org.datacontract.schemas._2004._07.livemediastructs.MatchFilActionStruct;
import org.datacontract.schemas._2004._07.livemediastructs.TeamMatchFilActionStruct;
import org.datacontract.schemas._2004._07.livemediastructs.TeamStruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.game.Pool;
import com.africaapps.league.model.league.BlockType;
import com.africaapps.league.model.league.Event;
import com.africaapps.league.model.league.League;
import com.africaapps.league.model.league.LeagueSeason;
import com.africaapps.league.model.league.LeagueType;
import com.africaapps.league.model.league.Match;
import com.africaapps.league.model.league.MatchProcessingStatus;
import com.africaapps.league.model.league.Player;
import com.africaapps.league.model.league.PlayerMatch;
import com.africaapps.league.model.league.PlayerMatchEvent;
import com.africaapps.league.model.league.Position;
import com.africaapps.league.model.league.PositionType;
import com.africaapps.league.model.league.Team;
import com.africaapps.league.service.cache.CacheService;
import com.africaapps.league.service.league.LeagueService;
import com.africaapps.league.service.match.MatchService;
import com.africaapps.league.service.player.PlayerService;
import com.africaapps.league.service.pool.PoolService;
import com.africaapps.league.service.team.TeamService;
import com.africaapps.league.service.transaction.ReadTransaction;
import com.africaapps.league.service.transaction.WriteTransaction;
import com.africaapps.league.service.webservice.WebServiceClient;
import com.africaapps.league.util.WebServiceXmlUtil;

@Service
public class FeedServiceImpl implements FeedService {

	@Autowired
	private LeagueService leagueService;
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

	private static Logger logger = LoggerFactory.getLogger(FeedServiceImpl.class);

	// TODO schedule feed processing - with retrys for failures, save status, datetime, etc

	@Override
	public void processFeed(String leagueName, String wsdlUrl, String username, String password) throws LeagueException {
		cacheService.clear();
		
		League league = getLeague(leagueName);
		LeagueSeason leagueSeason = getLeagueSeason(league);
		Pool pool = getPool(leagueSeason);
		WebServiceClient webServiceClient = setupWebServiceClient(wsdlUrl, username, password);
		
		List<Integer> processedMatchIds = webServiceClient.processMatches(league, leagueSeason, pool, this);
		logger.info("Processed "+processedMatchIds.size() +" matches");
		//TODO Calculate each user's team score for the processed matches		
	}
	
	@WriteTransaction
	public void saveTeamAndPlayers(League league, LeagueSeason leagueSeason, Pool pool, TeamStruct teamStruct) throws LeagueException {
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
		logger.info("Saved team: "+team);
		return team;
	}
	
	protected void savePlayers(League league, Pool pool, TeamStruct teamStruct, Team team) throws LeagueException {
		if (teamStruct.getLstActorStruct() != null 
				&& teamStruct.getLstActorStruct().getValue() != null
				&& teamStruct.getLstActorStruct().getValue().getActorStruct() != null) {
			for(ActorStruct actorStruct : teamStruct.getLstActorStruct().getValue().getActorStruct()) {
				logger.debug("Processing player: "+actorStruct.getIdActor());
				Player player = new Player();
				player.setPlayerId(actorStruct.getIdActor());
				player.setFirstName(actorStruct.getFirstName() != null ? actorStruct.getFirstName().getValue() : "");
				player.setLastName(actorStruct.getSecondName() != null ? actorStruct.getSecondName().getValue() : "");
				player.setNickName(actorStruct.getNickName() != null ? actorStruct.getNickName().getValue() : "");
				player.setPosition(getPosition(league, actorStruct.getIdPosition().getValue()));
				player.setBlock(getBlockType(actorStruct));
				player.setShirtNumber(actorStruct.getShirtNumber() != null ? actorStruct.getShirtNumber().getValue() : 0);
				player.setTeam(team);
				playerService.savePlayer(player);
				logger.debug("Saved player: "+player);
				poolService.savePlayer(pool, player);
				logger.debug("Saved poolPlayer: "+pool+" "+player);
			}
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
		}
		return null;
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
			logger.debug("Getting position for league type: "+league.getLeagueType().getId()+" positionNumber:"+positionNumber);
			position = playerService.getPosition(league.getLeagueType().getId(), positionNumber);
			if (position == null) {
				logger.debug("Unknown position for league: "+league+" and positionNumber: " + positionNumber);
				position = new Position();
				position.setLeagueType(league.getLeagueType());
				position.setPositionNumber(positionNumber);
				position.setPositionType(PositionType.UNKNOWN);
				playerService.savePosition(position);
				logger.debug("Saved position: "+position);
				cacheService.setPosition(league.getLeagueType().getId(), position);				
			} else {
				cacheService.setPosition(league.getLeagueType().getId(), position);
			}
		}
		return position;
	}

	protected WebServiceClient setupWebServiceClient(String wsdlUrl, String username, String password)
			throws LeagueException {
		WebServiceClient client = new WebServiceClient(wsdlUrl, username, password);
		if (!client.isServiceReady()) {
			throw new LeagueException("Web service is not ready!");
		} else {
			return client;
		}
	}

	protected League getLeague(String leagueName) throws LeagueException {
		League league = leagueService.getLeague(leagueName);
		if (league == null) {
			throw new LeagueException("Unknown league for name: " + leagueName);
		} else {
			logger.info("Starting feed for league: " + league.getName());
			return league;
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
			throw new LeagueException("No Pool specified for LeagueSeason: "+leagueSeason);
		} else {
			return pool;
		}
	}

	public boolean isProcessedMatch(long leagueSeasonId, int matchId) throws LeagueException {
		return matchService.isProcessedMatch(leagueSeasonId, matchId);
	}

	@WriteTransaction
	public void processMatch(League league, LeagueSeason leagueSeason, MatchFilActionStruct matchStruct)
			throws LeagueException {
		logger.info("Starting match processing: "+matchStruct.getIdMatch()+"...");
		checkTeams(matchStruct);
		Team team1 = getTeam(leagueSeason.getId(), matchStruct, true);
		Team team2 = getTeam(leagueSeason.getId(), matchStruct, false);
		Match match = saveMatch(leagueSeason, matchStruct, team1, team2);
		savePlayerEvents(league.getLeagueType(), matchStruct, match);
		matchService.calculatePlayerScores(match);
		updateMatchStatus(match);
	}

	private void checkTeams(MatchFilActionStruct matchStruct) throws LeagueException {
		if (matchStruct.getLstTeamMatchFilActionStruct() != null
				&& matchStruct.getLstTeamMatchFilActionStruct().getValue() != null
				&& matchStruct.getLstTeamMatchFilActionStruct().getValue().getTeamMatchFilActionStruct() != null) {
			List<TeamMatchFilActionStruct> teamStructs = matchStruct.getLstTeamMatchFilActionStruct().getValue().getTeamMatchFilActionStruct();
			if (teamStructs.size() != 2) {
				throw new LeagueException("Expected two teams, found: " + teamStructs.size() + " for match: "
						+ matchStruct.getIdMatch());
			}
		} else {
			throw new LeagueException("No teams found for match: " + matchStruct.getIdMatch());
		}
	}

	private Team getTeam(long leagueSeasonId, MatchFilActionStruct matchStruct, boolean firstTeam) throws LeagueException {
		List<TeamMatchFilActionStruct> teamStructs = matchStruct.getLstTeamMatchFilActionStruct().getValue().getTeamMatchFilActionStruct();
		TeamMatchFilActionStruct teamStruct = null;
		if (firstTeam) {
			teamStruct = teamStructs.get(0);
		} else {
			teamStruct = teamStructs.get(1);
		}
		if (teamStruct != null) {
			Team team = teamService.getTeam(leagueSeasonId, teamStruct.getIdTeam());
			if (team == null) {
				throw new LeagueException("Unknown team for id: "+teamStruct.getIdTeam());
			}
			logger.debug("Found team: "+team);
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
		logger.debug("Saving match: "+match);
		matchService.saveMatch(match);
		logger.debug("Saved match: "+match);
		return match;
	}

	public void updateMatchStatus(Match match) throws LeagueException {
		match.setStatus(MatchProcessingStatus.SAVED);
		matchService.saveMatch(match);
		logger.info("Set SAVED status for match id:" + match.getId()+" matchId:"+match.getMatchId());
	}

	private void savePlayerEvents(LeagueType leagueType, MatchFilActionStruct matchStruct, Match match) throws LeagueException {
		if (matchStruct.getLstEventMatchFilActionStruct() != null
				&& matchStruct.getLstEventMatchFilActionStruct().getValue() != null
				&& matchStruct.getLstEventMatchFilActionStruct().getValue().getEventMatchFilActionStruct() != null) {
			EventMatchFilActionStruct eventStruct = null;
			int size = matchStruct.getLstEventMatchFilActionStruct().getValue().getEventMatchFilActionStruct().size();
			for (int i=0;i<size;i++) {
				eventStruct = matchStruct.getLstEventMatchFilActionStruct().getValue().getEventMatchFilActionStruct().get(i);
				logger.info("Checking current event: score:"+getCurrentScore(eventStruct)
						+" matchTime: "+eventStruct.getTimeMatchStr().getValue());
				if (eventStruct.getActor1() != null && eventStruct.getActor1().getValue() != null) {
					PlayerMatchEvent event = new PlayerMatchEvent();
					event.setMatchTime(eventStruct.getTimeMatchStr().getValue());
					event.setPlayerMatch(getPlayerMatch(match, eventStruct.getActor1().getValue()));
					event.setEvent(getEvent(leagueType, eventStruct.getIdEvent()));
					playerService.savePlayerMatchEvent(event);
					logger.info("Saved player1's event: "+event);
				}				
				if (eventStruct.getActor2() != null && eventStruct.getActor2().getValue() != null) {
					PlayerMatchEvent event = new PlayerMatchEvent();
					event.setMatchTime(eventStruct.getTimeMatchStr().getValue());
					event.setPlayerMatch(getPlayerMatch(match, eventStruct.getActor2().getValue()));
					event.setEvent(getEvent(leagueType, eventStruct.getIdEvent()));
					playerService.savePlayerMatchEvent(event);
					logger.info("Saved player2's event: "+event);
				}
				//Last event - save the match's final score
				if (i == size-1) {
					match.setFinalScore(getCurrentScore(eventStruct));
					matchService.saveMatch(match);
				}
			}
		} else {
			throw new LeagueException("No match events to process for current match: " + matchStruct.getIdMatch());
		}
	}
	
	private String getCurrentScore(EventMatchFilActionStruct eventStruct) throws LeagueException {
		return "" + (eventStruct.getScoreA() != null ? eventStruct.getScoreA().getValue() : "")
		      +"-"+(eventStruct.getScoreB() != null ? eventStruct.getScoreB().getValue() : "");
	}
	
	@WriteTransaction
	protected PlayerMatch getPlayerMatch(Match match, ActorMatchFilActionStruct player) throws LeagueException {
		PlayerMatch playerMatch = cacheService.getPlayerMatch(match.getId(), player.getIdActor().longValue());
		if (playerMatch != null) {
			logger.debug("Got PlayerMatch from cache: "+playerMatch);
			return playerMatch;
		} else {
			playerMatch = new PlayerMatch();
			playerMatch.setMatch(match);
			playerMatch.setPlayer(getPlayer(player));
			playerMatch.setPlayerScore(0);
			matchService.savePlayerMatch(playerMatch);
			cacheService.setPlayerMatch(match.getId(), player.getIdActor().longValue(), playerMatch);
			logger.debug("Saved: "+playerMatch);
			return playerMatch;
		}
	}

	@ReadTransaction
	protected Player getPlayer(ActorMatchFilActionStruct playerStruct) throws LeagueException {
		Integer playerId = playerStruct.getIdActor();
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
	
	private Event getEvent(LeagueType leagueType, Integer eventId) throws LeagueException {
		if (eventId == null) {
			logger.error("No eventId specified!");
			return null;
		}
		Event event = cacheService.getEvent(leagueType.getId(), eventId);
		if (event != null) {
			return event;
		} else {
			logger.debug("Getting event leagueType: "+leagueType.getId()+" eventId:"+eventId);
			event = playerService.getEvent(leagueType.getId(), eventId);
			if (event == null) {
				logger.error("Unknown event for leagueType: "+leagueType+" eventId: " + eventId);
				event = new Event();
				event.setDescription("Unknown");
				event.setPoints(0);
				event.setEventId(eventId);
				event.setLeagueType(leagueType);
				playerService.saveEvent(event);
				logger.debug("Saved event: "+event);
				cacheService.setEvent(leagueType.getId(), event);				
			} else {
				cacheService.setEvent(leagueType.getId(), event);
			}
		}
		return event;
	}
}