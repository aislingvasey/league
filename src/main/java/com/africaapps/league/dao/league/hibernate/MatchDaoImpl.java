package com.africaapps.league.dao.league.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
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

	@SuppressWarnings("unchecked")
	@Override
	public Match getByMatchId(int matchId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Match.class);
		criteria.add(Restrictions.eq("matchId", matchId));
		List<Match> matches = criteria.list();
		if (matches.size() == 1) {
			return matches.get(0);
		} else {
			return null;
		}
	}
}
