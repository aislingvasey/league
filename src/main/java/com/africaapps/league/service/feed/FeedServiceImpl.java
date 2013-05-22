package com.africaapps.league.service.feed;

import java.util.List;

import org.datacontract.schemas._2004._07.livemediastructs.ActorMatchFilActionStruct;
import org.datacontract.schemas._2004._07.livemediastructs.EventMatchFilActionStruct;
import org.datacontract.schemas._2004._07.livemediastructs.MatchFilActionStruct;
import org.datacontract.schemas._2004._07.livemediastructs.TeamMatchFilActionStruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.league.League;
import com.africaapps.league.model.league.LeagueSeason;
import com.africaapps.league.model.league.Match;
import com.africaapps.league.model.league.MatchProcessingStatus;
import com.africaapps.league.model.league.Player;
import com.africaapps.league.model.league.PlayerMatch;
import com.africaapps.league.model.league.PlayerMatchStats;
import com.africaapps.league.model.league.Team;
import com.africaapps.league.service.cache.CacheService;
import com.africaapps.league.service.league.LeagueService;
import com.africaapps.league.service.match.MatchService;
import com.africaapps.league.service.player.PlayerService;
import com.africaapps.league.service.team.TeamService;
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
	
	@Override
	public void processFeed(String leagueName, String wsdlUrl, String username, String password) throws LeagueException {
		League league = getLeague(leagueName);
		WebServiceClient webServiceClient = setupWebServiceClient(wsdlUrl, username, password);
		parseMatchFilActionStruct(league, webServiceClient.getAvailableFilMatches());
	}
	
	public void parseMatchFilActionStruct(League league, List<MatchFilActionStruct> structs) throws LeagueException {
		if (structs != null && structs.size() > 0) {
			checkLeague(league, structs.get(0));
			LeagueSeason leagueSeason = getLeagueSeason(league);
			logger.info("Parsing match structs: "+structs.size());		
			for(MatchFilActionStruct matchStruct : structs) {
				if (!isProcessedMatch(matchStruct.getIdMatch())) {
					processMatch(league, leagueSeason, matchStruct);
				}
			}
		} else {
			logger.error("No match structs to parse");
		}		
	}
	
	protected WebServiceClient setupWebServiceClient(String wsdlUrl, String username, String password) throws LeagueException {
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
			throw new LeagueException("Unknown league for name: "+leagueName);
		} else {
			logger.info("Starting feed for league: "+league.getName());			
			return league;
		}
	}
	
	protected LeagueSeason getLeagueSeason(League league) throws LeagueException {
		LeagueSeason season = leagueService.getCurrentSeason(league);
		if (season == null) {
			throw new LeagueException("Unable to get current season for League: "+league);
		} else {
			logger.info("Current season: "+season);
			return season;
		}
	}

	protected void checkLeague(League league, MatchFilActionStruct matchStruct) throws LeagueException {
		String name = matchStruct.getCompetitionName().getValue();
		if (!league.getName().equalsIgnoreCase(name)) {
			throw new LeagueException("League/Matches Mismatch! Expected matches for league: "+league.getName()+" but got: "+name);
		} else {
			logger.info("Processing matches for league: "+name);
		}
	}
	
	protected boolean isProcessedMatch(Integer matchId) throws LeagueException {
		return matchService.isProcessedMatch(matchId.longValue());
	}
	
	@Transactional(readOnly=false, rollbackFor=LeagueException.class)
	protected void processMatch(League league, LeagueSeason leagueSeason, MatchFilActionStruct matchStruct) throws LeagueException {
		//TODO possibly get team and players details from separate feed calls first and then save matches - throws exception to break out
		//and recall the feed again after processing the teams and players
		checkTeams(matchStruct);
		Team team1 = saveTeam(matchStruct, true);
		Team team2 = saveTeam(matchStruct, false);
		
		Match match = saveMatch(leagueSeason, matchStruct, team1, team2);

		savePlayerStatistics(matchStruct, match);
		
		updateMatchStatus(match);
	}
	
	private void checkTeams(MatchFilActionStruct matchStruct) throws LeagueException {
		if (matchStruct.getLstTeamMatchFilActionStruct() != null
				&& matchStruct.getLstTeamMatchFilActionStruct().getValue() != null
				&& matchStruct.getLstTeamMatchFilActionStruct().getValue().getTeamMatchFilActionStruct() != null) {
			List<TeamMatchFilActionStruct> teamStructs = matchStruct.getLstTeamMatchFilActionStruct().getValue().getTeamMatchFilActionStruct();
			if (teamStructs.size() != 2) {
				throw new LeagueException("Expected two teams, found: "+teamStructs.size()+" for match: "+matchStruct.getIdMatch());
			}
		} else {
			throw new LeagueException("No teams found for match: "+matchStruct.getIdMatch());
		}
	}
	
	private Team saveTeam(MatchFilActionStruct matchStruct, boolean firstTeam) throws LeagueException {
		List<TeamMatchFilActionStruct> teamStructs = matchStruct.getLstTeamMatchFilActionStruct().getValue().getTeamMatchFilActionStruct();
		TeamMatchFilActionStruct teamStruct = null;
		if (firstTeam) {
			teamStruct = teamStructs.get(0);
		} else {
			teamStruct = teamStructs.get(1);
		}
		if (teamStruct != null) {
			Team team = new Team();
			team.setId(teamStruct.getIdTeam().longValue());
			team.setClubName(teamStruct.getTeamName().getValue());
			team.setTeamName(null);
			team.setCity(null);
			team.setManager(null);
			team.setCoach(null);
			teamService.saveTeam(team);
			return team;
		} else {
			throw new LeagueException("Invalid null team firstTeam: "+firstTeam);
		}
	}
	
	private Match saveMatch(LeagueSeason leagueSeason, MatchFilActionStruct matchStruct, Team team1, Team team2) throws LeagueException {
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
		logger.info("Completed match: "+match.getId());
	}
	
	private void savePlayerStatistics(MatchFilActionStruct matchStruct, Match match) throws LeagueException {
		if (matchStruct.getLstEventMatchFilActionStruct() != null
				&& matchStruct.getLstEventMatchFilActionStruct().getValue() != null
				&& matchStruct.getLstEventMatchFilActionStruct().getValue().getEventMatchFilActionStruct() != null) {
			for(EventMatchFilActionStruct eventStruct : matchStruct.getLstEventMatchFilActionStruct().getValue().getEventMatchFilActionStruct()) {				
				if (eventStruct.getActor1() != null && eventStruct.getActor1().getValue() != null) {
					PlayerMatchStats stats = new PlayerMatchStats();
					stats.setMatchTime(eventStruct.getTimeMatchStr().getValue());
					stats.setPlayerMatch(getPlayerMatch(match, eventStruct.getActor1().getValue()));
				}
			}
		} else {
			throw new LeagueException("No match events to process for current match: "+matchStruct.getIdMatch());
		}
	}
	
	@Transactional(readOnly=false, rollbackFor=LeagueException.class)
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
	
	@Transactional(readOnly=true)
	protected Player getPlayer(ActorMatchFilActionStruct playerStruct) throws LeagueException {
		Long playerId = playerStruct.getIdActor().longValue();
		Player player = cacheService.getPlayer(playerId);
		if (player != null) {
			return player;
		} else {
			player = playerService.getPlayer(playerId);
			if (player == null) {
				throw new LeagueException("Unknown player for id: "+playerId);
			} else {
				cacheService.setPlayer(player);
			}
		}
		return player;
	}
}