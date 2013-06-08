package com.africaapps.league.dao.game;

import com.africaapps.league.model.game.UserLeague;

public interface UserLeagueDao {
	
	public void saveOrUpdate(UserLeague userLeague);
	
	public UserLeague getDefault();
}
