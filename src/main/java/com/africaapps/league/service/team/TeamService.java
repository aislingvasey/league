package com.africaapps.league.service.team;

import java.util.List;

import com.africaapps.league.dto.TeamSummary;
import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.league.Team;

public interface TeamService {

	public void saveTeam(Team team) throws LeagueException;
	
	public Team getTeam(long leagueSeasonId, int teamId) throws LeagueException;
	
	public List<TeamSummary> getTeams(long userTeamId) throws LeagueException;
	
}
