package com.africaapps.league.dao.league.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.africaapps.league.dao.hibernate.BaseHibernateDao;
import com.africaapps.league.dao.league.PlayerMatchStatisticDao;
import com.africaapps.league.model.league.PlayerMatchStatistic;

@Repository
public class PlayerMatchStatisticDaoImpl extends BaseHibernateDao implements PlayerMatchStatisticDao {

	@Override
	public void saveOrUpdate(PlayerMatchStatistic pms) {
		if (pms != null) {
			sessionFactory.getCurrentSession().saveOrUpdate(pms);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public PlayerMatchStatistic get(long playerMatchId, long statId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(PlayerMatchStatistic.class);
		criteria.createAlias("playerMatch", "pm").add(Restrictions.eq("pm.id", playerMatchId));
		criteria.createAlias("statistic", "s").add(Restrictions.eq("s.id", statId));
		List<PlayerMatchStatistic> stats = criteria.list();
		if (stats.size() > 0) {
			return stats.get(0);
		} else {
			return null;
		}
	}
}
