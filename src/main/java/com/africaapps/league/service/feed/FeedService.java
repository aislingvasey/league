package com.africaapps.league.service.feed;

import com.africaapps.league.exception.LeagueException;

public interface FeedService {

	public void processFeed(String leagueName, String wsdlUrl, String username, String password) throws LeagueException;
	
}
