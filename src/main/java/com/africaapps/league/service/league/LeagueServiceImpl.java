package com.africaapps.league.service.league;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.africaapps.league.dao.league.LeagueDao;
import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.league.League;

@Service
public class LeagueServiceImpl implements LeagueService {

	@Autowired
	private LeagueDao leagueDao;
	
	@Override
	public League getLeague(String leagueName) throws LeagueException {
		return leagueDao.getLeagueByName(leagueName);
	}
}
