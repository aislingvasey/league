package com.africaapps.league.service.feed;

import java.util.Date;

public interface MatchFilter {

	public boolean isValidMatch(Integer matchId, Date matchDateTime);
	
}
