package com.africaapps.league.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.africaapps.league.BaseSpringDbUnitTest;
import com.africaapps.league.service.feed.FeedService;

public class FeedServiceTest extends BaseSpringDbUnitTest {
	
	private String url = "file:///home/aisling/development/workspaces/games/league/src/test/resources/test-env.wsdl";
	private String username = "";
	private String password = "";

	@Autowired
	private FeedService feedService;
	
	@Test
	public void processFeed() throws Exception {
		String leagueName = "ABSA Premier Soccer League";		
		feedService.processFeed(leagueName, url, username, password);
	}
}
