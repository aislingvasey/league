package com.africaapps.league.dao.league;

import com.africaapps.league.model.league.PlayerMatchStatistic;

public interface PlayerMatchStatisticDao {

	public void saveOrUpdate(PlayerMatchStatistic pms);
	
	public PlayerMatchStatistic get(long playerMatchId, long statId);
	
}
