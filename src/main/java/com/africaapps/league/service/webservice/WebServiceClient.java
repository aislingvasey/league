package com.africaapps.league.service.webservice;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.ws.BindingProvider;

import org.datacontract.schemas._2004._07.livemediastructs.ArrayOfMatchFilActionLightStruct;
import org.datacontract.schemas._2004._07.livemediastructs.ArrayOfMatchLightStruct;
import org.datacontract.schemas._2004._07.livemediastructs.MatchFilActionLightStruct;
import org.datacontract.schemas._2004._07.livemediastructs.MatchFilActionStruct;
import org.datacontract.schemas._2004._07.livemediastructs.MatchLightStruct;
import org.datacontract.schemas._2004._07.livemediastructs.MatchStruct;
import org.datacontract.schemas._2004._07.livemediastructs.TeamStruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tempuri.IServiceAmiscoLive;
import org.tempuri.ServiceAmiscoLive;

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
			logger.error("Error with URL:" + e.getMessage());
		} catch (Throwable t) {
			logger.error("Caught exception:", t);
		}
	}

	public void getFirstAvailableFilMatch(IServiceAmiscoLive service1) {
		ArrayOfKeyValueOfintstring intStrings = service1.getMatchFilActionStructDetailAvailable();
		if (intStrings.getKeyValueOfintstring().size() > 0) {
			// for (ArrayOfKeyValueOfintstring.KeyValueOfintstring entry : intStrings.getKeyValueOfintstring()) {
			ArrayOfKeyValueOfintstring.KeyValueOfintstring entry = intStrings.getKeyValueOfintstring().get(0);
			logger.info("FilMatchDetailAvailable: " + entry.getKey() + "=" + entry.getValue());
			ArrayOfMatchFilActionLightStruct matchStructs = service1.matchFilActionLightStructList(entry.getKey());
			logger.info("Got " + matchStructs.getMatchFilActionLightStruct().size() + " Fil (light) matches");
			if (matchStructs.getMatchFilActionLightStruct().size() > 0) {
				// for (MatchFilActionLightStruct matchLightStruct : matchStructs.getMatchFilActionLightStruct()) {
				MatchFilActionLightStruct matchLightStruct = matchStructs.getMatchFilActionLightStruct().get(0);
				MatchFilActionStruct matchStruct = service1.getMatchFilActionStruct(matchLightStruct.getIdMatch(),
						entry.getKey());
				if (matchStruct != null) {
					DataLogUtil.logMatchFilStruct(matchStruct);

					// 1st Team
					// TeamStruct teamStruct =
					// matchStruct.getLstTeamStruct().getValue().getTeamStruct().get(0);
					// TeamStruct retrievedTeam =
					// service1.getTeamStruct(matchStruct.getIdMatch(),
					// teamStruct.getIdTeam(), entry.getKey());
					// StringBuilder sb = new StringBuilder();
					// DataLogUtil.logTeamStruct(sb, retrievedTeam);
					// logger.info("Team: "+sb.toString());

				} else {
					logger.info("No match struct for matchLightStruct: " + matchLightStruct.getIdMatch());
				}
			}
		} else {
			logger.info("No matches available");
		}
	}

	public void getFirstAvailableMatch(IServiceAmiscoLive service1) {
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
				// DataLogUtil.logMatchLightStruct(matchLightStruct);
				MatchStruct matchStruct = service1.getMatchStruct(matchLightStruct.getIdMatch(), entry.getKey());
				if (matchStruct != null) {
					DataLogUtil.logMatchStruct(matchStruct);

					// 1st Team
					TeamStruct teamStruct = matchStruct.getLstTeamStruct().getValue().getTeamStruct().get(0);
					TeamStruct retrievedTeam = service1.getTeamStruct(matchStruct.getIdMatch(), teamStruct.getIdTeam(),
							entry.getKey());
					StringBuilder sb = new StringBuilder();
					DataLogUtil.logTeamStruct(sb, retrievedTeam);
					logger.info("Team: " + sb.toString());

				} else {
					logger.info("No match struct for matchLightStruct: " + matchLightStruct.getIdMatch());
				}
			}
		}
	}

/*	private void getAvailableRanking(IServiceAmiscoLive service1) {
		ArrayOfKeyValueOfintstring intStrings = service1.getDayRankingStructDetailAvailable();
		for (ArrayOfKeyValueOfintstring.KeyValueOfintstring keyValue : intStrings.getKeyValueOfintstring()) {
			logger.info("DayRanking: " + keyValue.getKey() + "=" + keyValue.getValue());
			ArrayOfDayRankingStruct struts = service1.dayRankingStructList(keyValue.getKey());
			for (DayRankingStruct dayRanking : struts.getDayRankingStruct()) {
				DataLogUtil.logDayRankingStruct(dayRanking);
			}
		}
	}

	private void getAvailableScoring(IServiceAmiscoLive service1) {
		ArrayOfKeyValueOfintstring intStrings = service1.getDayScoringStructDetailAvailable();
		for (ArrayOfKeyValueOfintstring.KeyValueOfintstring keyValue : intStrings.getKeyValueOfintstring()) {
			System.out.println("DayScoring: " + keyValue.getKey() + "=" + keyValue.getValue());
			ArrayOfDayScoringStruct struts = service1.dayScoringStructList(keyValue.getKey());
			logger.info("Got dayScoringStructs: " + struts.getDayScoringStruct().size());
			for (DayScoringStruct dayScoring : struts.getDayScoringStruct()) {
				DataLogUtil.logDayScoringStruct(dayScoring);
			}
		}
	}*/

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
}
