package com.africaapps.league.dao.league.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.africaapps.league.dao.hibernate.BaseHibernateDao;
import com.africaapps.league.dao.league.LeagueSeasonDao;
import com.africaapps.league.model.league.LeagueSeason;
import com.africaapps.league.model.league.LeagueSeasonStatus;

@Repository
public class LeagueSeasonDaoImpl extends BaseHibernateDao implements LeagueSeasonDao {

	@Override
	public void saveOrUpdate(LeagueSeason leagueSeason) {
		if (leagueSeason != null) {
			sessionFactory.getCurrentSession().saveOrUpdate(leagueSeason);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public LeagueSeason getCurrentSeason(long leagueId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(LeagueSeason.class);
		criteria.createAlias("league", "l").add(Restrictions.eq("l.id", leagueId));
		criteria.add(Restrictions.eq("status", LeagueSeasonStatus.CURRENT));
		List<LeagueSeason> seasons = criteria.list();
		if (seasons.size() == 1) {
			return seasons.get(0);
		} else {
			return null;
		}
	}
}
