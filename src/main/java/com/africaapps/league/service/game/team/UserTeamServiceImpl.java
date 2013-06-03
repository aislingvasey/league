package com.africaapps.league.service.game.team;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.africaapps.league.dao.game.UserTeamDao;
import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.game.UserTeam;

@Service
public class UserTeamServiceImpl implements UserTeamService {

	@Autowired
	private UserTeamDao userTeamDao;
	
	@Override
	public List<UserTeam> getTeams(long userId) throws LeagueException {
		return userTeamDao.getTeams(userId);
	}

	@Override
	public void saveTeam(UserTeam userTeam) throws LeagueException {
		if (userTeam != null) {
			userTeamDao.saveOrUpdate(userTeam);
		}
	}
}
