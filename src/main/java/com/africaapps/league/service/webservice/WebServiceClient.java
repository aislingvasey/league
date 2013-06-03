package com.africaapps.league.service.webservice;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.BindingProvider;

import org.datacontract.schemas._2004._07.livemediastructs.ArrayOfMatchFilActionLightStruct;
import org.datacontract.schemas._2004._07.livemediastructs.MatchFilActionLightStruct;
import org.datacontract.schemas._2004._07.livemediastructs.MatchFilActionStruct;
import org.datacontract.schemas._2004._07.livemediastructs.TeamMatchFilActionStruct;
import org.datacontract.schemas._2004._07.livemediastructs.TeamStruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tempuri.IServiceAmiscoLive;
import org.tempuri.ServiceAmiscoLive;

import com.africaapps.league.exception.InvalidLeagueException;
import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.game.Pool;
import com.africaapps.league.model.league.League;
import com.africaapps.league.model.league.LeagueSeason;
import com.africaapps.league.service.feed.FeedService;
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
	
	private Integer getMatchKey() throws LeagueException {
		Integer matchKey = null;
		ArrayOfKeyValueOfintstring matchIntStrings = service1.getMatchStructDetailAvailable();
		for (ArrayOfKeyValueOfintstring.KeyValueOfintstring entry : matchIntStrings.getKeyValueOfintstring()) {
			logger.info("Got match key entry: " + entry.getKey() + "," + entry.getValue());
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
			logger.info("Got Fil match key entry: " + entry.getKey() + "," + entry.getValue());
			if (matchKey == null) {
				matchKey = Integer.valueOf(entry.getKey());
			}
		}
		if (matchKey == null) {
			throw new LeagueException("No fil match struct entry key retrieved!");
		}
		return matchKey;
	}

	public List<Integer> processMatches(League league, LeagueSeason leagueSeason, Pool pool, FeedService feedService)
			throws LeagueException {
		List<Integer> processedMatchIds = new ArrayList<Integer>();
		Integer matchId = null;
		int key = getFilMatchKey();
		int matchKey = getMatchKey();
		int count = 0;
		// for (ArrayOfKeyValueOfintstring.KeyValueOfintstring entry : intStrings.getKeyValueOfintstring()) {
		// key = entry.getKey();
		// logger.info("MatchFilActionStructDetailAvailable: " + key + "=" + entry.getValue());
		ArrayOfMatchFilActionLightStruct matchStructs = service1.matchFilActionLightStructList(key);
		logger.info("Got " + matchStructs.getMatchFilActionLightStruct().size() + " light matches to check...");
		for (MatchFilActionLightStruct matchLightStruct : matchStructs.getMatchFilActionLightStruct()) {
			matchId = matchLightStruct.getIdMatch();
			try {
				checkMatchCompetition(league, matchLightStruct);
				if (!feedService.isProcessedMatch(leagueSeason.getId(), matchId)) {
					logger.info("Match: " + matchId+" is unprocessed, processing it now...");
					MatchFilActionStruct matchStruct = service1.getMatchFilActionStruct(matchLightStruct.getIdMatch(), key);
					if (matchStruct != null) {
						logger.info("Got matchStruct to process for matchId: " + matchStruct.getIdMatch());
						saveMatchTeams(league, leagueSeason, pool, feedService, matchKey, matchStruct);
//						DataLogUtil.logMatchFilStruct(matchStruct);
						processMatch(league, leagueSeason, feedService, matchStruct);
						processedMatchIds.add(matchId);
						logger.info("Processed match struct for matchId: " + matchStruct.getIdMatch());
					} else {
						logger.error("No match struct for matchLightStruct: " + matchLightStruct.getIdMatch());
					}
				}
			} catch (InvalidLeagueException e) {
				logger.error(e.getMessage());
			}
			// TODO for testing only - remove to process all
			count++;
			if (count >= 1) {
				break;
			}
		}
		// }
		return processedMatchIds;
	}

	private void checkMatchCompetition(League league, MatchFilActionLightStruct matchLightStruct) throws InvalidLeagueException {
		if (matchLightStruct.getCompetitionName() != null) {
			if (!league.getName().equalsIgnoreCase(matchLightStruct.getCompetitionName().getValue())) {
				throw new InvalidLeagueException(
						" Received match for competitionName: " + matchLightStruct.getCompetitionName().getValue() 
						+ " matchId: " + matchLightStruct.getIdMatch()
						+ " Currently processing matches for league: " + league);
			}
		}
	}

	protected void saveMatchTeams(League league, LeagueSeason leagueSeason,
																Pool pool,
																FeedService feedService, Integer key,
																MatchFilActionStruct matchStruct) throws LeagueException {
		if (matchStruct.getLstTeamMatchFilActionStruct() != null
				&& matchStruct.getLstTeamMatchFilActionStruct().getValue() != null
				&& matchStruct.getLstTeamMatchFilActionStruct().getValue().getTeamMatchFilActionStruct() != null) {
			Integer teamId = null;
			for (TeamMatchFilActionStruct teamMatchStruct : matchStruct.getLstTeamMatchFilActionStruct().getValue()
					.getTeamMatchFilActionStruct()) {
				teamId = teamMatchStruct.getIdTeam();
				TeamStruct teamStruct = service1.getTeamStruct(matchStruct.getIdMatch(), teamId, key);
//				StringBuilder sb = new StringBuilder();
//				DataLogUtil.logTeamStruct(sb, teamStruct);
//				logger.info(sb.toString());
				feedService.saveTeamAndPlayers(league, leagueSeason, pool, teamStruct);
			}
		}
	}

	protected void processMatch(League league, LeagueSeason leagueSeason, FeedService feedService,
			MatchFilActionStruct matchStruct) throws LeagueException {
//		DataLogUtil.logMatchFilStruct(matchStruct);
		feedService.processMatch(league, leagueSeason, matchStruct);
	}
}
