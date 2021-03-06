package com.africaapps.league.dao.game;

import java.util.List;

import com.africaapps.league.dto.PlayerMatchStatisticSummary;
import com.africaapps.league.dto.PlayerMatchSummary;
import com.africaapps.league.model.game.PoolPlayerPointsHistory;

public interface PoolPlayerPointsHistoryDao {

	public void save(PoolPlayerPointsHistory history);
	
	public List<PlayerMatchSummary> getHistoryForPlayer(long poolPlayerId);
	public List<PlayerMatchStatisticSummary> getStatsForPlayer(long poolPlayerId, long matchId);
	
	public List<PoolPlayerPointsHistory> getForPlayingWeek(long poolPlayerId, long currentPlayingWeekId);
}
