package com.africaapps.league.dao.game;

import java.util.List;

import com.africaapps.league.model.game.UserTeam;

public interface UserTeamDao {

	public List<UserTeam> getTeams(long userId);
	
	public void saveOrUpdate(UserTeam userTeam);
}
