package com.africaapps.league.service.webservice;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

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
import com.africaapps.league.service.feed.MatchFilter;
import com.africaapps.league.service.webservice.ssl.HeaderHandlerResolver;
import com.africaapps.league.service.webservice.ssl.TrustEverythingTrustManager;
import com.africaapps.league.service.webservice.ssl.VerifyEverythingHostnameVerifier;
import com.africaapps.league.util.WebServiceXmlUtil;
import com.microsoft.schemas._2003._10.serialization.arrays.ArrayOfKeyValueOfintstring;

public class WebServiceClient {
	
	private IServiceAmiscoLive service1;

	private static Logger logger = LoggerFactory.getLogger(WebServiceClient.class);

	static {
		 TrustManager[] trustManager = new TrustManager[] {new TrustEverythingTrustManager()};
	    // Let us create the factory where we can set some parameters for the connection
	    SSLContext sslContext = null;
	    try {
	        sslContext = SSLContext.getInstance("SSL");
	        sslContext.init(null, trustManager, new java.security.SecureRandom());
	        logger.info("Set up trust everything SSL context");
	    } catch (NoSuchAlgorithmException e) {
	        logger.error("Meh1", e);
	    }catch (KeyManagementException e) {
	    	logger.error("Meh2", e);
	    }
	    HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
	    
	    javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(new VerifyEverythingHostnameVerifier()); 
	    
	    //Logging
//	    System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "false");	    
	} 
	
	public WebServiceClient(String url, String username, String password) {
		setupService(url, username, password);
	}

	private void setupService(String url, String username, String password) {
		try {
			URL wsdl = new URL(url);
			
			ServiceAmiscoLive serviceAmiscoLive = new ServiceAmiscoLive(wsdl);			
			
			HeaderHandlerResolver handlerResolver = new HeaderHandlerResolver();
			serviceAmiscoLive.setHandlerResolver(handlerResolver);
			
			service1 = serviceAmiscoLive.getAmiscoBindingIServiceAmiscoLive();
			
//			((BindingProvider) service1).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, username);
//			((BindingProvider) service1).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, password);
		} catch (MalformedURLException e) {
			logger.error("Error with web service URL:" + e.getMessage());
		} catch (Throwable t) {
			logger.error("Caught web service exception:", t);
		}
	}

