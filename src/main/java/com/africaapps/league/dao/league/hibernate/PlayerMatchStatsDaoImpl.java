package com.africaapps.league.dao.league.hibernate;

import java.util.List;

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
}
