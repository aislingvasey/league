package com.africaapps.league.dao.league.hibernate;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.africaapps.league.BaseSpringDbUnitTest;
import com.africaapps.league.dao.league.LeagueTypeDao;
import com.africaapps.league.model.league.LeagueType;
import com.africaapps.league.model.league.LeagueTypeStatus;

public class LeagueTypeDaoTest extends BaseSpringDbUnitTest {

	@Autowired
	private LeagueTypeDao leagueTypeDao;
	
	private static Logger logger = LoggerFactory.getLogger(LeagueTypeDaoTest.class);
	
	@Transactional(readOnly=false)
	@Test
	public void saveUpdateAndGetAll() throws Exception {
		assertNotNull(leagueTypeDao);
		
		List<LeagueType> types = leagueTypeDao.getAll();
		assertEquals(0, types.size());
		assertNull(leagueTypeDao.getLeagueTypeByName("Soccer"));
		
		LeagueType type = new LeagueType();
		type.setName("Soccer");
		type.setDescription("League for soccer");
		type.setStatus(LeagueTypeStatus.INACTIVE);
		leagueTypeDao.saveOrUpdate(type);
		
		types = leagueTypeDao.getAll();
		assertEquals(1, types.size());
		logger.debug("Got type: "+types.get(0));
		assertNotNull(types.get(0).getId());
		assertEquals(type.getName(), types.get(0).getName());
		assertEquals(type.getDescription(), types.get(0).getDescription());
		assertEquals(LeagueTypeStatus.INACTIVE, types.get(0).getStatus());
		
		types.get(0).setStatus(LeagueTypeStatus.ACTIVE);
		leagueTypeDao.saveOrUpdate(type);
		
		LeagueType soccer = leagueTypeDao.getLeagueTypeByName("Soccer");
		assertNotNull(soccer.getId());
		logger.debug("Got typeByName: "+types.get(0));
		assertEquals(type.getName(), soccer.getName());
		assertEquals(type.getDescription(), soccer.getDescription());
		assertEquals(LeagueTypeStatus.ACTIVE, soccer.getStatus());
		
		types = leagueTypeDao.getAll();
		assertEquals(1, types.size());
		logger.debug("Got type: "+types.get(0));
		assertNotNull(types.get(0).getId());
		assertEquals(type.getName(), types.get(0).getName());
		assertEquals(type.getDescription(), types.get(0).getDescription());
		assertEquals(LeagueTypeStatus.ACTIVE, types.get(0).getStatus());
	}
}