	public boolean isServiceReady() {
		if (service1 != null) {
			try {
				return service1.isReady();
			} catch (Exception e) {
				logger.error("Error calling isServiceReady()", e);
				return false;
			}
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
	
	private boolean isValidLeagueSeasonMatch(LeagueSeason leagueSeason, Date matchDate){
		if (matchDate != null) {
			if (matchDate.getTime() > System.currentTimeMillis()) {
				//future date - match not played yet
				return false;	
			} else if (leagueSeason.getStartDate().getTime() <= matchDate.getTime() 
					&& matchDate.getTime() <= leagueSeason.getEndDate().getTime()) {
				logger.info("Match is within the current leagueSeason");
				return true;
			} else {
				logger.info("Match is outside the current leagueSeason");
				return false;
			}			
		} else {
			logger.error("Invalid matchDate: "+matchDate);
			return false;
		}
	}

	public List<Integer> processMatches(League league, LeagueSeason leagueSeason, Pool pool, FeedService feedService,
			MatchFilter matchFilter) throws LeagueException {
		List<Integer> processedMatchIds = new ArrayList<Integer>();
		
		int key = getFilMatchKey();
		int matchKey = getMatchKey();
		logger.info("Using FilMatchKey:" + key + " matchKey: " + matchKey);
		
		Integer matchId = null;
		Date matchDate = null;
		ArrayOfMatchFilActionLightStruct matchStructs = service1.matchFilActionLightStructList(key);
		logger.info("Got " + matchStructs.getMatchFilActionLightStruct().size() + " light matches to check...");
		for (MatchFilActionLightStruct matchLightStruct : matchStructs.getMatchFilActionLightStruct()) {
			matchId = matchLightStruct.getIdMatch();
			matchDate = WebServiceXmlUtil.getDate(matchLightStruct.getDateAndTime());
			try {
				checkMatchCompetition(league, matchLightStruct);
				if (isValidLeagueSeasonMatch(leagueSeason, matchDate)) {
					if (!feedService.isProcessedMatch(leagueSeason.getId(), matchId)) {
						if (matchFilter != null && !matchFilter.isValidMatch(matchId, matchDate)) {
							logger.warn("Skipping processing match:" + matchId + " due to match filter: "+matchFilter);
						} else {
							logger.info("Match: " + matchId + " is unprocessed Processing it now...");
							MatchFilActionStruct matchStruct = service1.getMatchFilActionStruct(matchLightStruct.getIdMatch(), key);
							if (matchStruct != null) {
								logger.info("Got matchStruct to process for matchId: " + matchStruct.getIdMatch());
								List<TeamStruct> teams = saveMatchTeams(league, leagueSeason, pool, feedService, matchKey, matchStruct);
								processMatch(league, leagueSeason, feedService, matchStruct, teams);
								processedMatchIds.add(matchId);
								logger.info("Processed match struct for matchId: " + matchStruct.getIdMatch());											
							} else {
								logger.error("No match struct for matchLightStruct: " + matchLightStruct.getIdMatch());
							}
						}
					} else {
						logger.info("Match is already processed: " + matchId);
					}
				} else {
					logger.info("Match not processed: outside of league season: "+matchId+" "+matchDate);
				}
			} catch (InvalidLeagueException e) {
				logger.error(e.getMessage());
			}
		}
		return processedMatchIds;
	}

	private void checkMatchCompetition(League league, MatchFilActionLightStruct matchLightStruct)
			throws InvalidLeagueException {
		if (matchLightStruct.getCompetitionName() != null) {
			if (!league.getName().equalsIgnoreCase(matchLightStruct.getCompetitionName().getValue())) {
				throw new InvalidLeagueException(" Received match for competitionName: "
						+ matchLightStruct.getCompetitionName().getValue() + " matchId: " + matchLightStruct.getIdMatch()
						+ " Currently processing matches for league: " + league);
			}
		}
	}

	protected List<TeamStruct> saveMatchTeams(League league, LeagueSeason leagueSeason, Pool pool, FeedService feedService, Integer key,
			MatchFilActionStruct matchStruct) throws LeagueException {
		List<TeamStruct> teams = new ArrayList<TeamStruct>();
		
		if (matchStruct.getLstTeamMatchFilActionStruct() != null
				&& matchStruct.getLstTeamMatchFilActionStruct().getValue() != null
				&& matchStruct.getLstTeamMatchFilActionStruct().getValue().getTeamMatchFilActionStruct() != null) {
			Integer teamId = null;
//			DataLogUtil.logMatchFilStruct(matchStruct);
			for (TeamMatchFilActionStruct teamMatchStruct : matchStruct.getLstTeamMatchFilActionStruct().getValue().getTeamMatchFilActionStruct()) {
				teamId = teamMatchStruct.getIdTeam();
				logger.info("Getting team struct from service...");
				TeamStruct teamStruct = service1.getTeamStruct(matchStruct.getIdMatch(), teamId, key);
//				StringBuilder sb = new StringBuilder();
//				DataLogUtil.logTeamStruct(sb, teamStruct);
//				logger.info(sb.toString());
				feedService.saveTeamAndPlayers(league, leagueSeason, pool, teamStruct);
				teams.add(teamStruct);
			}
		}
		return teams;
	}

	protected void processMatch(League league, LeagueSeason leagueSeason, FeedService feedService,
			MatchFilActionStruct matchStruct, List<TeamStruct> teams) throws LeagueException {
		feedService.processMatch(league, leagueSeason, matchStruct, teams);
	}
}
