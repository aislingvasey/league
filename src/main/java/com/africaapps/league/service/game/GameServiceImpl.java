package com.africaapps.league.service.game;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.game.UserTeam;
import com.africaapps.league.service.game.league.UserLeagueService;
import com.africaapps.league.service.game.team.UserTeamService;

@Service
public class GameServiceImpl implements GameService {
	
	@Autowired
	private UserTeamService userTeamService;
	@Autowired
	private UserLeagueService userLeagueService;

	@Override
	public List<UserTeam> getUserTeams(long userId) throws LeagueException {
		return userTeamService.getTeams(userId);
	}
}
