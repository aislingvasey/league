package com.africaapps.league.dao.game.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.africaapps.league.dao.game.PlayingWeekDao;
import com.africaapps.league.dao.hibernate.BaseHibernateDao;
import com.africaapps.league.model.game.PlayingWeek;

@Repository
public class PlayingWeekDaoImpl extends BaseHibernateDao implements PlayingWeekDao {

	@SuppressWarnings("unchecked")
	@Override
	public PlayingWeek get(Long leagueSeasonId, Date matchDateTime) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(PlayingWeek.class);
		criteria.createAlias("leagueSeason", "s").add(Restrictions.eq("s.id", leagueSeasonId));
		criteria.add(Restrictions.le("start", matchDateTime));
		criteria.add(Restrictions.ge("end", matchDateTime));
		List<PlayingWeek> weeks = criteria.list();
		if (weeks.size() > 0) {
			return weeks.get(0);
		} else {
			return null;
		}
	}
}
