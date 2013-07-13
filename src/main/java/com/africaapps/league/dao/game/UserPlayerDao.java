package com.africaapps.league.dao.game;

import java.util.List;

import com.africaapps.league.model.game.UserPlayer;
import com.africaapps.league.model.game.UserPlayerStatus;

public interface UserPlayerDao {

	public UserPlayer getUserPlayer(long userteamId, long playerId);
	
	public void saveOrUpdate(UserPlayer userPlayer);
	
	public UserPlayer getPlayerByStatus(long userTeamId, UserPlayerStatus status);
	
	public void delete(UserPlayer userPlayer);
	
	public List<UserPlayer> getSubstitutes(long userTeamId, int subsCount);
	
}
