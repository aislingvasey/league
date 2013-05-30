package com.africaapps.league.dao.league.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.africaapps.league.dao.hibernate.BaseHibernateDao;
import com.africaapps.league.dao.league.StatisticDao;
import com.africaapps.league.model.league.Statistic;

@Repository
public class StatisticDaoImpl extends BaseHibernateDao implements StatisticDao {
	
	private static Logger logger = LoggerFactory.getLogger(StatisticDaoImpl.class);

	@Override
	public void saveOrUpdate(Statistic statistic) {
		if (statistic != null) {
			sessionFactory.getCurrentSession().saveOrUpdate(statistic);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Statistic> getStatistics(long leagueTypeId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Statistic.class);
		criteria.createAlias("leagueType", "t").add(Restrictions.eq("t.id", leagueTypeId));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Statistic getStatistic(long leagueTypeId, int statsId) {
		logger.info("statsId: "+statsId+" leagueTypeId: "+leagueTypeId);
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Statistic.class);
		criteria.add(Restrictions.eq("statsId", statsId));
		criteria.createAlias("leagueType", "t").add(Restrictions.eq("t.id", leagueTypeId));
		List<Statistic> stats = criteria.list();
		logger.info("Got stats: "+stats.size());
		if (stats.size() == 1) {
			return stats.get(0);
		}
		return null;
	}
}
