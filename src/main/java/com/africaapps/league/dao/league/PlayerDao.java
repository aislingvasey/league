package com.africaapps.league.dao.league;

import com.africaapps.league.model.league.Player;

public interface PlayerDao {

	public void saveOrUpdate(Player player);
	
	public Player getById(long playerId);
	
}
