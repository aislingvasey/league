package com.africaapps.league.dao.league;

import com.africaapps.league.model.league.League;

public interface LeagueDao {

	public void saveOrUpdate(League league);
	
	public League getLeagueByName(String name);
	
}
