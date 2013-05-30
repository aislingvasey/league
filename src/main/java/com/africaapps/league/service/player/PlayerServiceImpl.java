package com.africaapps.league.service.player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.africaapps.league.dao.league.PlayerDao;
import com.africaapps.league.dao.league.PlayerMatchStatsDao;
import com.africaapps.league.dao.league.PositionDao;
import com.africaapps.league.dao.league.StatisticDao;
import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.league.Player;
import com.africaapps.league.model.league.PlayerMatchStats;
import com.africaapps.league.model.league.Position;
import com.africaapps.league.model.league.Statistic;
import com.africaapps.league.service.transaction.ReadTransaction;
import com.africaapps.league.service.transaction.WriteTransaction;

@Service
public class PlayerServiceImpl implements PlayerService {

	@Autowired
	private PlayerDao playerDao;
	@Autowired
	private PositionDao positionDao;
	@Autowired
	private StatisticDao statisticDao;
	@Autowired
	private PlayerMatchStatsDao playerMatchStatsDao;

	private static Logger logger = LoggerFactory.getLogger(PlayerServiceImpl.class);
	
	@ReadTransaction
	@Override
	public Player getPlayer(int playerId) throws LeagueException {
		return playerDao.getByPlayerId(playerId);
	}

	@WriteTransaction
	@Override
	public void savePlayer(Player player) throws LeagueException {
		if (player != null) {
			player.setId(playerDao.getIdByPlayerId(player.getPlayerId()));
			playerDao.saveOrUpdate(player);
		}
	}

	@ReadTransaction
	@Override
	public Position getPosition(long leagueTypeId, int positionNumber) throws LeagueException {
		return positionDao.getPosition(leagueTypeId, positionNumber);
	}

	@WriteTransaction
	@Override
	public void savePosition(Position position) throws LeagueException {
		if (position != null) {
			positionDao.saveAndUpdate(position);
		}
	}

	@ReadTransaction
	@Override
	public Statistic getStatistic(long leagueTypeId, int statsId) throws LeagueException {
		return statisticDao.getStatistic(leagueTypeId, statsId);
	}

	@WriteTransaction
	@Override
	public void saveStatistic(Statistic statistic) throws LeagueException {
		Statistic existing = statisticDao.getStatistic(statistic.getLeagueType().getId(), statistic.getStatsId());
		if (existing != null) {
			statistic.setId(existing.getId());
		}
		statisticDao.saveOrUpdate(statistic);
	}

	@WriteTransaction
	@Override
	public void savePlayerMatchStats(PlayerMatchStats playerMatchStats) throws LeagueException {		
		PlayerMatchStats existing = playerMatchStatsDao.getStats(playerMatchStats.getPlayerMatch().getId(), playerMatchStats.getStatistic().getId(), playerMatchStats.getMatchTime());
		if (existing == null) {
			playerMatchStatsDao.saveOrUpdate(playerMatchStats);
			logger.debug("Saved stats: "+playerMatchStats);
		} else {
			logger.debug("Not resaving existing stats: "+existing);
		}
	}
}
