package com.africaapps.league.service.feed;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.league.League;
import com.africaapps.league.model.league.Match;
import com.africaapps.league.service.league.LeagueService;
import com.africaapps.league.service.webservice.WebServiceClient;

@Service
public class FeedServiceImpl implements FeedService {

	@Autowired
	private LeagueService leagueService;
	@Autowired
	private FeedParsingService feedParsingService;
	
	private static Logger logger = LoggerFactory.getLogger(FeedServiceImpl.class);
	
	@Override
	public void processFeed(String leagueName, String wsdlUrl, String username, String password) throws LeagueException {
		League league = getLeague(leagueName);
		WebServiceClient webServiceClient = setupWebServiceClient(wsdlUrl, username, password);
		List<Match> matches = feedParsingService.parseMatchFilActionStruct(webServiceClient.getAvailableFilMatches());
		for(Match match : matches) {
			processMatch(league, match);
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
	
	protected WebServiceClient setupWebServiceClient(String wsdlUrl, String username, String password) throws LeagueException {
		WebServiceClient client = new WebServiceClient(wsdlUrl, username, password);
		if (!client.isServiceReady()) {
			throw new LeagueException("Web service is not ready!");
		} else {
			return client;
		}
	}
	
	protected void processMatch(League league, Match match) throws LeagueException {
		//TODO
	}
}
