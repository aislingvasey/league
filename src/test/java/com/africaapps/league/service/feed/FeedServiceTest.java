package com.africaapps.league.service.feed;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.africaapps.league.BaseSpringDbUnitTest;
import com.africaapps.league.model.league.League;
import com.africaapps.league.service.league.LeagueService;

public class FeedServiceTest extends BaseSpringDbUnitTest {
	
	private String x = "file:///home/aisling/development/workspaces/games/league/src/test/resources/new-env.wsdl";
	private String y = "ashley.kleynhans@gmail.com";
	private String z = "Mxit_For_AmiscoSA!!";

	@Autowired
	private FeedService feedService;
	@Autowired
	private LeagueService leagueService;
	
	@Test
	public void placeHolderMethod() {
		
	}
	
//	@Test
//	public void processFeed() throws Exception {
//		String leagueName = "ABSA Premier Soccer League";		
//		League league = leagueService.getLeague(leagueName);
//		feedService.processFeed(league, x, y, z, null);
//	}
}
