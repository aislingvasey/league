package com.africaapps.league.dao.game;

import com.africaapps.league.model.game.UserPlayer;
import com.africaapps.league.model.game.UserPlayerStatus;

public interface UserPlayerDao {

	public UserPlayer getUserPlayer(long userteamId, long playerId);
	
	public void saveOrUpdate(UserPlayer userPlayer);
	
	public UserPlayer getPlayerByStatus(long userTeamId, UserPlayerStatus status);
	
}
