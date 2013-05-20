package com.africaapps.league.dao.league.hibernate;

import org.springframework.stereotype.Repository;

import com.africaapps.league.dao.hibernate.BaseHibernateDao;
import com.africaapps.league.dao.league.TeamDao;
import com.africaapps.league.model.league.Team;

@Repository
public class TeamDaoImpl extends BaseHibernateDao implements TeamDao {

	@Override
	public void saveOrUpdate(Team team) {
		if (team != null) {
			sessionFactory.getCurrentSession().saveOrUpdate(team);
		}
	}

	@Override
	public Team getById(long teamId) {
		return (Team) sessionFactory.getCurrentSession().get(Team.class, teamId);
	}
}
