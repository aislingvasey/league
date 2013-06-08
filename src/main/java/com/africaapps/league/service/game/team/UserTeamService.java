package com.africaapps.league.service.game.team;

import java.util.List;

import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.game.TeamFormat;
import com.africaapps.league.model.game.UserLeague;
import com.africaapps.league.model.game.UserTeam;

public interface UserTeamService {

	public List<UserTeam> getTeams(long userId) throws LeagueException;
	
	public void saveTeam(UserTeam userTeam) throws LeagueException;

	public UserTeam getTeam(long userId, String teamName) throws LeagueException;
	
	public UserLeague getDefaultUserLeague() throws LeagueException;
	
	public TeamFormat getDefaultTeamFormat() throws LeagueException;
	
}
