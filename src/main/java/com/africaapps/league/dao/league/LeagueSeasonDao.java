package com.africaapps.league.dao.league;

import com.africaapps.league.model.league.LeagueSeason;

public interface LeagueSeasonDao {

	public void saveOrUpdate(LeagueSeason leagueSeason);
	
	public LeagueSeason getCurrentSeason(long leagueId);
	
	public LeagueSeason getCurrentSeasonForUserTeam(long userTeamId);
	
}
