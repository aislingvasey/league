package com.africaapps.league.dao.game;

import java.util.List;

import com.africaapps.league.model.game.UserTeam;

public interface UserTeamDao {

	public List<UserTeam> getTeams(long userId);
	
	public UserTeam getTeam(long userId, String teamName);
	
	public void saveOrUpdate(UserTeam userTeam);
}
