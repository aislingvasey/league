package com.africaapps.league.dao.league.hibernate;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.africaapps.league.dao.hibernate.BaseHibernateDao;
import com.africaapps.league.dao.league.LeagueDataDao;
import com.africaapps.league.model.league.LeagueData;

@Repository
public class LeagueDataDaoImpl extends BaseHibernateDao implements LeagueDataDao {

	@Override
	public LeagueData get(long leagueId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(LeagueData.class);
		criteria.createAlias("league", "l").add(Restrictions.eq("l.id", leagueId));
		return (LeagueData) criteria.uniqueResult();
	}

	@Override
	public void saveOrUpdate(LeagueData leagueData) {
		if (leagueData != null) {
			sessionFactory.getCurrentSession().saveOrUpdate(leagueData);
		}
	}
}
