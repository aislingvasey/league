package com.africaapps.league.dao.league;

import com.africaapps.league.model.league.Match;

public interface MatchDao {

	public void saveOrUpdate(Match match);
	
	public Match getById(long matchId);
	
}
