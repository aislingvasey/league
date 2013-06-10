package com.africaapps.league.service.game.league;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.africaapps.league.dao.game.UserLeagueDao;
import com.africaapps.league.dao.game.UserTeamDao;
import com.africaapps.league.dto.UserLeagueSummary;
import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.game.UserLeague;
import com.africaapps.league.service.transaction.ReadTransaction;

@Service
public class UserLeagueServiceImpl implements UserLeagueService {

	@Autowired
	private UserLeagueDao userLeagueDao;
	@Autowired
	private UserTeamDao userTeamDao;
	
	
	@ReadTransaction
	@Override
	public UserLeague getDefaultUserLeague() throws LeagueException {
		return userLeagueDao.getDefault();
	}

	@ReadTransaction
	@Override
	public UserLeagueSummary getUserLeagueSummary(long userLeagueId, long userId) throws LeagueException {
		UserLeagueSummary userLeagueSummary = new UserLeagueSummary();
		
		userLeagueSummary.setLeagueId(userLeagueId);
		userLeagueSummary.setLeagueName(userLeagueDao.getLeagueName(userLeagueId));
		userLeagueSummary.setTeamCount(userTeamDao.getTeamCount(userLeagueId));
		
		//get user's teams
		//TODO user's team's position in league
		userLeagueSummary.setUserTeamSummary(userTeamDao.getTeamSummary(userLeagueId, userId));
		
		//get top 25 of league's teams
		//TODO getting next page of teams....
		userLeagueSummary.setLeagueTeamSummary(userLeagueDao.getLeagueTeamSummary(userLeagueId, 25, 0));
		userLeagueSummary.setCurrentCount(userLeagueSummary.getLeagueTeamSummary().size());
		
		return userLeagueSummary;
	}
}
