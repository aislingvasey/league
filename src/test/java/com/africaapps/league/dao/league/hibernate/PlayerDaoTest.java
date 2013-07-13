package com.africaapps.league.dao.league.hibernate;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.africaapps.league.BaseSpringDbUnitTest;
import com.africaapps.league.dao.league.PlayerDao;
import com.africaapps.league.dao.league.PositionDao;
import com.africaapps.league.dao.league.TeamDao;
import com.africaapps.league.model.league.Player;

public class PlayerDaoTest extends BaseSpringDbUnitTest {

	@Autowired
	private PlayerDao playerDao;
	@Autowired
	private TeamDao teamDao;
	@Autowired
	private PositionDao positionDao;
	
	private static Logger logger = LoggerFactory.getLogger(TeamDaoTest.class);
	
	@Transactional(readOnly=false)
	@Test
	public void saveUpdateAndGetById() throws Exception {
		assertNotNull(playerDao);
		assertNotNull(teamDao);
		
		int playerId = 1;
		int playerId2 = 2;
		long leagueTypeId = -1;
		int positionNumber = 1;
		int teamId = 11;
		
		Player player = playerDao.getByPlayerId(playerId);
		assertNull(player);
		
		player = new Player();
		player.setPlayerId(playerId);
		player.setFirstName("Gold");
		player.setLastName("Smith");
		player.setShirtNumber(5);
		player.setPosition(positionDao.getPosition(leagueTypeId, positionNumber));
		player.setTeam(teamDao.getBySeasonandTeamId(-1, teamId));
		playerDao.saveOrUpdate(player);
		
		player = playerDao.getByPlayerId(playerId);
		assertNotNull(player);
		logger.debug("Got: "+player);
		assertNotNull(player.getId());
		assertEquals(playerId, player.getPlayerId().intValue());
		assertEquals("Gold", player.getFirstName());
		assertEquals("Smith", player.getLastName());
		assertEquals(5, player.getShirtNumber().intValue());
		assertEquals(-1, player.getPosition().getId().longValue());
		assertEquals(teamId, player.getTeam().getTeamId());
		
		player = new Player();
		player.setPlayerId(playerId2);
		player.setFirstName("Robert");
		player.setLastName("Smith");
		player.setShirtNumber(6);
		player.setPosition(positionDao.getPosition(leagueTypeId, positionNumber));
		player.setTeam(teamDao.getBySeasonandTeamId(-1, teamId));
		playerDao.saveOrUpdate(player);
		
		player = playerDao.getByPlayerId(playerId2);
		assertNotNull(player);
		logger.debug("Got: "+player);
		assertNotNull(player.getId());
		assertEquals(playerId2, player.getPlayerId().intValue());
		assertEquals("Robert", player.getFirstName());
		assertEquals("Smith", player.getLastName());
		assertEquals(6, player.getShirtNumber().intValue());
		assertEquals(-1, player.getPosition().getId().longValue());
		assertEquals(teamId, player.getTeam().getTeamId());
	}	
}
