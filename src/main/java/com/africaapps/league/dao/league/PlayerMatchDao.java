package com.africaapps.league.dao.league;

import com.africaapps.league.model.league.PlayerMatch;

public interface PlayerMatchDao {

	public void saveOrUpdate(PlayerMatch playerMatch);
	
	public PlayerMatch getByIds(long matchId, long playerId);
	
	public Long getIdByIds(long matchId, long playerId);
}
