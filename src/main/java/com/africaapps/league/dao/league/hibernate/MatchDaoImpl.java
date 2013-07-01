package com.africaapps.league.dao.league.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.africaapps.league.dao.hibernate.BaseHibernateDao;
import com.africaapps.league.dao.league.MatchDao;
import com.africaapps.league.model.league.Match;

@Repository
public class MatchDaoImpl extends BaseHibernateDao implements MatchDao {
	
	private static final String CALC_PLAYER_SCORE = "UPDATE player_match SET player_score = t.score"  
+" FROM ("  
+"  select player_match_id, sum(e.points) as score"
+"  from player_match_event pme join event e on pme.event_id = e.id" 
+"  where player_match_id in (select id from player_match where match_id = :matchId )"
+"    group by player_match_id"
+" ) t"  
+" WHERE player_match.id = t.player_match_id";

	private static Logger logger = LoggerFactory.getLogger(MatchDaoImpl.class);
	
	@Override
	public void saveOrUpdate(Match match) {
		if (match != null) {
			sessionFactory.getCurrentSession().saveOrUpdate(match);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Match getByLeagueSeasonAndMatchId(long leagueSeasonId, int matchId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Match.class);
		criteria.add(Restrictions.eq("matchId", matchId));
		criteria.createAlias("leagueSeason", "s").add(Restrictions.eq("s.id", leagueSeasonId));
		List<Match> matches = criteria.list();
		if (matches.size() == 1) {
			return matches.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Long getIdByMatchId(long leagueSeasonId, int matchId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Match.class);
		criteria.add(Restrictions.eq("matchId", matchId));
		criteria.createAlias("leagueSeason", "s").add(Restrictions.eq("s.id", leagueSeasonId));
		criteria.setProjection(Projections.property("id"));
		List<Long> ids = criteria.list();
		if (ids.size() == 1) {
			return ids.get(0).longValue();
		} else {
			return null;
		}
	}

	@Override
	public void calculatePlayerScores(long id) {
		long rows = sessionFactory.getCurrentSession().createSQLQuery(CALC_PLAYER_SCORE).setLong("matchId", id).executeUpdate();
		logger.info("Set "+rows+" players' match scores for match: "+id);
	}

	@Override
	public Match getMatch(long matchId) {
		return (Match) sessionFactory.getCurrentSession().get(Match.class, matchId);
	}
}
