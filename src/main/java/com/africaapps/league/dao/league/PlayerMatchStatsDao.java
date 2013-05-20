package com.africaapps.league.dao.league;

import java.util.List;

import com.africaapps.league.model.league.PlayerMatchStats;

public interface PlayerMatchStatsDao {

	public void saveOrUpdate(PlayerMatchStats playerMatchStats);
	
	public Integer getTotalPoints(long PlayerMatchId);

	public List<PlayerMatchStats> getStats(long playerMatchId);
	
}
