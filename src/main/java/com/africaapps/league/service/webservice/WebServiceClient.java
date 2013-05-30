package com.africaapps.league.service.webservice;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.BindingProvider;

import org.datacontract.schemas._2004._07.livemediastructs.ArrayOfMatchFilActionLightStruct;
import org.datacontract.schemas._2004._07.livemediastructs.ArrayOfMatchLightStruct;
import org.datacontract.schemas._2004._07.livemediastructs.MatchFilActionLightStruct;
import org.datacontract.schemas._2004._07.livemediastructs.MatchFilActionStruct;
import org.datacontract.schemas._2004._07.livemediastructs.MatchLightStruct;
import org.datacontract.schemas._2004._07.livemediastructs.MatchStruct;
import org.datacontract.schemas._2004._07.livemediastructs.TeamMatchFilActionStruct;
import org.datacontract.schemas._2004._07.livemediastructs.TeamStruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tempuri.IServiceAmiscoLive;
import org.tempuri.ServiceAmiscoLive;

import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.league.League;
import com.africaapps.league.model.league.LeagueSeason;
import com.africaapps.league.service.feed.FeedService;
import com.africaapps.league.service.webservice.util.DataLogUtil;
import com.microsoft.schemas._2003._10.serialization.arrays.ArrayOfKeyValueOfintstring;

public class WebServiceClient {

	private IServiceAmiscoLive service1;

	private static Logger logger = LoggerFactory.getLogger(WebServiceClient.class);

	public WebServiceClient(String url, String username, String password) {
		setupService(url, username, password);
	}

