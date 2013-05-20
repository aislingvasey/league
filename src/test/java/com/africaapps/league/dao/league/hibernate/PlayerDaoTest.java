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
		
		long playerId = -1;
		long playerId2 = -2;
		long leagueTypeId = -1;
		int positionNumber = 1;
		long teamId = -1;
		
		Player player = playerDao.getById(playerId);
		assertNull(player);
		
		player = new Player();
		player.setId(playerId);
		player.setFirstName("Gold");
		player.setLastName("Smith");
		player.setNickName("Goldie");
		player.setShirtNumber(5);
		player.setPosition(positionDao.getPosition(leagueTypeId, positionNumber));
		player.setTeam(teamDao.getById(teamId));
		playerDao.saveOrUpdate(player);
		
		player = playerDao.getById(playerId);
		assertNotNull(player);
		logger.debug("Got: "+player);
		assertEquals(playerId, player.getId().longValue());
		assertEquals("Gold", player.getFirstName());
		assertEquals("Smith", player.getLastName());
		assertEquals("Goldie", player.getNickName());
		assertEquals(5, player.getShirtNumber().intValue());
		assertEquals(-1, player.getPosition().getId().longValue());
		assertEquals(teamId, player.getTeam().getId().longValue());
		
		player = new Player();
		player.setId(playerId2);
		player.setFirstName("Robert");
		player.setLastName("Smith");
		player.setNickName("Bob");
		player.setShirtNumber(6);
		player.setPosition(positionDao.getPosition(leagueTypeId, positionNumber));
		player.setTeam(teamDao.getById(teamId));
		playerDao.saveOrUpdate(player);
		
		player = playerDao.getById(playerId2);
		assertNotNull(player);
		logger.debug("Got: "+player);
		assertEquals(playerId2, player.getId().longValue());
		assertEquals("Robert", player.getFirstName());
		assertEquals("Smith", player.getLastName());
		assertEquals("Bob", player.getNickName());
		assertEquals(6, player.getShirtNumber().intValue());
		assertEquals(-1, player.getPosition().getId().longValue());
		assertEquals(teamId, player.getTeam().getId().longValue());
	}	
}
