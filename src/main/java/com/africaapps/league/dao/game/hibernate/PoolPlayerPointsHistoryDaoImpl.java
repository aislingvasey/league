package com.africaapps.league.dao.game.hibernate;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.africaapps.league.dao.game.PoolPlayerPointsHistoryDao;
import com.africaapps.league.dao.hibernate.BaseHibernateDao;
import com.africaapps.league.dto.PlayerMatchEventSummary;
import com.africaapps.league.dto.PlayerMatchSummary;
import com.africaapps.league.model.game.PoolPlayerPointsHistory;

@Repository
public class PoolPlayerPointsHistoryDaoImpl extends BaseHibernateDao implements PoolPlayerPointsHistoryDao {
	
	private static final String MATCHES_HISTORY = 
			"select p.first_name, p.last_name, p.block, m.id, m.start_date_time, h.player_points " 
     +" from game_pool_player_points_history h, game_pool_player gpp, player p, match m"
     +" where h.pool_player_id = gpp.id and gpp.player_id = p.id and h.match_id = m.id"
     +" and gpp.id = :ppId"
     +" order by h.added_date_time";
	
	private static final String MATCH_EVENTS_HISTORY 
		= "select p.first_name, p.last_name, p.block as PlayerBlock, m.start_date_time, pme.match_time, e.description, e.points, pm.player_score "
+"from event e, player_match_event pme, player_match pm, player p, game_pool_player gpp, match m "
+"where e.id = pme.event_id and pme.player_match_id = pm.id and pm.player_id = p.id and p.id = gpp.id and gpp.id = :poolPlayerId and pm.match_id = :matchId and pm.match_id = m.id "
+" and e.points != 0 "
+"order by pme.match_time";

	@Override
	public void save(PoolPlayerPointsHistory history) {
		if (history != null) {
			sessionFactory.getCurrentSession().save(history);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PlayerMatchSummary> getHistoryForPlayer(long poolPlayerId) {
		List<PlayerMatchSummary> summaries = new ArrayList<PlayerMatchSummary>();
		Query query = sessionFactory.getCurrentSession().createSQLQuery(MATCHES_HISTORY);
		query.setLong("ppId", poolPlayerId);
		List<Object[]> matches = query.list();
		PlayerMatchSummary match = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		for(Object[] history : matches) {
			match = new PlayerMatchSummary();
			match.setFirstName((String) history[0]);
			match.setLastName((String) history[1]);
			match.setPlayerBlock((String) history[2]);
			match.setMatchId(((BigInteger) history[3]).longValue());
			match.setMatchDate(sdf.format((Date) history[4]));
			match.setPlayerPoints((Integer) history[5]);
			match.setPoolPlayerId(poolPlayerId);
			summaries.add(match);
		}
		return summaries;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PlayerMatchEventSummary> getEventsForPlayer(long poolPlayerId, long matchId) {
		List<PlayerMatchEventSummary> summaries = new ArrayList<PlayerMatchEventSummary>();
		Query query = sessionFactory.getCurrentSession().createSQLQuery(MATCH_EVENTS_HISTORY);
		query.setLong("poolPlayerId", poolPlayerId);
		query.setLong("matchId", matchId);		
		List<Object[]> matches = query.list();
		PlayerMatchEventSummary match = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		for(Object[] history : matches) {
			match = new PlayerMatchEventSummary();
			match.setFirstName((String) history[0]);
			match.setLastName((String) history[1]);
			match.setPlayerBlock((String) history[2]);
			match.setMatchId(matchId);
			match.setMatchDate(sdf.format((Date) history[3]));
			match.setMatchTime((String) history[4]);			
			match.setDescription((String) history[5]);
			match.setEventPoints((Integer) history[6]);			
			match.setPoolPlayerId(poolPlayerId);
			match.setMatchPoints((Integer) history[7]);
			summaries.add(match);
		}
		return summaries;
	}
}
