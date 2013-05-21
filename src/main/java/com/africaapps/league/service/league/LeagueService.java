package com.africaapps.league.service.league;

import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.league.League;

public interface LeagueService {

	public League getLeague(String leagueName) throws LeagueException;
	
}
