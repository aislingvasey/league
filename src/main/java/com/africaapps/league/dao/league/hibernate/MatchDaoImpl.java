package com.africaapps.league.dao.league.hibernate;

import org.springframework.stereotype.Repository;

import com.africaapps.league.dao.hibernate.BaseHibernateDao;
import com.africaapps.league.dao.league.MatchDao;
import com.africaapps.league.model.league.Match;

@Repository
public class MatchDaoImpl extends BaseHibernateDao implements MatchDao {

	@Override
	public void saveOrUpdate(Match match) {
		if (match != null) {
			sessionFactory.getCurrentSession().saveOrUpdate(match);
		}
	}

	@Override
	public Match getById(long matchId) {
		return (Match) sessionFactory.getCurrentSession().get(Match.class, matchId);
	}
}
