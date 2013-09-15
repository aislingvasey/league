package com.africaapps.league.service.pool;

import java.util.List;

import com.africaapps.league.dto.PlayerMatchStatisticSummary;
import com.africaapps.league.dto.PlayerMatchSummary;
import com.africaapps.league.dto.PoolPlayersResults;
import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.game.PlayerPrice;
import com.africaapps.league.model.game.Pool;
import com.africaapps.league.model.game.PoolPlayer;
import com.africaapps.league.model.game.PoolPlayerPointsHistory;
import com.africaapps.league.model.league.BlockType;
import com.africaapps.league.model.league.LeagueSeason;
import com.africaapps.league.model.league.Match;
import com.africaapps.league.model.league.Player;

public interface PoolService {

	public Pool getPool(LeagueSeason leagueSeason) throws LeagueException;
	
	public void savePlayer(Pool pool, Player player) throws LeagueException;
	
	public PoolPlayer getPoolPlayer(long poolId, long playerId) throws LeagueException;
	
	public PoolPlayer getPoolPlayer(long poolPlayerId) throws LeagueException;
	
	public void addPointsToPoolPlayer(PoolPlayer poolPlayer, Match match, Double playerScore) throws LeagueException;
	
	public List<PlayerMatchSummary> getPlayerMatches(Long poolPlayerId) throws LeagueException;
	public List<PlayerMatchStatisticSummary> getMatchStats(Long poolPlayerId, Long matchId) throws LeagueException;
	
	public PoolPlayersResults getPoolPlayers(Long userTeamId, int page, int pageSize) throws LeagueException;
	
	public int getPlayersByPointsOrPriceCount(long poolId, List<Long> existingPlayersId, boolean points, int page, int pageSize) throws LeagueException;
	public int getPlayersByPointsOrPriceAndTypeCount(long poolId, List<Long> existingPlayersId, BlockType playerType, boolean points, int page, int pageSize) throws LeagueException;	
	public List<PoolPlayer> getPlayersByPointsOrPrice(long poolId, List<Long> existingPlayersId, boolean points, int page, int pageSize) throws LeagueException;
	public List<PoolPlayer> getPlayersByPointsOrPriceAndType(long poolId, List<Long> existingPlayersId, BlockType playerType, boolean points, int page, int pageSize) throws LeagueException;
		
	public List<PoolPlayerPointsHistory> getPoolPlayerHistory(long poolPlayerId, long currentPlayingWeekId) throws LeagueException;
	
	public PlayerPrice getPlayerInfo(Player player) throws LeagueException;
	
}
