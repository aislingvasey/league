package com.africaapps.league.dao.league.hibernate;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.africaapps.league.BaseSpringDbUnitTest;
import com.africaapps.league.dao.league.LeagueTypeDao;
import com.africaapps.league.dao.league.StatisticDao;
import com.africaapps.league.model.league.LeagueType;
import com.africaapps.league.model.league.Statistic;

public class StatisticDaoTest extends BaseSpringDbUnitTest {

	@Autowired
	private StatisticDao statsDao;
	@Autowired
	private LeagueTypeDao leagueTypeDao;
	
	private static Logger logger = LoggerFactory.getLogger(StatisticDaoTest.class);
	
	@Transactional(readOnly=false)
	@Test
	public void saveUpdateAndGet() throws Exception {
		assertNotNull(statsDao);
		assertNotNull(leagueTypeDao);
		
		long leagueTypeId = -1;
		int existingStatsId = 100;
		
		List<Statistic> s = statsDao.getStatistics(leagueTypeId);
		assertEquals(2, s.size());
		assertTrue(s.get(0).getId().longValue() == -1 || s.get(1).getId().longValue() == -2);		
		logger.debug("Got 1:"+s.get(0));
		logger.debug("Got 2:"+s.get(1));
		
		Statistic stats = statsDao.getStatistic(leagueTypeId, existingStatsId);
		assertNotNull(stats);		
		logger.debug("Got: "+stats);
		assertEquals("Yellow Card", stats.getDescription());
		assertEquals(-5, stats.getPoints().intValue());
		assertEquals(100, stats.getStatsId().intValue());
		assertEquals(-1, stats.getId().longValue());
		assertEquals(-1, stats.getLeagueType().getId().longValue());		
		
		stats = statsDao.getStatistic(leagueTypeId, 102);
		assertNull(stats);
		
		LeagueType leagueType = leagueTypeDao.getLeagueTypeByName("Soccer");
		stats = new Statistic();
		stats.setDescription("Goal");
		stats.setLeagueType(leagueType);
		stats.setPoints(20);
		stats.setStatsId(102);
		statsDao.saveOrUpdate(stats);
		
		s = statsDao.getStatistics(leagueTypeId);
		assertEquals(3, s.size());
		for(Statistic ss : s) {
			if (ss.getId().longValue() == -1) {
				
			} else if (ss.getId().longValue() == -2) {
				
			} else if (ss.getStatsId().equals(102)) {
				logger.debug("Got: "+stats);
				assertEquals("Goal", stats.getDescription());
				assertEquals(20, stats.getPoints().intValue());
				assertEquals(102, stats.getStatsId().intValue());
				assertNotNull(stats.getId().longValue());
				assertEquals(leagueType.getId(), stats.getLeagueType().getId());
			} else {
				fail("Meh - unexpected statistic: "+ss);
			}
		}
	}
}
