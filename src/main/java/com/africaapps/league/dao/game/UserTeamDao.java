package com.africaapps.league.dao.game;

import java.util.List;

import com.africaapps.league.dto.TeamSummary;
import com.africaapps.league.model.game.UserTeam;

public interface UserTeamDao {

	public List<UserTeam> getTeams(long userId);
	
	public UserTeam getTeam(long userId, String teamName);
	
	public UserTeam getTeam(long userTeamId);
	
	public void saveOrUpdate(UserTeam userTeam);
	
	public int getTeamCount(long userLeagueId);
	
	public List<TeamSummary> getTeamSummary(long userLeagueId, long userId);
		
	public UserTeam getTeamWithPlayers(long teamId);
}
