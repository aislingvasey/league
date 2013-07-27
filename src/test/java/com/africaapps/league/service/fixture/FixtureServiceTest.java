package com.africaapps.league.service.fixture;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.africaapps.league.BaseSpringDbUnitTest;
import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.league.Fixture;
import com.africaapps.league.model.league.League;
import com.africaapps.league.model.league.LeagueSeason;
import com.africaapps.league.service.league.LeagueService;

public class FixtureServiceTest extends BaseSpringDbUnitTest {

	@Autowired
	private FixtureService fixtureService;
	@Autowired
	private LeagueService leagueService;

	@Test
	public void isTradeOrSubstitutionAllowed() throws Exception {
		League league = leagueService.getLeague("ABSA Premier Soccer League");
		assertEquals(Long.valueOf(1), league.getId());
		LeagueSeason leagueSeason = leagueService.getCurrentSeason(league);
		assertEquals(Long.valueOf(1), leagueSeason.getId());
		Date date = new Date();

		// No fixtures
		assertTrue(fixtureService.isTradeOrSubstitutionAllowed(leagueSeason, date));

		// Current fixture
		Fixture fixture = saveFixtureForNow(leagueSeason, date);
		assertFalse(fixtureService.isTradeOrSubstitutionAllowed(leagueSeason, date));

		// Closest Fixture
		saveClosestFixture(fixture);
		assertFalse(fixtureService.isTradeOrSubstitutionAllowed(leagueSeason, date));

		// With a set feed date before the match end
		Date feedRun = savePastFeedDate(league, fixture.getEndDate());
		Calendar feed = Calendar.getInstance();
		feed.setTime(feedRun);
		feed.add(Calendar.MINUTE, 11); // after end of match and after the last time the feed has run
		date = feed.getTime();
		assertFalse(fixtureService.isTradeOrSubstitutionAllowed(leagueSeason, date));

		// No feed data
		resetFeedDate(league);
		feed = Calendar.getInstance();
		feed.setTime(feedRun);
		feed.add(Calendar.MINUTE, 11); // after end of match and no feed date
		date = feed.getTime();
		assertFalse(fixtureService.isTradeOrSubstitutionAllowed(leagueSeason, date));

		// With a set feed date after the match end
		feedRun = saveFutureFeedDate(league, fixture.getEndDate());
		feed = Calendar.getInstance();
		feed.setTime(feedRun);
		feed.add(Calendar.MINUTE, 10); // after match is over and feed has run
		date = feed.getTime();
		assertTrue(fixtureService.isTradeOrSubstitutionAllowed(leagueSeason, date));
	}

	private Fixture saveFixtureForNow(LeagueSeason leagueSeason, Date date) {
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		now.add(Calendar.MINUTE, -15);
		Date start = now.getTime();
		now.add(Calendar.HOUR_OF_DAY, 2);
		Date end = now.getTime();
		Fixture fixture = new Fixture();
		fixture.setLeagueSeason(leagueSeason);
		fixture.setStartDate(start);
		fixture.setEndDate(end);
		fixtureService.saveFixture(fixture);

		// Save some other past fixtures too on other days
		Calendar past = Calendar.getInstance();
		past.setTime(now.getTime());
		for (int i = 0; i < 5; i++) {
			past.add(Calendar.DAY_OF_YEAR, -i);
			past.set(Calendar.HOUR_OF_DAY, i);
			Date s = past.getTime();
			past.add(Calendar.HOUR_OF_DAY, 2);
			Date e = past.getTime();
			Fixture f = new Fixture();
			f.setLeagueSeason(leagueSeason);
			f.setStartDate(s);
			f.setEndDate(e);
			fixtureService.saveFixture(f);
		}

		// Save some other future fixtures too on other days
		Calendar future = Calendar.getInstance();
		future.setTime(now.getTime());
		for (int i = 1; i <= 5; i++) {
			future.add(Calendar.DAY_OF_YEAR, i);
			future.set(Calendar.HOUR_OF_DAY, i);
			Date s = future.getTime();
			future.add(Calendar.HOUR_OF_DAY, 2);
			Date e = future.getTime();
			Fixture f = new Fixture();
			f.setLeagueSeason(leagueSeason);
			f.setStartDate(s);
			f.setEndDate(e);
			fixtureService.saveFixture(f);
		}

		return fixture;
	}

	private void saveClosestFixture(Fixture fixture) {
		Calendar now = Calendar.getInstance();
		now.setTime(fixture.getStartDate());
		now.add(Calendar.HOUR_OF_DAY, -1);
		Date end = now.getTime();
		now.add(Calendar.HOUR_OF_DAY, -2);
		Date start = now.getTime();
		fixture.setStartDate(start);
		fixture.setEndDate(end);
		fixtureService.saveFixture(fixture);
	}

	private Date savePastFeedDate(League league, Date endMatch) throws LeagueException {
		Calendar now = Calendar.getInstance();
		now.setTime(endMatch);
		now.add(Calendar.MINUTE, -10);
		leagueService.setLastFeedRun(league, now.getTime());
		return now.getTime();
	}

	private Date saveFutureFeedDate(League league, Date endMatch) throws LeagueException {
		Calendar now = Calendar.getInstance();
		now.setTime(endMatch);
		now.add(Calendar.MINUTE, 5);
		leagueService.setLastFeedRun(league, now.getTime());
		return now.getTime();
	}

	private void resetFeedDate(League league) throws LeagueException {
		leagueService.setLastFeedRun(league, null);
	}
}
