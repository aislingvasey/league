package com.africaapps.league.service.webservice;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import awdl.history.ArrayOfEncounterInfo;
import awdl.history.ArrayOfSeasonInfo;
import awdl.history.ArrayOfint;
import awdl.history.EncounterInfo;
import awdl.history.IServiceAmisco;
import awdl.history.SeasonInfo;
import awdl.history.ServiceAmisco;

import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.league.League;
import com.africaapps.league.service.webservice.ssl.HeaderHandlerResolver;
import com.africaapps.league.service.webservice.ssl.TrustEverythingTrustManager;
import com.africaapps.league.service.webservice.ssl.VerifyEverythingHostnameVerifier;
import com.africaapps.league.util.WebServiceXmlUtil;

public class WebServiceHistoryClient {

//	private static final String LANGUAGE_ID = "3";
	
	private IServiceAmisco service;
	
	private static Logger logger = LoggerFactory.getLogger(WebServiceHistoryClient.class);

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
	    } catch (KeyManagementException e) {
	    	logger.error("Meh2", e);
	    }
	    HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
	    
	    javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(new VerifyEverythingHostnameVerifier()); 
	    
	    //Web service package logging
//	    System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "false");	
//	    System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", "false");
	} 
	
	public WebServiceHistoryClient(String url, String username, String password) {
		setupService(url, username, password);
	}

	private void setupService(String url, String username, String password) {
		try {
			URL wsdl = new URL(url);
			
			ServiceAmisco serviceAmisco = new ServiceAmisco(wsdl);			
			
			HeaderHandlerResolver handlerResolver = new HeaderHandlerResolver();
			serviceAmisco.setHandlerResolver(handlerResolver);
			
			service = serviceAmisco.getBasicHttpBindingIServiceAmisco();			
		} catch (MalformedURLException e) {
			logger.error("Error with web service URL:" + e.getMessage());
		} catch (Throwable t) {
			logger.error("Caught web service exception:", t);
		}
	}

	public boolean isServiceReady() {
		if (service != null) {
			try {
				return service.isReady();
			} catch (Exception e) {
				logger.error("Error calling isServiceReady()", e);
				return false;
			}
		} else {
			logger.error("Service not initialised for use");
			return false;
		}
	}
	
	public List<Integer> processMatchesForStats(
			/*FeedService feedService, */League league/*, LeagueSeason leagueSeason, Pool pool, MatchFilter matchFilter*/) 
	  throws LeagueException {
		List<Integer> processedGameIds = new ArrayList<>();		
		
//		Integer competitionId = getCompetitionId();
		
		Integer seasonId = getSeasonId();
		
		ArrayOfint actorIds = new ArrayOfint();
		ArrayOfint teamIds = new ArrayOfint();
		ArrayOfint statIds = new ArrayOfint();
//	List<Statistic> stats = getStatisticsCategories();
//		for(Statistic stat : stats) {
//			statIds.getInt().add(stat.getStatDefId());
//		}
		statIds.getInt().add(26);
//		statIds.getInt().add(55);
		
		ArrayOfEncounterInfo encounters = service.getEncounters(null, seasonId, null);
		logger.info("Number of encounters: "+encounters.getEncounterInfo().size());
		for(EncounterInfo encounter : encounters.getEncounterInfo()) {
//		if (encounters.getEncounterInfo().size() > 0) {
//			EncounterInfo encounter = encounters.getEncounterInfo().get(0);
			teamIds.getInt().clear();
			actorIds.getInt().clear();
			
//			actorIds.getInt().add(355181);
			
//			logger.info("Encounter: id:"+encounter.getIdEncounter()+" date:"+WebServiceXmlUtil.getDate(encounter.getDateAndTime())+" teamA:"+encounter.getIdTeamA()+" teamB:"+encounter.getIdTeamB());
			if (league.getName().equals(encounter.getCompetitionName().getValue())) {
//		TODO		if (!isProcessedMatch(feedService, leagueSeason.getId(), encounter.getIdEncounter())) {
				
				ArrayOfint idEncounters = new ArrayOfint();
				idEncounters.getInt().add(encounter.getIdEncounter());
				logger.info("Current encounter: encounterId:"+encounter.getIdEncounter()+" date:"+WebServiceXmlUtil.getDate(encounter.getDateAndTime()));
				
//				teamIds.getInt().add(encounter.getIdTeamA());
//				teamIds.getInt().add(encounter.getIdTeamB());
//				ArrayOfCustomTeam teams = service.getTeam(teamIds);
//				for(CustomTeam team : teams.getCustomTeam()) {
//					logger.info("Got team: "+team.getIdTeam()+", "+team.getClubName().getValue()+", "+team.getName().getValue());					
//				}
				
//				ArrayOfPlayerInfo players = service.getEncounterActors(encounter.getIdEncounter());
//				for(PlayerInfo player : players.getPlayerInfo()) {
//					logger.info("Got player: "
//							+" id:"+player.getIdPlayer()
//							+" firstName:"+player.getFirstName().getValue()
//							+" secondName:"+player.getSecondName().getValue()
//							+" teamId:"+player.getIdTeam()
//							+" shirt:"+player.getIdShirt().getValue()
//							+" unit:"+player.getIdUnit());					
//					actorIds.getInt().add(player.getIdPlayer());									
//				}				
////				ArrayOfStatActorEncounterInfo statsActors = service.getStatValueActorEncounter(actorIds, idEncounters);
////				for(StatActorEncounterInfo info : statsActors.getStatActorEncounterInfo()) {	
////					logger.info("StatActorEncounterInfo: "
////							+" encounterId:"+info.getIdEncounter()
////							+" actorId:"+info.getIdActor()
////							+" timePlayed: "+info.getTimePlayed().getValue());													
////				}
//								
//				ArrayOfStatValueInfo statInfo = service.getStatValueActor(actorIds, statIds, idEncounters);
//				for(StatValueInfo stat : statInfo.getStatValueInfo()) {
//					logger.info("StatValueInfo: encounterId:"+stat.getIdEncounter()
//							+" entityId:"+stat.getIdEntity()
//							+" statDefId:"+stat.getIdStatDef()
//							+" start:"+stat.getStartTime().getValue()
//							+" end:"+stat.getEndTime().getValue()
//							+" value:"+stat.getValue().getValue()
//							+" idPeriod:"+stat.getIdPeriod().getValue());
//				}		
//				
				
//				} else {
//					logger.info("Ignoring processed match: "+encounter.getIdEncounter());
//				}
			} else {
				logger.warn("Ignoring encounter for non-league game: "+encounter.getCompetitionName().getValue());
			}
		}		
		return processedGameIds;
	}
	
	/*private boolean isProcessedMatch(FeedService feedService, long leagueSeasonId, int encounterId) throws LeagueException {
		return false;
		//TODO
//		return feedService.isProcessedMatch(leagueSeasonId, encounterId);
	}*/
	
	/*private Integer getCompetitionId() {
		//TODO hard code this? Competition: 89 name:ABSA Premier Soccer League		
	ArrayOfCompetitionInfo competitions = service.getCompetition(null);
	for (CompetitionInfo info : competitions.getCompetitionInfo()) {
		logger.info("Competition: "+info.getIdCompetition()+" name:"+info.getCompetitionName().getValue());			
	}
		return 89;
	}*/
	
	private Integer getSeasonId() {
		//TODO hard code this? Season: 846 start: 2012-08-10T00:00:00		
	ArrayOfSeasonInfo seasons = service.getSeason(null);
	for(SeasonInfo info : seasons.getSeasonInfo()) {
		logger.info("Season: "+info.getIdSeason()+" start: "+info.getStartDate().toString()+" end: "+info.getEndDate().toString());			
	}
		return 846;
	}
	
	/*private List<Statistic> getStatisticsCategories() {
		Map<Integer, String> categories = new HashMap<Integer, String>();
		Map<Integer, StatSubCategInfo> subCategories = new HashMap<Integer, StatSubCategInfo>();
		
		List<String> languages = new ArrayList<String>();
		languages.add("Anglais");
		
		ArrayOfint catIds = new ArrayOfint();
		ArrayOfStatCategInfo cats = service.getStatCategs(languages);
		for(StatCategInfo cat : cats.getStatCategInfo()) {
			catIds = new ArrayOfint();
			logger.info("Category: "+cat.getIdCateg()+" "+cat.getCategName().getValue());
			catIds.getInt().add(cat.getIdCateg());
			categories.put(cat.getIdCateg(), cat.getCategName().getValue());
			//sub cats
			ArrayOfStatSubCategInfo subCats = service.getStatSubCategs(catIds, languages);
			for(StatSubCategInfo subCat : subCats.getStatSubCategInfo()) {
				logger.info("SubCat: "+subCat.getIdStatSubCateg()+" "+subCat.getStatSubCategName().getValue());				
				subCategories.put(subCat.getIdStatSubCateg(), subCat);
			}
		}		
		
		List<Statistic> statistics = new ArrayList<Statistic>();
//		Statistic statistic = null;
		ArrayOfStatInfo stats = service.getStats(null, languages);
		for(StatInfo stat : stats.getStatInfo()) {
			logger.info("Stat: idStatDef:"+stat.getIdStatDef()+" catId:"+stat.getIdStatCateg().getValue()+" name:"+stat.getStatName().getValue());
//			statistic = new Statistic();
//			StatSubCategInfo subCat = subCategories.get(stat.getIdStatCateg().getValue());			
//			statistic.setCatId(subCat.getIdStatCateg());
//			statistic.setCatName(categories.get(subCat.getIdStatCateg()));
//			statistic.setSubCatId(subCat.getIdStatSubCateg());
//			statistic.setSubCatName(subCat.getStatSubCategName().getValue());
//			statistic.setStatDefId(stat.getIdStatDef());
//			statistic.setName(stat.getStatName().getValue());
//			statistics.add(statistic);
		}
		
		return statistics;
	}*/
}
