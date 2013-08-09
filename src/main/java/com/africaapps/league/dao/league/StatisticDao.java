package com.africaapps.league.dao.league;

import java.util.List;

import com.africaapps.league.model.league.BlockType;
import com.africaapps.league.model.league.Statistic;

public interface StatisticDao {

	public List<Statistic> get(long leagueTypeId);
	
	public Statistic get(long leagueTypeId, int externalId, BlockType block);
	
}
