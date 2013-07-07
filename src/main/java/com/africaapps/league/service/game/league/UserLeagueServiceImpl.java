package com.africaapps.league.service.game.league;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.africaapps.league.dao.game.UserLeagueDao;
import com.africaapps.league.dao.game.UserTeamDao;
import com.africaapps.league.dto.TeamSummary;
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
	
	private static Logger logger = LoggerFactory.getLogger(UserLeagueServiceImpl.class);
	
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
		
		//Not displaying user's team on the league page
//		get user's teams
//		userLeagueSummary.setUserTeamSummary(userTeamDao.getTeamSummary(userLeagueId, userId));
		
		//get top 10 of league's teams
		List<TeamSummary> leagueTeams = userLeagueDao.getLeagueTeamSummary(userLeagueId, 10, 0);		
		userLeagueSummary.setLeagueTeamSummary(leagueTeams);
		userLeagueSummary.setCurrentCount(userLeagueSummary.getLeagueTeamSummary().size());
		logger.info("Got leagueTeams: "+leagueTeams.size());
		
		return userLeagueSummary;
	}
}
