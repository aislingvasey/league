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
import com.africaapps.league.model.league.League;
import com.africaapps.league.model.league.LeagueSeason;
import com.africaapps.league.model.league.Match;
import com.africaapps.league.model.league.MatchProcessingStatus;
import com.africaapps.league.model.league.Player;
import com.africaapps.league.model.league.PlayerMatch;
import com.africaapps.league.model.league.PlayerMatchStats;
import com.africaapps.league.model.league.Position;
import com.africaapps.league.model.league.PositionType;
import com.africaapps.league.model.league.Team;
import com.africaapps.league.service.cache.CacheService;
import com.africaapps.league.service.league.LeagueService;
import com.africaapps.league.service.match.MatchService;
import com.africaapps.league.service.player.PlayerService;
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

	private static Logger logger = LoggerFactory.getLogger(FeedServiceImpl.class);

	// TODO schedule feed processing - with retrys for failures, save status, datetime, etc

	@Override
	public void processFeed(String leagueName, String wsdlUrl, String username, String password) throws LeagueException {
		League league = getLeague(leagueName);
		WebServiceClient webServiceClient = setupWebServiceClient(wsdlUrl, username, password);
		// Pass 1: save teams and players
		saveTeams(webServiceClient, league);
		// Pass 2: save matches
//		processMatches(webServiceClient, league);
	}

	@WriteTransaction
	public void saveTeams(WebServiceClient webServiceClient, League league) throws LeagueException {
		List<TeamStruct> teamStructs = webServiceClient.getAvailableTeams(league.getName());
		if (teamStructs != null && teamStructs.size() > 0) {
			logger.info("Saving teams: "+teamStructs.size());
			LeagueSeason leagueSeason = getLeagueSeason(league);
			for(TeamStruct teamStruct : teamStructs) {
				Team team = saveTeam(league, leagueSeason, teamStruct);
				savePlayers(league, teamStruct, team);
			}
		} else {
			logger.error("No Match Structs received to be parsed!");
		}
	}

	@WriteTransaction
	protected Team saveTeam(League league, LeagueSeason leagueSeason, TeamStruct teamStruct)
			throws LeagueException {
		//Save team
		Team team = new Team();
		team.setTeamId(teamStruct.getIdTeam());
		team.setCity(teamStruct.getCity().getValue());
		team.setClubName(teamStruct.getClubName().getValue());
		team.setTeamName(teamStruct.getTeamName().getValue());
		team.setLeagueSeason(leagueSeason);
		//TODO coach is an actor
//		team.setCoach(teamStruct.getCoach().getValue().getIdA())
		teamService.saveTeam(team);
		logger.info("Saved team: "+team);
		return team;
	}
	
	@WriteTransaction
	protected void savePlayers(League league, TeamStruct teamStruct, Team team) throws LeagueException {
		if (teamStruct.getLstActorStruct() != null && teamStruct.getLstActorStruct().getValue() != null
				&& teamStruct.getLstActorStruct().getValue().getActorStruct() != null) {
			for(ActorStruct actorStruct : teamStruct.getLstActorStruct().getValue().getActorStruct()) {
				logger.debug("Processing player: "+actorStruct.getIdActor());
				Player player = new Player();
				player.setPlayerId(actorStruct.getIdActor());
				player.setFirstName(actorStruct.getFirstName() != null ? actorStruct.getFirstName().getValue() : "");
				player.setLastName(actorStruct.getSecondName() != null ? actorStruct.getSecondName().getValue() : "");
				player.setNickName(actorStruct.getNickName() != null ? actorStruct.getNickName().getValue() : "");
				player.setPosition(getPosition(league, actorStruct.getIdPosition().getValue()));
				player.setShirtNumber(actorStruct.getShirtNumber() != null ? actorStruct.getShirtNumber().getValue() : 0);
				player.setTeam(team);
				playerService.savePlayer(player);
				logger.info("Saved player: "+player);
			}
		}
	}
	
	private Position getPosition(League league, Integer positionNumber) throws LeagueException {
		if (positionNumber == null) {
			logger.error("No positionNumber specified!");
			return null;
		}
		Position position = cacheService.getPosition(league.getLeagueType().getId(), positionNumber);
		if (position != null) {
			return position;
		} else {
			logger.info("Getting position for league type: "+league.getLeagueType().getId()+" positionNumber:"+positionNumber);
			position = playerService.getPosition(league.getLeagueType().getId(), positionNumber);
			if (position == null) {
				logger.error("Unknown position for league: "+league+" and positionNumber: " + positionNumber);
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

	// ------------------------------------------------------------------------------------------

	protected void processMatches(WebServiceClient webServiceClient, League league) throws LeagueException {
		List<MatchFilActionStruct> matchStructs = webServiceClient.getAvailableFilMatches();
		if (matchStructs != null && matchStructs.size() > 0) {
			LeagueSeason leagueSeason = validateLeague(league, matchStructs.get(0));
			parseMatchFilActionStruct(league, leagueSeason, matchStructs);
		} else {
			logger.error("No Match Structs received to be parsed!");
		}
	}
	
	protected void parseMatchFilActionStruct(League league, LeagueSeason leagueSeason, List<MatchFilActionStruct> structs)
			throws LeagueException {
		logger.info("Parsing matches: " + structs.size());
		for (MatchFilActionStruct matchStruct : structs) {
			if (!isProcessedMatch(matchStruct.getIdMatch())) {
				processMatch(league, leagueSeason, matchStruct);
			}
		}
	}

	protected LeagueSeason validateLeague(League league, MatchFilActionStruct matchStruct) throws LeagueException {
		checkLeague(league, matchStruct);
		return getLeagueSeason(league);
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

	protected void checkLeague(League league, MatchFilActionStruct matchStruct) throws LeagueException {
		String name = matchStruct.getCompetitionName().getValue();
		if (!league.getName().equalsIgnoreCase(name)) {
			throw new LeagueException("League/Matches Mismatch! Expected matches for league: " + league.getName()
					+ " but got: " + name);
		} else {
			logger.info("Processing matches for league: " + name);
		}
	}

	protected boolean isProcessedMatch(int matchId) throws LeagueException {
		return matchService.isProcessedMatch(matchId);
	}

	@WriteTransaction
	protected void processMatch(League league, LeagueSeason leagueSeason, MatchFilActionStruct matchStruct)
			throws LeagueException {
		checkTeams(matchStruct);
		Team team1 = getTeam(leagueSeason.getId(), matchStruct, true);
		Team team2 = getTeam(leagueSeason.getId(), matchStruct, false);
		Match match = saveMatch(leagueSeason, matchStruct, team1, team2);
		savePlayerStatistics(matchStruct, match);
		updateMatchStatus(match);
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
			return team;
		} else {
			throw new LeagueException("Invalid null team firstTeam: " + firstTeam);
		}
	}

	private Match saveMatch(LeagueSeason leagueSeason, MatchFilActionStruct matchStruct, Team team1, Team team2)
			throws LeagueException {
		Match match = new Match();
		match.setId(matchStruct.getIdMatch().longValue());
		match.setFinalScore(null);
		match.setLocation(null);
		match.setStartDateTime(WebServiceXmlUtil.getDate(matchStruct.getDateAndTime()));
		match.setEndDateTime(null);
		match.setTeam1(team1);
		match.setTeam2(team2);
		match.setStatus(MatchProcessingStatus.INPROGRESS);
		matchService.saveMatch(match);
		return match;
	}

	public void updateMatchStatus(Match match) throws LeagueException {
		match.setStatus(MatchProcessingStatus.COMPLETE);
		matchService.saveMatch(match);
		logger.info("Completed match: " + match.getId());
	}

	private void savePlayerStatistics(MatchFilActionStruct matchStruct, Match match) throws LeagueException {
		if (matchStruct.getLstEventMatchFilActionStruct() != null
				&& matchStruct.getLstEventMatchFilActionStruct().getValue() != null
				&& matchStruct.getLstEventMatchFilActionStruct().getValue().getEventMatchFilActionStruct() != null) {
			for (EventMatchFilActionStruct eventStruct : matchStruct.getLstEventMatchFilActionStruct().getValue()
					.getEventMatchFilActionStruct()) {
				if (eventStruct.getActor1() != null && eventStruct.getActor1().getValue() != null) {
					PlayerMatchStats stats = new PlayerMatchStats();
					stats.setMatchTime(eventStruct.getTimeMatchStr().getValue());
					stats.setPlayerMatch(getPlayerMatch(match, eventStruct.getActor1().getValue()));
				}
			}
		} else {
			throw new LeagueException("No match events to process for current match: " + matchStruct.getIdMatch());
		}
	}

	@WriteTransaction
	protected PlayerMatch getPlayerMatch(Match match, ActorMatchFilActionStruct player) throws LeagueException {
		PlayerMatch playerMatch = cacheService.getPlayerMatch(match.getId(), player.getIdActor().longValue());
		if (playerMatch != null) {
			return playerMatch;
		} else {
			playerMatch = new PlayerMatch();
			playerMatch.setMatch(match);
			playerMatch.setPlayer(getPlayer(player));
			playerMatch.setPlayerScore(0);
			matchService.savePlayerMatch(playerMatch);
			cacheService.setPlayerMatch(match.getId(), player.getIdActor().longValue(), playerMatch);
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
}