package com.africaapps.league.dao.league;

import java.util.List;

import com.africaapps.league.model.league.Team;

public interface TeamDao {

	public void saveOrUpdate(Team team);
	
	public Team getBySeasonandTeamId(long leagueSeasonId, int teamId);
	
	public Long getIdBySeasonandTeamId(long leagueSeasonId, int teamId);
	
	public List<Team> getBySeasonId(long leagueSeasonId);
}
