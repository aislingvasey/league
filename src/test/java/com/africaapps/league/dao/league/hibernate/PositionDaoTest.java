package com.africaapps.league.dao.league.hibernate;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.africaapps.league.BaseSpringDbUnitTest;
import com.africaapps.league.dao.league.LeagueTypeDao;
import com.africaapps.league.dao.league.PositionDao;
import com.africaapps.league.model.league.Position;
import com.africaapps.league.model.league.PositionType;

public class PositionDaoTest extends BaseSpringDbUnitTest {

	@Autowired
	private PositionDao positionDao;
	@Autowired
	private LeagueTypeDao leagueTypeDao;
	
	private static Logger logger = LoggerFactory.getLogger(TeamDaoTest.class);
	
	@Transactional(readOnly=false)
	@Test
	public void saveUpdateAndGet() throws Exception {
		assertNotNull(positionDao);
		
		long leagueTypeId = -1;
		int positionNumber = 1;
				
		Position pos = positionDao.getPosition(leagueTypeId, positionNumber);
		assertNotNull(pos);
		assertEquals(-1, pos.getId().longValue());

		Position pos2 = new Position();
		pos2.setLeagueType(leagueTypeDao.getLeagueTypeByName("Soccer"));
		pos2.setPositionNumber(2);
		pos2.setPositionType(PositionType.GOALKEEPER);
		positionDao.saveAndUpdate(pos2);
		
		pos = positionDao.getPosition(leagueTypeId, positionNumber);
		assertNotNull(pos);
		assertEquals(-1, pos.getId().longValue());
		
		pos = positionDao.getPosition(leagueTypeId, 2);
		assertNotNull(pos);
		logger.debug("Got position: "+pos);
		assertFalse(-1 == pos.getId().longValue());
		assertTrue(2 == pos.getPositionNumber());
	}	
}