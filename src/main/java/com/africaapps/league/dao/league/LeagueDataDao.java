package com.africaapps.league.dao.league;

import com.africaapps.league.model.league.LeagueData;

public interface LeagueDataDao {

	public LeagueData get(long leagueId);
	
	public void saveOrUpdate(LeagueData leagueData);
	
}
