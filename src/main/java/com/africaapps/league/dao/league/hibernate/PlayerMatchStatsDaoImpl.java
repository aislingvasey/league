package com.africaapps.league.dao.league.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.africaapps.league.dao.hibernate.BaseHibernateDao;
import com.africaapps.league.dao.league.PlayerMatchStatsDao;
import com.africaapps.league.model.league.PlayerMatchStats;

@Repository
public class PlayerMatchStatsDaoImpl extends BaseHibernateDao implements PlayerMatchStatsDao {

	@Override
	public void saveOrUpdate(PlayerMatchStats playerMatchStats) {
		if (playerMatchStats != null) {
			sessionFactory.getCurrentSession().saveOrUpdate(playerMatchStats);
		}
	}

	@Override
	public Integer getTotalPoints(long PlayerMatchId) {
		// TODO 
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PlayerMatchStats> getStats(long playerMatchId) {		
		return sessionFactory.getCurrentSession().createCriteria(PlayerMatchStats.class)
				.createAlias("playerMatch", "pm").add(Restrictions.eq("pm.id", playerMatchId))
				.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public PlayerMatchStats getStats(Long playerMatchId, Long statisticId, String matchTime) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(PlayerMatchStats.class);
		criteria.createAlias("playerMatch", "pm").add(Restrictions.eq("pm.id", playerMatchId));
		criteria.createAlias("statistic", "s").add(Restrictions.eq("s.id", statisticId));
		criteria.add(Restrictions.eq("matchTime", matchTime));
		List<PlayerMatchStats> stats = criteria.list();
		if (stats.size() == 1) {
			return stats.get(0);
		} else {
			return null;
		}
	}
}