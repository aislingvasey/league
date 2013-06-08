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
	
	@SuppressWarnings("unchecked")
	@Override
	public UserTeam getTeam(long userId, String teamName) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(UserTeam.class);
		criteria.add(Restrictions.eq("name", teamName));	
		criteria.createAlias("user", "u").add(Restrictions.eq("u.id", userId));
		List<UserTeam> teams = criteria.list();
		if (teams.size() > 0) {
			return teams.get(0);
		} else {
			return null;
		}
	}

	@Override
	public void saveOrUpdate(UserTeam userTeam) {
		if (userTeam != null) {
			sessionFactory.getCurrentSession().saveOrUpdate(userTeam);
		}
	}
}
