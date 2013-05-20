package com.africaapps.league.dao.league.hibernate;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.africaapps.league.dao.hibernate.BaseHibernateDao;
import com.africaapps.league.dao.league.LeagueDao;
import com.africaapps.league.model.league.League;

@Repository
public class LeagueDaoImpl extends BaseHibernateDao implements LeagueDao {

	@Override
	public void saveOrUpdate(League league) {
		if (league != null) {
			sessionFactory.getCurrentSession().saveOrUpdate(league);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public League getLeagueByName(String name) {
		List<League> leagues = sessionFactory.getCurrentSession()
																					.createCriteria(League.class)
																					.add(Restrictions.eq("name", name))
																					.list();
		if (leagues.size() == 1) {
			return leagues.get(0);
		} else {
			return null;
		}
	}
}
