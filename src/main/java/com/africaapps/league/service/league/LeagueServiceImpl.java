package com.africaapps.league.service.league;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.africaapps.league.dao.league.LeagueDao;
import com.africaapps.league.dao.league.LeagueSeasonDao;
import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.league.League;
import com.africaapps.league.model.league.LeagueSeason;
import com.africaapps.league.service.transaction.ReadTransaction;

@Service
public class LeagueServiceImpl implements LeagueService {

	@Autowired
	private LeagueDao leagueDao;
	@Autowired
	private LeagueSeasonDao leagueSeasonDao;

//	private static Logger logger = LoggerFactory.getLogger(LeagueServiceImpl.class);
	
	@ReadTransaction
	@Override
	public League getLeague(String leagueName) throws LeagueException {
		return leagueDao.getLeagueByName(leagueName);
	}

	@ReadTransaction
	@Override
	public LeagueSeason getCurrentSeason(League league) throws LeagueException {
		if (league != null) {
			return leagueSeasonDao.getCurrentSeason(league.getId());
		}
		return null;
	}
}
