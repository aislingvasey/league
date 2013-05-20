package com.africaapps.league.dao.league;

import com.africaapps.league.model.league.Team;

public interface TeamDao {

	public void saveOrUpdate(Team team);
	
	public Team getById(long teamId);
	
}
