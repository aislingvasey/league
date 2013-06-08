package com.africaapps.league.dao.game.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.africaapps.league.dao.game.UserLeagueDao;
import com.africaapps.league.dao.hibernate.BaseHibernateDao;
import com.africaapps.league.model.game.UserLeague;

@Repository
public class UserLeagueDaoImpl extends BaseHibernateDao implements UserLeagueDao {

	@Override
	public void saveOrUpdate(UserLeague userLeague) {
		if (userLeague != null) {
			sessionFactory.getCurrentSession().saveOrUpdate(userLeague);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public UserLeague getDefault() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(UserLeague.class);
		criteria.add(Restrictions.eq("defaultUserLeague", true));
		List<UserLeague> leagues = criteria.list();
		if (leagues.size() > 0) {
			return leagues.get(0);
		}
		return null;
	}
}
