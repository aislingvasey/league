package com.africaapps.league.dao.league.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
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

	@SuppressWarnings("unchecked")
	@Override
	public Team getBySeasonandTeamId(long leagueSeasonId, int teamId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Team.class);
		criteria.add(Restrictions.eq("teamId", teamId));
		criteria.createAlias("leagueSeason", "s").add(Restrictions.eq("s.id", leagueSeasonId));
		List<Team> teams = criteria.list();
		if (teams.size() == 1) {
			return teams.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Long getIdBySeasonandTeamId(long leagueSeasonId, int teamId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Team.class);
		criteria.add(Restrictions.eq("teamId", teamId));
		criteria.createAlias("leagueSeason", "s").add(Restrictions.eq("s.id", leagueSeasonId));
		criteria.setProjection(Projections.property("id"));
		List<Long> ids = criteria.list();
		if (ids.size() == 1) {
			return ids.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Team> getBySeasonId(long leagueSeasonId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Team.class);
		criteria.createAlias("leagueSeason", "s").add(Restrictions.eq("s.id", leagueSeasonId));
		return criteria.list();
	}

	@Override
	public String getName(long teamId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Team.class);
		criteria.add(Restrictions.eq("id", teamId));
		criteria.setProjection(Projections.property("clubName"));
		return (String) criteria.uniqueResult();
	}
}