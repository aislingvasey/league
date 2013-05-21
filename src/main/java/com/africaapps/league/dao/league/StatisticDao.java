package com.africaapps.league.dao.league;

import java.util.List;

import com.africaapps.league.model.league.Statistic;

public interface StatisticDao {

	public void saveOrUpdate(Statistic statistic);
	
	public Statistic getStatistic(long leagueTypeId, long statsId);
	
	public List<Statistic> getStatistics(long leagueTypeId);
	
}
