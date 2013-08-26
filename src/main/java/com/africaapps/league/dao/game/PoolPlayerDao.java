package com.africaapps.league.dao.game;

import java.util.List;

import com.africaapps.league.model.game.PoolPlayer;
import com.africaapps.league.model.league.BlockType;

public interface PoolPlayerDao {

	public void saveOrUpdate(PoolPlayer poolPlayer);
	
	public PoolPlayer getByPoolAndPlayer(long poolId, long playerId);
	
	public PoolPlayer get(long poolPlayerId);
	
	public void addPlayerScore(long poolPlayerId, long matchId, Double playerScore);
	
	public List<PoolPlayer> getByPoolId(long poolId, int page, int pageSize);
	
	public int getPlayersByPointsOrPriceCount(long poolId, List<Long> existingPlayersId, boolean points, int page, int pageSize);
	
	public List<PoolPlayer> getPlayersByPointsOrPrice(long poolId, List<Long> existingPlayersId, boolean points, int page, int pageSize);
	
	public int getPlayersByPointsOrPriceAndTypeCount(long poolId, List<Long> existingPlayersId, BlockType playerType, boolean points, int page, int pageSize);
	
	public List<PoolPlayer> getPlayersByPointsOrPriceAndType(long poolId, List<Long> existingPlayersId, BlockType playerType, boolean points, int page, int pageSize);
	
}
