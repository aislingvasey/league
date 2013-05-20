package com.africaapps.league.dao.league.hibernate;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.africaapps.league.BaseSpringDbUnitTest;
import com.africaapps.league.dao.league.TeamDao;
import com.africaapps.league.model.league.Team;

public class TeamDaoTest extends BaseSpringDbUnitTest {

	@Autowired
	private TeamDao teamDao;
	
	private static Logger logger = LoggerFactory.getLogger(TeamDaoTest.class);
	
	@Transactional(readOnly=false)
	@Test
	public void saveUpdateAndGetById() throws Exception {
		assertNotNull(teamDao);
		
		long teamId = Long.valueOf(-1);
		
		Team team = teamDao.getById(teamId);
		assertNull(team);
		
		team = new Team();
		team.setId(teamId);
		team.setCity("Bloemfontein");
		team.setClubName("Sundowns");
		team.setTeamName("Sundowns Team");
		team.setCoach(null);
		team.setManager("Bob Smith");
		teamDao.saveOrUpdate(team);
		
		team = teamDao.getById(teamId);
		assertNotNull(team);
		logger.debug("Got team: "+team);
		assertTrue(teamId == team.getId().longValue());
		assertEquals("Bloemfontein", team.getCity());
		assertEquals("Sundowns", team.getClubName());
		assertEquals("Sundowns Team", team.getTeamName());
		assertEquals("Bob Smith", team.getManager());
		assertNull(team.getCoach());
	}
}
