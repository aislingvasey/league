package com.africaapps.league.service.league;

import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.league.League;
import com.africaapps.league.model.league.LeagueSeason;

public interface LeagueService {

	public League getLeague(String leagueName) throws LeagueException;
	
	public LeagueSeason getCurrentSeason(League league) throws LeagueException;
		
}
