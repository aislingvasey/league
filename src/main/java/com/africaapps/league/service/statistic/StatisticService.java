package com.africaapps.league.service.statistic;

import java.util.List;

import com.africaapps.league.model.league.Statistic;
import com.africaapps.league.model.league.LeagueType;

import com.africaapps.league.exception.LeagueException;

public interface StatisticService {

	public List<Statistic> getStatistics(LeagueType leagueType) throws LeagueException;
	
}
