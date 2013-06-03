package com.africaapps.league.dao.game.hibernate;

import java.util.List;

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
}
