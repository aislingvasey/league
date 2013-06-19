package com.africaapps.league.dao.league;

import java.util.List;

import com.africaapps.league.model.league.BlockType;
import com.africaapps.league.model.league.Player;

public interface PlayerDao {

	public void saveOrUpdate(Player player);
	
	public Player getByPlayerId(int playerId);
	
	public Long getIdByPlayerId(int playerId);
	
	public List<Player> getByTeamIdAndPlayerType(long teamId, BlockType blockType);
	
	public List<Player> getByTeamId(long teamId);
}
