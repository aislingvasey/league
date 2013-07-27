package com.africaapps.league.service.fixture;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.africaapps.league.dao.league.FixtureDao;
import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.league.Fixture;
import com.africaapps.league.model.league.LeagueSeason;
import com.africaapps.league.service.league.LeagueService;
import com.africaapps.league.service.transaction.ReadTransaction;
import com.africaapps.league.service.transaction.WriteTransaction;

@Service
public class FixtureServiceImpl implements FixtureService {

	@Autowired
	private FixtureDao fixtureDao;
	@Autowired
	private LeagueService leagueService;
	
	private static Logger logger = LoggerFactory.getLogger(FixtureServiceImpl.class);
	
	@ReadTransaction
	@Override
	public boolean isTradeOrSubstitutionAllowed(LeagueSeason leagueSeason, Date date) throws LeagueException {
		if (date == null) {
			date = new Date();
		}
		logger.info("Checking trades/subs for date: "+date);
		Fixture fixture = fixtureDao.getCurrent(leagueSeason.getId(), date);
		if (fixture == null) {
			fixture = fixtureDao.getClosest(leagueSeason.getId(), date);
			if (fixture != null) {
				Date lastFeedRun = leagueService.getLastFeedRun(leagueSeason.getLeague());
				if (lastFeedRun != null) {
					if (lastFeedRun.getTime() >= fixture.getEndDate().getTime()) {
						logger.info("Feed has been run after closest fixture: "+lastFeedRun+" --> "+fixture);
						return true;
					} else {
						logger.info("No feed run yet after closest fixture: "+fixture);
						return false;
					}
				} else {
					logger.info("Got closest fixture but no feed run: "+fixture);
					return false;
				}
			} else {
				logger.info("No current or closest fixture for date: "+date);
				return true;
			}
		} else {
			logger.info("There is a current fixture for date: "+date+" --> "+fixture);
			return false;
		}
	}

	@WriteTransaction
	@Override
	public void saveFixture(Fixture fixture) {
		if (fixture != null) {
			fixtureDao.saveOrUpdate(fixture);
			logger.debug("Saved fixture: "+fixture);
		}
	}
}
