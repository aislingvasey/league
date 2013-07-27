package com.africaapps.league.dao.league;

import java.util.Date;

import com.africaapps.league.model.league.Fixture;

public interface FixtureDao {

	public Fixture getCurrent(long leagueSeasonId, Date date);
	
	public Fixture getClosest(long leagueSeasonId, Date date);
	
	public void saveOrUpdate(Fixture fixture);
	
}
