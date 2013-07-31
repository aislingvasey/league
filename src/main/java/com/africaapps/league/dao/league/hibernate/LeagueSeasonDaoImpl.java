package com.africaapps.league.dao.league.hibernate;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.africaapps.league.dao.hibernate.BaseHibernateDao;
import com.africaapps.league.dao.league.LeagueSeasonDao;
import com.africaapps.league.model.league.LeagueSeason;
import com.africaapps.league.model.league.LeagueSeasonStatus;

@Repository
public class LeagueSeasonDaoImpl extends BaseHibernateDao implements LeagueSeasonDao {

	private static final String USER_TEAM_QUERY = "select s.id " 
    +" from league_season s , game_user_league ul, game_user_team t "
    +" where s.league_id = ul.league_id and ul.id = t.user_league_id and t.id = :userTeamId " 
    +" and s.status = 'CURRENT'";
	
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

	@Override
	public LeagueSeason getCurrentSeasonForUserTeam(long userTeamId) {
		Query query = sessionFactory.getCurrentSession().createSQLQuery(USER_TEAM_QUERY);
		query.setParameter("userTeamId", userTeamId);
		Long leagueSeasonId = ((BigInteger) query.uniqueResult()).longValue();
		return (LeagueSeason) sessionFactory.getCurrentSession().get(LeagueSeason.class, leagueSeasonId);
	}
}
