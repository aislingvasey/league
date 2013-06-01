package com.africaapps.league.dao.league;

import com.africaapps.league.model.league.Match;

public interface MatchDao {

	public void saveOrUpdate(Match match);
	
	public Match getByLeagueSeasonAndMatchId(long leagueSeasonId, int matchId);
	
	public Long getIdByMatchId(long leagueSeasonId, int matchId);
	
	public void calculatePlayerScores(long Id);
}
