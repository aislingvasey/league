package com.africaapps.league.service.game.team;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.africaapps.league.dao.game.UserTeamDao;
import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.game.TeamFormat;
import com.africaapps.league.model.game.UserLeague;
import com.africaapps.league.model.game.UserTeam;
import com.africaapps.league.service.game.format.TeamFormatService;
import com.africaapps.league.service.game.league.UserLeagueService;
import com.africaapps.league.service.transaction.ReadTransaction;
import com.africaapps.league.service.transaction.WriteTransaction;

@Service
public class UserTeamServiceImpl implements UserTeamService {

	@Autowired
	private UserTeamDao userTeamDao;
	@Autowired
	private TeamFormatService teamFormatService;	
	@Autowired
	private UserLeagueService userLeagueService;
	
	@ReadTransaction
	@Override
	public List<UserTeam> getTeams(long userId) throws LeagueException {
		return userTeamDao.getTeams(userId);
	}

	@WriteTransaction
	@Override
	public void saveTeam(UserTeam userTeam) throws LeagueException {
		if (userTeam != null) {
				userTeamDao.saveOrUpdate(userTeam);
		}
	}
	
	@ReadTransaction
	@Override
	public UserTeam getTeam(long userId, String teamName) throws LeagueException {		
		return userTeamDao.getTeam(userId, teamName);
	}

	@ReadTransaction
	@Override
	public TeamFormat getDefaultTeamFormat() throws LeagueException {
		return teamFormatService.getDefaultFormat();
	}

	@ReadTransaction
	@Override
	public UserLeague getDefaultUserLeague() throws LeagueException {
		return userLeagueService.getDefaultUserLeague();
	}
}
