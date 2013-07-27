package com.africaapps.league.dao.league.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.africaapps.league.dao.hibernate.BaseHibernateDao;
import com.africaapps.league.dao.league.FixtureDao;
import com.africaapps.league.model.league.Fixture;

@Repository
public class FixtureDaoImpl extends BaseHibernateDao implements FixtureDao {

	@SuppressWarnings("unchecked")
	@Override
	public Fixture getCurrent(long leagueSeasonId, Date date) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Fixture.class);
		criteria.createAlias("leagueSeason", "s").add(Restrictions.eq("s.id", leagueSeasonId));
		criteria.add(Restrictions.le("startDate", date));
		criteria.add(Restrictions.ge("endDate", date));
		List<Fixture> fixtures =  criteria.list();
		if (fixtures.size() > 0) {
			return fixtures.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Fixture getClosest(long leagueSeasonId, Date date) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Fixture.class);
		criteria.createAlias("leagueSeason", "s").add(Restrictions.eq("s.id", leagueSeasonId));
		criteria.add(Restrictions.le("startDate", date));
		criteria.addOrder(Order.desc("startDate"));
		criteria.setMaxResults(1); //only care about the closest fixture
		List<Fixture> fixtures =  criteria.list();
		if (fixtures.size() > 0) {
			return fixtures.get(0);
		} else {
			return null;
		}
	}

	@Override
	public void saveOrUpdate(Fixture fixture) {
		sessionFactory.getCurrentSession().saveOrUpdate(fixture);		
	}
}