package com.africaapps.league.dao.league.hibernate;

import java.text.SimpleDateFormat;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.africaapps.league.BaseSpringDbUnitTest;
import com.africaapps.league.dao.league.LeagueDao;
import com.africaapps.league.dao.league.LeagueSeasonDao;
import com.africaapps.league.model.league.League;
import com.africaapps.league.model.league.LeagueSeason;
import com.africaapps.league.model.league.LeagueSeasonStatus;

public class LeagueSeasonDaoTest extends BaseSpringDbUnitTest {

	@Autowired
	private LeagueSeasonDao leagueSeasonDao;
	@Autowired
	private LeagueDao leagueDao;
	
private static Logger logger = LoggerFactory.getLogger(LeagueDaoTest.class);
	
	@Transactional(readOnly=false)
	@Test
	public void saveAndGetCurrentSeason() throws Exception {
		assertNotNull(leagueSeasonDao);
		
		String name = "ABSA Soccer League";
		League league = leagueDao.getLeagueByName(name);
		assertNotNull(league);
		
		LeagueSeason season = leagueSeasonDao.getCurrentSeason(league.getId());
		assertNull(season);
		
		LeagueSeason s = new LeagueSeason();
		s.setName("2012-2013 Season");
		s.setStatus(LeagueSeasonStatus.COMPLETE);
		s.setLeague(league);		
		s.setStartDate(new SimpleDateFormat("yyyy-MM-dd").parse("2012-07-15"));	
		s.setEndDate(new SimpleDateFormat("yyyy-MM-dd").parse("2013-05-01"));	
		leagueSeasonDao.saveOrUpdate(s);
		
		season = leagueSeasonDao.getCurrentSeason(league.getId());
		assertNull(season);
		
		s = new LeagueSeason();
		s.setName("2013-2014 Season");
		s.setStatus(LeagueSeasonStatus.CURRENT);
		s.setLeague(league);		
		s.setStartDate(new SimpleDateFormat("yyyy-MM-dd").parse("2013-07-15"));	
		s.setEndDate(new SimpleDateFormat("yyyy-MM-dd").parse("2014-05-01"));
		leagueSeasonDao.saveOrUpdate(s);
		
		season = leagueSeasonDao.getCurrentSeason(league.getId());
		assertNotNull(season);
		assertEquals(season.getName(), s.getName());	
		logger.debug("Got current season: "+s);
	}	
}
