package com.africaapps.league.dao.league.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.africaapps.league.dao.hibernate.BaseHibernateDao;
import com.africaapps.league.dao.league.PlayerMatchEventDao;
import com.africaapps.league.model.league.PlayerMatchEvent;

@Repository
public class PlayerMatchEventDaoImpl extends BaseHibernateDao implements PlayerMatchEventDao {

	@Override
	public void saveOrUpdate(PlayerMatchEvent playerMatchEvent) {
		if (playerMatchEvent != null) {
			sessionFactory.getCurrentSession().saveOrUpdate(playerMatchEvent);
		}
	}

	@Override
	public Integer getTotalPoints(long PlayerMatchId) {
		// TODO 
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PlayerMatchEvent> getEvents(long playerMatchId) {		
		return sessionFactory.getCurrentSession().createCriteria(PlayerMatchEvent.class)
				.createAlias("playerMatch", "pm").add(Restrictions.eq("pm.id", playerMatchId))
				.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public PlayerMatchEvent getEvent(Long playerMatchId, Long eventId, String matchTime) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(PlayerMatchEvent.class);
		criteria.createAlias("playerMatch", "pm").add(Restrictions.eq("pm.id", playerMatchId));
		criteria.createAlias("event", "e").add(Restrictions.eq("e.id", eventId));
		criteria.add(Restrictions.eq("matchTime", matchTime));
		List<PlayerMatchEvent> stats = criteria.list();
		if (stats.size() == 1) {
			return stats.get(0);
		} else {
			return null;
		}
	}
}