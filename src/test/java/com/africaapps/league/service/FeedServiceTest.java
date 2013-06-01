package com.africaapps.league.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.africaapps.league.BaseSpringDbUnitTest;
import com.africaapps.league.service.feed.FeedService;

public class FeedServiceTest extends BaseSpringDbUnitTest {
	
	private String x = "file:///home/aisling/development/workspaces/games/league/src/test/resources/test-env.wsdl";
	private String y = "ashley.kleynhans@gmail.com";
	private String z = "Mxit_For_AmiscoSA!!";

	@Autowired
	private FeedService feedService;
	
	@Test
	public void processFeed() throws Exception {
		String leagueName = "ABSA Premier Soccer League";		
		feedService.processFeed(leagueName, x, y, z);
	}
}
