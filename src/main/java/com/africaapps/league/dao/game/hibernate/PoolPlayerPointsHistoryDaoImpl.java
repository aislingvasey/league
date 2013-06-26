package com.africaapps.league.dao.game.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.africaapps.league.dao.game.PoolPlayerPointsHistoryDao;
import com.africaapps.league.dao.hibernate.BaseHibernateDao;
import com.africaapps.league.model.game.PoolPlayerPointsHistory;

@Repository
public class PoolPlayerPointsHistoryDaoImpl extends BaseHibernateDao implements PoolPlayerPointsHistoryDao {

	@Override
	public void save(PoolPlayerPointsHistory history) {
		if (history != null) {
			sessionFactory.getCurrentSession().save(history);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PoolPlayerPointsHistory> getHistoryForPlayer(long poolPlayerId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(PoolPlayerPointsHistory.class);
		criteria.createAlias("poolPlayer", "pp").add(Restrictions.eq("pp.id", poolPlayerId));
		return criteria.list();
	}
}
