package com.africaapps.league.dao.game;

import java.util.List;

import com.africaapps.league.model.game.PoolPlayerPointsHistory;

public interface PoolPlayerPointsHistoryDao {

	public void save(PoolPlayerPointsHistory history);
	
	public List<PoolPlayerPointsHistory> getHistoryForPlayer(long poolPlayerId);
	
}
