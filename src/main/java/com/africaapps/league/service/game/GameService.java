package com.africaapps.league.service.game;

import java.util.List;

import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.game.UserTeam;

public interface GameService {
	
	public List<UserTeam> getUserTeams(long userId) throws LeagueException;
		
	public void saveUserTeam(UserTeam userTeam) throws LeagueException;
	
}
