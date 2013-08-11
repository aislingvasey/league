package com.africaapps.league.dao.game.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.africaapps.league.dao.game.PoolPlayerDao;
import com.africaapps.league.dao.hibernate.BaseHibernateDao;
import com.africaapps.league.model.game.PoolPlayer;

@Repository
public class PoolPlayerDaoImpl extends BaseHibernateDao implements PoolPlayerDao {
	
	private static final String ADD_PLAYER_MATCH_SCORE 
	= "UPDATE game_pool_player "
  +" SET player_current_score = player_current_score + :points "  
  +" WHERE game_pool_player.player_id = :poolPlayerId";

	private static final Logger logger = LoggerFactory.getLogger(PoolPlayerDaoImpl.class);
	
	@Override
	public void saveOrUpdate(PoolPlayer poolPlayer) {
		if (poolPlayer != null) {
			sessionFactory.getCurrentSession().saveOrUpdate(poolPlayer);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public PoolPlayer getByPoolAndPlayer(long poolId, long playerId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(PoolPlayer.class);
		criteria.createAlias("pool", "p").add(Restrictions.eq("p.id", poolId));
		criteria.createAlias("player", "pl").add(Restrictions.eq("pl.id", playerId));
		List<PoolPlayer> players = criteria.list();
		if (players.size() == 1) {
			return players.get(0);
		}
		return null;
	}

	@Override
	public PoolPlayer get(long poolPlayerId) {
		return (PoolPlayer) sessionFactory.getCurrentSession().get(PoolPlayer.class, poolPlayerId);
	}

	@Override
	public void addPlayerScore(long poolPlayerId, long matchId, Double playerScore) {
		logger.info("Added points from match:"+matchId+" to corresponding poolPlayer:"+poolPlayerId+" points:"+playerScore);
		Query query = sessionFactory.getCurrentSession().createSQLQuery(ADD_PLAYER_MATCH_SCORE);
		query.setLong("poolPlayerId", poolPlayerId);
		query.setDouble("points", playerScore);
		int rowsUpdated = query.executeUpdate();
		logger.info("Updated poolPlayer's points: "+rowsUpdated);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PoolPlayer> getByPoolId(long poolId, int page, int pageSize) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(PoolPlayer.class);
		criteria.createAlias("pool", "p").add(Restrictions.eq("p.id", poolId));
		criteria.add(Restrictions.gt("playerPrice", Long.valueOf(0))); //no players with zero price
		criteria.addOrder(Order.desc("playerCurrentScore"));
		if (page == 0) {
			criteria.setFirstResult(0);
		} else {
			criteria.setFirstResult(page * pageSize);
		}
    criteria.setMaxResults(pageSize);
    return criteria.list();
	}
}