package com.africaapps.league.dao.league.hibernate;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.africaapps.league.BaseSpringDbUnitTest;
import com.africaapps.league.dao.league.EventDao;
import com.africaapps.league.dao.league.LeagueTypeDao;
import com.africaapps.league.model.league.BlockType;
import com.africaapps.league.model.league.Event;
import com.africaapps.league.model.league.LeagueType;
import com.africaapps.league.service.transaction.WriteTransaction;

public class EventDaoTest extends BaseSpringDbUnitTest {

	@Autowired
	private EventDao statsDao;
	@Autowired
	private LeagueTypeDao leagueTypeDao;
	
	private static Logger logger = LoggerFactory.getLogger(EventDaoTest.class);
	
	@WriteTransaction
	@Test
	public void saveUpdateAndGet() throws Exception {
		assertNotNull(statsDao);
		assertNotNull(leagueTypeDao);
		
		long leagueTypeId = -1;
		int existingStatsId = 100;
		
		List<Event> s = statsDao.getEvents(leagueTypeId);
		assertEquals(3, s.size());
		assertTrue(s.get(0).getId().longValue() == -1 || s.get(0).getId().longValue() == -2 || s.get(0).getId().longValue() == -4);		
		logger.debug("Got 1:"+s.get(0));
		logger.debug("Got 2:"+s.get(1));
		logger.debug("Got 3:"+s.get(2));
		
		
		Event stats = statsDao.getEvent(leagueTypeId, 101, BlockType.GOALKEEPER);
		assertNotNull(stats);
		assertEquals(-4, stats.getId().longValue());
		
		stats = statsDao.getEvent(leagueTypeId, 101, null);
		assertNotNull(stats);
		assertEquals(-2, stats.getId().longValue());
		
		stats = statsDao.getEvent(leagueTypeId, existingStatsId, null);
		assertNotNull(stats);		
		logger.debug("Got: "+stats);
		assertEquals("Yellow Card", stats.getDescription());
		assertEquals(-5, stats.getPoints().intValue());
		assertEquals(100, stats.getEventId().intValue());
		assertEquals(-1, stats.getId().longValue());
		assertEquals(-1, stats.getLeagueType().getId().longValue());		
		
		stats = statsDao.getEvent(leagueTypeId, 102, null);
		assertNull(stats);
		
		LeagueType leagueType = leagueTypeDao.getLeagueTypeByName("Soccer");
		stats = new Event();
		stats.setDescription("Goal");
		stats.setLeagueType(leagueType);
		stats.setPoints(20);
		stats.setEventId(102);
		statsDao.saveOrUpdate(stats);		
		
		s = statsDao.getEvents(leagueTypeId);
		assertEquals(4, s.size());
		for(Event ss : s) {
			if (ss.getId().longValue() == -1) {
				
			} else if (ss.getId().longValue() == -2) {
				
			} else if (ss.getEventId().equals(102)) {
				logger.debug("Got: "+ss);
				assertEquals("Goal", ss.getDescription());
				assertEquals(20, ss.getPoints().intValue());
				assertEquals(102, ss.getEventId().intValue());
				assertNotNull(ss.getId().longValue());
				assertEquals(leagueType.getId(), ss.getLeagueType().getId());
			} else if (ss.getId().longValue() == -4) {
				logger.debug("Got: "+stats);
				assertEquals("Goalkeeper Red Card", ss.getDescription());
				assertEquals(-50, ss.getPoints().intValue());
				assertEquals(101, ss.getEventId().intValue());
				assertNotNull(ss.getId().longValue());
				assertEquals(leagueType.getId(), ss.getLeagueType().getId());
			} else {
				fail("Meh - unexpected event: "+ss);
			}
		}
	}
}
