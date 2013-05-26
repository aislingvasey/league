package com.africaapps.league.dao.league.hibernate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.africaapps.league.BaseSpringDbUnitTest;
import com.africaapps.league.dao.league.MatchDao;
import com.africaapps.league.dao.league.PlayerDao;
import com.africaapps.league.dao.league.PlayerMatchDao;
import com.africaapps.league.dao.league.PlayerMatchStatsDao;
import com.africaapps.league.dao.league.StatisticDao;
import com.africaapps.league.dao.league.TeamDao;
import com.africaapps.league.model.league.Match;
import com.africaapps.league.model.league.MatchProcessingStatus;
import com.africaapps.league.model.league.Player;
import com.africaapps.league.model.league.PlayerMatch;
import com.africaapps.league.model.league.PlayerMatchStats;

public class PlayerMatchStatsDaoTest extends BaseSpringDbUnitTest {

	@Autowired
	private PlayerMatchStatsDao playerMatchStatsDao;
	@Autowired
	private MatchDao matchDao;
	@Autowired
	private PlayerMatchDao playerMatchDao;
	@Autowired
	private PlayerDao playerDao;
	@Autowired
	private TeamDao teamDao;
	@Autowired
	private StatisticDao statsDao;
	
private static Logger logger = LoggerFactory.getLogger(LeagueDaoTest.class);
	
	@Transactional(readOnly=false)
	@Test
	public void saveAndGet() throws Exception {
		int matchId = 1234;
		int playerId = 5;
		long statsId = -2;
		
		Match match = matchDao.getByMatchId(matchId);
		assertNull(match);
		
		match = new Match();		
		match.setMatchId(matchId);
		match.setLocation("Cape Town Stadium");
		match.setStatus(MatchProcessingStatus.INPROGRESS);
		match.setTeam1(teamDao.getBySeasonandTeamId(-1, 10));		
		match.setTeam2(teamDao.getBySeasonandTeamId(-1, 11));
		match.setFinalScore("2-1");
		Date start = new SimpleDateFormat("yyyy-mm-dd HH:mm").parse("2013-05-10 20:30");
		match.setStartDateTime(start);
		Date end = new SimpleDateFormat("yyyy-mm-dd HH:mm").parse("2013-05-10 22:00");
		match.setEndDateTime(end);
		matchDao.saveOrUpdate(match);
		
		Match match2 = matchDao.getByMatchId(matchId);
		assertNotNull(match2);
		assertNotNull(match2.getId());
		assertEquals(match.getFinalScore(), match2.getFinalScore());
		assertEquals(match.getLocation(), match2.getLocation());
		assertEquals(match.getEndDateTime(), match2.getEndDateTime());
		assertEquals(match.getId(), match2.getId());
		assertEquals(match.getStartDateTime(), match2.getStartDateTime());
		assertEquals(match.getStatus(), match2.getStatus());
		assertEquals(match.getTeam1().getId(), match2.getTeam1().getId());
		assertEquals(match.getTeam2().getId(), match2.getTeam2().getId());
		
		Player player = playerDao.getByPlayerId(playerId);
		assertNotNull(player);
		
		PlayerMatch playerMatch = playerMatchDao.getByIds(match2.getId(), player.getId());
		assertNull(playerMatch);
		playerMatch = new PlayerMatch();
		playerMatch.setMatch(match2);
		playerMatch.setPlayer(player);
		playerMatch.setPlayerScore(null);
		playerMatchDao.saveOrUpdate(playerMatch);
		PlayerMatch playerMatch2 = playerMatchDao.getByIds(match2.getId(), player.getId());
		assertNotNull(playerMatch2);

		List<PlayerMatchStats> stats = playerMatchStatsDao.getStats(playerMatch2.getId());
		assertEquals(0, stats.size());
		
		PlayerMatchStats playerMatchStats = new PlayerMatchStats();
		playerMatchStats.setMatchTime("00:05");
		playerMatchStats.setStatistic(statsDao.getStatistic(-1, statsId));
		playerMatchStats.setPlayerMatch(playerMatch2);
		playerMatchStatsDao.saveOrUpdate(playerMatchStats);
		
		stats = playerMatchStatsDao.getStats(playerMatch2.getId());
		assertEquals(1, stats.size());
		logger.info("Got: "+stats.get(0));
	}
}