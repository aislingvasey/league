package com.africaapps.league.dao.game;

import java.util.List;

import com.africaapps.league.dto.TeamSummary;
import com.africaapps.league.model.game.UserLeague;

public interface UserLeagueDao {
	
	public void saveOrUpdate(UserLeague userLeague);
	
	public UserLeague getDefault();
	
	public String getLeagueName(long userLeagueId);
	
	public List<TeamSummary> getLeagueTeamSummary(long userLeagueId, int teamNumber, int startRow);
}
