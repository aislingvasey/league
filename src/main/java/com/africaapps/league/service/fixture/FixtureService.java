package com.africaapps.league.service.fixture;

import java.util.Date;

import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.league.Fixture;
import com.africaapps.league.model.league.LeagueSeason;

public interface FixtureService {

	public boolean isTradeOrSubstitutionAllowed(LeagueSeason leagueSeason, Date date) throws LeagueException;
	
	public void saveFixture(Fixture fixture);
}
