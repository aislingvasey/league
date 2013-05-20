package com.africaapps.league.dao.league.hibernate;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.africaapps.league.dao.hibernate.BaseHibernateDao;
import com.africaapps.league.dao.league.LeagueTypeDao;
import com.africaapps.league.model.league.LeagueType;

@Repository
public class LeagueTypeDaoImpl extends BaseHibernateDao implements LeagueTypeDao {

	@Override
	public void saveOrUpdate(LeagueType leagueType) {
		if (leagueType != null) {
      sessionFactory.getCurrentSession().saveOrUpdate(leagueType);
    } 
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LeagueType> getAll() {
		return sessionFactory.getCurrentSession().createCriteria(LeagueType.class).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public LeagueType getLeagueTypeByName(String name) {
		List<LeagueType> types = sessionFactory.getCurrentSession()
																					 .createCriteria(LeagueType.class)
																					 .add(Restrictions.eq("name", name))
																					 .list();
		if (types.size() == 1) {
			return types.get(0);
		} else {
			return null;
		}
	}
}
