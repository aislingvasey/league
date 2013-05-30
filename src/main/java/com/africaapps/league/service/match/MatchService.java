package com.africaapps.league.service.match;

import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.league.Match;
import com.africaapps.league.model.league.PlayerMatch;

public interface MatchService {

	public boolean isProcessedMatch(long leageaSeasonId, int matchId) throws LeagueException;
	
	public void saveMatch(Match match) throws LeagueException;
	
	public void savePlayerMatch(PlayerMatch playerMatch) throws LeagueException;
	
}
