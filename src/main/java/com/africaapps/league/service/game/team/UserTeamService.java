package com.africaapps.league.service.game.team;

import java.util.List;

import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.game.UserTeam;

public interface UserTeamService {

	public List<UserTeam> getTeams(long userId) throws LeagueException;
	
	public void saveTeam(UserTeam userTeam) throws LeagueException;
	
}
