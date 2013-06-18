package com.africaapps.league.service.pool;

import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.game.Pool;
import com.africaapps.league.model.game.PoolPlayer;
import com.africaapps.league.model.league.LeagueSeason;
import com.africaapps.league.model.league.Player;

public interface PoolService {

	public Pool getPool(LeagueSeason leagueSeason) throws LeagueException;
	
	public void savePlayer(Pool pool, Player player) throws LeagueException;
	
	public PoolPlayer getPoolPlayer(long poolId, long playerId) throws LeagueException;
	
}
