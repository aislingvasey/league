package com.africaapps.league.dao.league.hibernate;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.africaapps.league.BaseSpringDbUnitTest;
import com.africaapps.league.dao.league.LeagueSeasonDao;
import com.africaapps.league.dao.league.TeamDao;
import com.africaapps.league.model.league.Team;

public class TeamDaoTest extends BaseSpringDbUnitTest {

	@Autowired
	private TeamDao teamDao;
	@Autowired
	private LeagueSeasonDao leagueSeasonDao;
	
	private static Logger logger = LoggerFactory.getLogger(TeamDaoTest.class);
	
	@Transactional(readOnly=false)
	@Test
	public void saveUpdateAndGetById() throws Exception {
		assertNotNull(teamDao);
		
		long leagueId = Long.valueOf(-1);
		long leagueSeasonId = Long.valueOf(-1);
		int teamId = 11;
		
		Team team = teamDao.getBySeasonandTeamId(leagueSeasonId, teamId);
		assertNull(team);
		
		team = new Team();
		team.setTeamId(teamId);
		team.setCity("Bloemfontein");
		team.setClubName("Sundowns");
		team.setTeamName("Sundowns Team");
		team.setLeagueSeason(leagueSeasonDao.getCurrentSeason(leagueId));
		teamDao.saveOrUpdate(team);
		
		team = teamDao.getBySeasonandTeamId(leagueSeasonId, teamId);
		assertNotNull(team);
		logger.debug("Got team: "+team);
		assertNotNull(team.getId());
		assertTrue(teamId == team.getTeamId());
		assertEquals("Bloemfontein", team.getCity());
		assertEquals("Sundowns", team.getClubName());
		assertEquals("Sundowns Team", team.getTeamName());
	}
}
