package com.africaapps.league.service.cache;

import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.league.BlockType;
import com.africaapps.league.model.league.LeagueType;
import com.africaapps.league.model.league.Player;
import com.africaapps.league.model.league.PlayerMatch;
import com.africaapps.league.model.league.Position;
import com.africaapps.league.model.league.Statistic;

public interface CacheService {

	public void clear();
	
	public PlayerMatch getPlayerMatch(Long matchId, Long playerId) throws LeagueException;	
	public void setPlayerMatch(Long matchId, Long playerId, PlayerMatch playerMatch) throws LeagueException;
	
	public Player getPlayer(Integer playerId) throws LeagueException;
	public void setPlayer(Player player) throws LeagueException;
	
	public Position getPosition(Long leagueTypeId, Integer positionNumber) throws LeagueException;
	public void setPosition(Long leagueTypeId, Position position) throws LeagueException;	
	
	public void loadStatistics(LeagueType leagueType) throws LeagueException;
	public Statistic getStatistic(Integer id, BlockType block) throws LeagueException;
}
