package com.africaapps.league.service.statistic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.africaapps.league.dao.league.StatisticDao;
import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.league.LeagueType;
import com.africaapps.league.model.league.Statistic;
import com.africaapps.league.service.transaction.ReadTransaction;

@Service
public class StatisticServiceImpl implements StatisticService {

	@Autowired
	private StatisticDao statisticDao;
	
	@ReadTransaction
	@Override
	public List<Statistic> getStatistics(LeagueType leagueType) throws LeagueException {
		return statisticDao.get(leagueType.getId());
	}
}
