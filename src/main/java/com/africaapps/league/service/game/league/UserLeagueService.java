package com.africaapps.league.service.game.league;

import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.game.UserLeague;

public interface UserLeagueService {

	public UserLeague getDefaultUserLeague() throws LeagueException;
	
}
