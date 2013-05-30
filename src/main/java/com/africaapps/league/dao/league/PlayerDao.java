package com.africaapps.league.dao.league;

import com.africaapps.league.model.league.Player;

public interface PlayerDao {

	public void saveOrUpdate(Player player);
	
	public Player getByPlayerId(int playerId);
	
	public Long getIdByPlayerId(int playerId);
	
}
