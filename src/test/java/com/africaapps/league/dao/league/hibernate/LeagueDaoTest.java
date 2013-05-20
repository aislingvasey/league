package com.africaapps.league.dao.league.hibernate;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.africaapps.league.BaseSpringDbUnitTest;
import com.africaapps.league.dao.league.LeagueDao;
import com.africaapps.league.dao.league.LeagueTypeDao;
import com.africaapps.league.model.league.League;

public class LeagueDaoTest extends BaseSpringDbUnitTest {

	@Autowired
	private LeagueDao leagueDao;
	@Autowired
	private LeagueTypeDao leagueTypeDao;
	
	private static Logger logger = LoggerFactory.getLogger(LeagueDaoTest.class);
	
	@Transactional(readOnly=false)
	@Test
	public void saveAndGetByName() throws Exception {
		assertNotNull(leagueDao);
		
		String name = "Absa Soccer League";
		League league = leagueDao.getLeagueByName(name);
		assertNull(league);
		
		league = new League();
		league.setName(name);
		league.setDescription("SA's soccer league");
		league.setLeagueType(leagueTypeDao.getLeagueTypeByName("Soccer"));		
		leagueDao.saveOrUpdate(league);
		
		League saved = leagueDao.getLeagueByName(name);
		logger.debug("Got league: "+saved);
		assertNotNull(saved);
		assertNotNull(saved.getId());
		assertEquals(name, saved.getName());
		assertEquals("Soccer", saved.getLeagueType().getName());		
	}	
}
