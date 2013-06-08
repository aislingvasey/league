package com.africaapps.league.service.game.league;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.africaapps.league.dao.game.UserLeagueDao;
import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.game.UserLeague;
import com.africaapps.league.service.transaction.ReadTransaction;

@Service
public class UserLeagueServiceImpl implements UserLeagueService {

	@Autowired
	private UserLeagueDao userLeagueDao;

	@ReadTransaction
	@Override
	public UserLeague getDefaultUserLeague() throws LeagueException {
		return userLeagueDao.getDefault();
	}
}
