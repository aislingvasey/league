package com.africaapps.league.service.pool;

import java.util.List;

import com.africaapps.league.dto.PlayerMatchEventSummary;
import com.africaapps.league.dto.PlayerMatchSummary;
import com.africaapps.league.dto.PoolPlayersResults;
import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.game.Pool;
import com.africaapps.league.model.game.PoolPlayer;
import com.africaapps.league.model.game.PoolPlayerPointsHistory;
import com.africaapps.league.model.league.LeagueSeason;
import com.africaapps.league.model.league.Match;
import com.africaapps.league.model.league.Player;

public interface PoolService {

	public Pool getPool(LeagueSeason leagueSeason) throws LeagueException;
	
	public void savePlayer(Pool pool, Player player) throws LeagueException;
	
	public PoolPlayer getPoolPlayer(long poolId, long playerId) throws LeagueException;
	
	public PoolPlayer getPoolPlayer(long poolPlayerId) throws LeagueException;
	
	public void addPointsToPoolPlayer(PoolPlayer poolPlayer, Match match, Integer playerScore) throws LeagueException;
	
	public List<PlayerMatchSummary> getPlayerMatches(Long poolPlayerId) throws LeagueException;
	public List<PlayerMatchEventSummary> getMatchEvents(Long poolPlayerId, Long matchId) throws LeagueException;
	
	public PoolPlayersResults getPoolPlayers(Long userTeamId, int page, int pageSize) throws LeagueException;
	
	public List<PoolPlayerPointsHistory> getPoolPlayerHistory(long poolPlayerId, long currentPlayingWeekId) throws LeagueException;
	
}