	private void setupService(String url, String username, String password) {
		try {
			URL wsdl = new URL(url);
			ServiceAmiscoLive serviceAmiscoLive = new ServiceAmiscoLive(wsdl);
			service1 = serviceAmiscoLive.getAmiscoBindingIServiceAmiscoLive1();
			((BindingProvider) service1).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, username);
			((BindingProvider) service1).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, password);
		} catch (MalformedURLException e) {
			logger.error("Error with web service URL:" + e.getMessage());
		} catch (Throwable t) {
			logger.error("Caught web service exception:", t);
		}
	}

	public boolean isServiceReady() {
		if (service1 != null) {
			return service1.isReady();
		} else {
			logger.error("Service not initialised for use");
			return false;
		}
	}

	public String getServiceVersion() {
		if (service1 != null) {
			return service1.serviceVersion();
		} else {
			return null;
		}
	}
	
	public List<TeamStruct> getFirstAvailableTeam(String leagueName) throws LeagueException {
		List<TeamStruct> structs = new ArrayList<TeamStruct>();

		ArrayOfKeyValueOfintstring intStrings = service1.getMatchStructDetailAvailable();
		if (intStrings.getKeyValueOfintstring().size() > 0) {
			// for(ArrayOfKeyValueOfintstring.KeyValueOfintstring entry :
			// intStrings.getKeyValueOfintstring()) {
			ArrayOfKeyValueOfintstring.KeyValueOfintstring entry = intStrings.getKeyValueOfintstring().get(0);
			logger.info("MatchDetailAvailable: " + entry.getKey() + "=" + entry.getValue());
			ArrayOfMatchLightStruct matchStructs = service1.matchLightStructList(entry.getKey());
			logger.info("Got " + matchStructs.getMatchLightStruct().size() + " (light) matches");
			if (matchStructs.getMatchLightStruct().size() > 0) {
				// for(MatchLightStruct matchLightStruct :
				// matchStructs.getMatchLightStruct()) {
				MatchLightStruct matchLightStruct = matchStructs.getMatchLightStruct().get(0);
				if (!matchLightStruct.getCompetitionName().getValue().equalsIgnoreCase(leagueName)) {
					throw new LeagueException("League/Matches Mismatch! Expected matches for league: " + leagueName
							+ " but got: " + matchLightStruct.getCompetitionName().getValue());
				}
				// DataLogUtil.logMatchLightStruct(matchLightStruct);
				MatchStruct matchStruct = service1.getMatchStruct(matchLightStruct.getIdMatch(), entry.getKey());
				if (matchStruct != null) {
					// DataLogUtil.logMatchStruct(matchStruct);
					if (matchStruct.getLstTeamStruct() != null && matchStruct.getLstTeamStruct().getValue() != null
							&& matchStruct.getLstTeamStruct().getValue().getTeamStruct().size() > 0) {
						for (TeamStruct teamStruct : matchStruct.getLstTeamStruct().getValue().getTeamStruct()) {
							// 1st Team
							// TeamStruct teamStruct = matchStruct.getLstTeamStruct().getValue().getTeamStruct().get(0);
							TeamStruct retrievedTeam = service1.getTeamStruct(matchStruct.getIdMatch(), teamStruct.getIdTeam(),
									entry.getKey());
							structs.add(retrievedTeam);
							logger.info("Retrieved teamStruct to process: " + retrievedTeam.getIdTeam());
							// StringBuilder sb = new StringBuilder();
							// DataLogUtil.logTeamStruct(sb, retrievedTeam);
							// logger.info("Team: " + sb.toString());
						}
					}
				} else {
					logger.info("No match struct for matchLightStruct: " + matchLightStruct.getIdMatch());
				}
			}
		}
		return structs;
	}
	
	private Integer getMatchKey() throws LeagueException {
		Integer matchKey = null;
		ArrayOfKeyValueOfintstring matchIntStrings = service1.getMatchStructDetailAvailable();
		for (ArrayOfKeyValueOfintstring.KeyValueOfintstring entry : matchIntStrings.getKeyValueOfintstring()) {
			logger.info("Got match key entry: "+entry.getKey()+","+entry.getValue());
			if (matchKey == null) {
				matchKey = Integer.valueOf(entry.getKey());
			}
		}
		if (matchKey == null) {
			throw new LeagueException("No match struct entry key retrieved!");
		}
		return matchKey;
	}
	
	private Integer getFilMatchKey() throws LeagueException {
		Integer matchKey = null;
		ArrayOfKeyValueOfintstring intStrings = service1.getMatchFilActionStructDetailAvailable();
		for (ArrayOfKeyValueOfintstring.KeyValueOfintstring entry : intStrings.getKeyValueOfintstring()) {
			logger.info("Got Fil match key entry: "+entry.getKey()+","+entry.getValue());
			if (matchKey == null) {
				matchKey = Integer.valueOf(entry.getKey());
			}
		}
		if (matchKey == null) {
			throw new LeagueException("No fil match struct entry key retrieved!");
		}
		return matchKey;
	}

	public List<Integer> processMatches(League league, LeagueSeason leagueSeason, FeedService feedService) 
		throws LeagueException {
		List<Integer> processedMatchIds = new ArrayList<Integer>();		
		Integer matchId = null;
		int key = getFilMatchKey();
		int matchKey = getMatchKey();
		int count = 0;
//		for (ArrayOfKeyValueOfintstring.KeyValueOfintstring entry : intStrings.getKeyValueOfintstring()) {
//			key = entry.getKey();
//			logger.info("MatchFilActionStructDetailAvailable: " + key + "=" + entry.getValue());
			ArrayOfMatchFilActionLightStruct matchStructs = service1.matchFilActionLightStructList(key);
			logger.info("Got " + matchStructs.getMatchFilActionLightStruct().size() + " light matches to check...");
			for (MatchFilActionLightStruct matchLightStruct : matchStructs.getMatchFilActionLightStruct()) {
				matchId = matchLightStruct.getIdMatch();
				checkMatchCompetition(league, matchLightStruct);
				if (!feedService.isProcessedMatch(leagueSeason.getId(), matchId)) {
					logger.info("Unprocessed match: "+matchId);					
					MatchFilActionStruct matchStruct = service1.getMatchFilActionStruct(matchLightStruct.getIdMatch(), key);
					if (matchStruct != null) {						
						logger.info("Got match struct to process for matchId: " + matchStruct.getIdMatch());
						saveMatchTeams(league, leagueSeason, feedService, matchKey, matchStruct);
						DataLogUtil.logMatchFilStruct(matchStruct);
						processMatch(league, leagueSeason, feedService, matchStruct);
						processedMatchIds.add(matchId);
						logger.info("Processed match struct for matchId: "+matchStruct.getIdMatch());
					} else {
						logger.error("No match struct for matchLightStruct: " + matchLightStruct.getIdMatch());
					}				
				}
				count++;
				if (count >= 1) {
					break;
				}
			}
//		}		
		return processedMatchIds;
	}
	
	private void checkMatchCompetition(League league, MatchFilActionLightStruct matchLightStruct) throws LeagueException {
		if (matchLightStruct.getCompetitionName() != null) {
			if (!league.getName().equalsIgnoreCase(matchLightStruct.getCompetitionName().getValue())) {
				/*throw new LeagueException*/logger.error("Processing match for league: "+league
						+" but received match for competition: "+matchLightStruct.getCompetitionName().getValue()
						+" matchId: "+matchLightStruct.getIdMatch());
			}
		}
	}

	protected void saveMatchTeams(League league, LeagueSeason leagueSeason, FeedService feedService, Integer key, MatchFilActionStruct matchStruct)
			throws LeagueException {
		if (matchStruct.getLstTeamMatchFilActionStruct() != null
				&& matchStruct.getLstTeamMatchFilActionStruct().getValue() != null
				&& matchStruct.getLstTeamMatchFilActionStruct().getValue().getTeamMatchFilActionStruct() != null) {
			Integer teamId = null;
			for(TeamMatchFilActionStruct teamMatchStruct : matchStruct.getLstTeamMatchFilActionStruct().getValue().getTeamMatchFilActionStruct()) {
				teamId = teamMatchStruct.getIdTeam();
				TeamStruct teamStruct = service1.getTeamStruct(matchStruct.getIdMatch(), teamId, key);
				StringBuilder sb = new StringBuilder();
				DataLogUtil.logTeamStruct(sb, teamStruct);
				logger.info(sb.toString());
				feedService.saveTeamAndPlayers(league, leagueSeason, teamStruct);
			}
		}
	}

	protected void processMatch(League league, LeagueSeason leagueSeason, FeedService feedService, MatchFilActionStruct matchStruct) 
			throws LeagueException {
		DataLogUtil.logMatchFilStruct(matchStruct);
		feedService.processMatch(league, leagueSeason, matchStruct);
	}
}
