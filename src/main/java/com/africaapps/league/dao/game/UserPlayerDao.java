package com.africaapps.league.dao.game;

import com.africaapps.league.model.game.UserPlayer;

public interface UserPlayerDao {

	public UserPlayer getUserPlayer(long userteamId, long playerId);
	
	public void saveOrUpdate(UserPlayer userPlayer);
	
}
