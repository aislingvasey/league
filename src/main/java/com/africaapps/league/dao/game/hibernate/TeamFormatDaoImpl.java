package com.africaapps.league.dao.game.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.africaapps.league.dao.game.TeamFormatDao;
import com.africaapps.league.dao.hibernate.BaseHibernateDao;
import com.africaapps.league.model.game.TeamFormat;

@Repository
public class TeamFormatDaoImpl extends BaseHibernateDao implements TeamFormatDao {

	@SuppressWarnings("unchecked")
	@Override
	public TeamFormat getDefault() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(TeamFormat.class);
		criteria.add(Restrictions.eq("defaultFormat", true));
		List<TeamFormat> formats = criteria.list();
		if (formats.size() > 0) {
			return formats.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TeamFormat> getAll() {
		return sessionFactory.getCurrentSession().createCriteria(TeamFormat.class).list();
	}

	@Override
	public TeamFormat get(long teamFormatId) {
		return (TeamFormat) sessionFactory.getCurrentSession().get(TeamFormat.class, teamFormatId);
	}
}
