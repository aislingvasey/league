package com.africaapps.league.dao.game;

import com.africaapps.league.model.game.PoolPlayer;

public interface PoolPlayerDao {

	public void saveOrUpdate(PoolPlayer poolPlayer);
	
	public PoolPlayer getByPoolAndPlayer(long poolId, long playerId);
	
	public PoolPlayer get(long poolPlayerId);
	
	public void addPlayerScore(long poolPlayerId, long matchId, Integer playerScore);
	
}
