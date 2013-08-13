package com.africaapps.league.dao.league;

import java.util.List;

import com.africaapps.league.model.league.BlockType;
import com.africaapps.league.model.league.Player;

public interface PlayerDao {

	public void saveOrUpdate(Player player);
	
	public Player getPlayer(long id);
	public Player getByPlayerId(int playerId);
	public Long getIdByPlayerId(int playerId);
	public Object[] getInfoByPlayerId(int playerId);
	public Player getByTeamIdAndBlock(long teamId, BlockType block);
		
	public List<Player> getByTeamIdAndPlayerType(long teamId, BlockType blockType);
	
	public List<Player> getByTeamId(long teamId);
	public List<Player> getTeamPlayers(int teamId);
}
