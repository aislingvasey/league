package com.africaapps.league.service.pool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.africaapps.league.dao.game.PoolDao;
import com.africaapps.league.dao.game.PoolPlayerDao;
import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.game.Pool;
import com.africaapps.league.model.game.PoolPlayer;
import com.africaapps.league.model.league.LeagueSeason;
import com.africaapps.league.model.league.Player;
import com.africaapps.league.service.transaction.ReadTransaction;
import com.africaapps.league.service.transaction.WriteTransaction;

@Service
public class PoolServiceImpl implements PoolService {

	@Autowired
	private PoolDao poolDao;
	@Autowired
	private PoolPlayerDao poolPlayerDao;
	
	private static Logger logger = LoggerFactory.getLogger(PoolServiceImpl.class);
	
	@ReadTransaction
	@Override
	public Pool getPool(LeagueSeason leagueSeason) throws LeagueException {
		if (leagueSeason != null) {
			return poolDao.getByLeagueSeason(leagueSeason.getId());
		} else {
			return null;
		}
	}

	@WriteTransaction
	@Override
	public void savePlayer(Pool pool, Player player) throws LeagueException {
		if (pool != null && player != null) {
			PoolPlayer pp = poolPlayerDao.getByPoolAndPlayer(pool.getId(), player.getId());
			if (pp == null) {
				pp = new PoolPlayer();
				pp.setPool(pool);
				pp.setPlayer(player);
				pp.setPlayerPrice(0);
				pp.setPlayerCurrentScore(0);
				poolPlayerDao.saveOrUpdate(pp);
				logger.warn("Set price for new PoolPlayer: "+pp+"!!!");
			}
		} else {
			throw new LeagueException("Unable to save PoolPlayer for invalid input: "+pool+", "+player);
		}
	}
}
