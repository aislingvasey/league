package com.africaapps.league.dao.league;

import java.util.List;

import com.africaapps.league.model.league.LeagueType;

public interface LeagueTypeDao {

	public void saveOrUpdate(LeagueType leagueType);
	
	public List<LeagueType> getAll();
	
	public LeagueType getLeagueTypeByName(String name);
	
}
