package com.africaapps.league.dao.league;

import com.africaapps.league.model.league.Team;

public interface TeamDao {

	public void saveOrUpdate(Team team);
	
	public Team getBySeasonandTeamId(long leagueSeasonId, int teamId);
	
}
