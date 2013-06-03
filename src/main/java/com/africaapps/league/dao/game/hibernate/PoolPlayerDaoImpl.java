package com.africaapps.league.dao.game.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.africaapps.league.dao.game.PoolPlayerDao;
import com.africaapps.league.dao.hibernate.BaseHibernateDao;
import com.africaapps.league.model.game.PoolPlayer;

@Repository
public class PoolPlayerDaoImpl extends BaseHibernateDao implements PoolPlayerDao {

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
}
