package com.africaapps.league.service.game;

import java.util.List;

import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.game.UserLeague;
import com.africaapps.league.model.game.UserTeam;

public interface GameService {

	//TODO impl
	
	public List<UserTeam> getUserTeams(long userId) throws LeagueException;
	
	public List<UserLeague> getUserLeagues(long userId) throws LeagueException;
	
	public void saveUserTeam(UserTeam userTeam) throws LeagueException;
	
}
