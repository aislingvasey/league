package com.africaapps.league.dao.game.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.africaapps.league.dao.game.UserTeamDao;
import com.africaapps.league.dao.hibernate.BaseHibernateDao;
import com.africaapps.league.model.game.UserTeam;

@Repository
public class UserTeamDaoImpl extends BaseHibernateDao implements UserTeamDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<UserTeam> getTeams(long userId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(UserTeam.class);
		criteria.createAlias("user", "u").add(Restrictions.eq("u.id", userId));
		criteria.addOrder(org.hibernate.criterion.Order.asc("name"));
		return criteria.list();
	}

	@Override
	public void saveOrUpdate(UserTeam userTeam) {
		if (userTeam != null) {
			sessionFactory.getCurrentSession().saveOrUpdate(userTeam);
		}
	}
}
