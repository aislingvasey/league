package com.africaapps.league.dao.game.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.africaapps.league.dao.game.PoolDao;
import com.africaapps.league.dao.hibernate.BaseHibernateDao;
import com.africaapps.league.model.game.Pool;

@Repository
public class PoolDaoImpl extends BaseHibernateDao implements PoolDao {

	@Override
	public void saveOrUpdate(Pool pool) {
		if (pool != null) {
			sessionFactory.getCurrentSession().saveOrUpdate(pool);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Pool getByLeagueSeason(long leagueSeasonId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Pool.class);
		criteria.createAlias("leagueSeason", "s").add(Restrictions.eq("s.id", leagueSeasonId));
		List<Pool> pools = criteria.list();
		if (pools.size() == 1) {
			return pools.get(0);
		}
		return null;
	}
}
