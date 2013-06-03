package com.africaapps.league.dao.game;

import com.africaapps.league.model.game.Pool;

public interface PoolDao {

	public void saveOrUpdate(Pool pool);
	
	public Pool getByLeagueSeason(long leagueSeasonId);
	
}
