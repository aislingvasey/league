package com.africaapps.league.dao.league.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.africaapps.league.dao.hibernate.BaseHibernateDao;
import com.africaapps.league.dao.league.EventDao;
import com.africaapps.league.model.league.Event;

@Repository
public class EventDaoImpl extends BaseHibernateDao implements EventDao {
	
	private static Logger logger = LoggerFactory.getLogger(EventDaoImpl.class);

	@Override
	public void saveOrUpdate(Event event) {
		if (event != null) {
			sessionFactory.getCurrentSession().saveOrUpdate(event);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Event> getEvents(long leagueTypeId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Event.class);
		criteria.createAlias("leagueType", "t").add(Restrictions.eq("t.id", leagueTypeId));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Event getEvent(long leagueTypeId, int eventId) {
		logger.info("eventId: "+eventId+" leagueTypeId: "+leagueTypeId);
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Event.class);
		criteria.add(Restrictions.eq("eventId", eventId));
		criteria.createAlias("leagueType", "t").add(Restrictions.eq("t.id", leagueTypeId));
		List<Event> e = criteria.list();
		logger.info("Got events: "+e.size());
		if (e.size() == 1) {
			return e.get(0);
		}
		return null;
	}
}
