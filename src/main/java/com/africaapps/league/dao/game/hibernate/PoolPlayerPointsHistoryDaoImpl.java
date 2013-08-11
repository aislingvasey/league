package com.africaapps.league.dao.game.hibernate;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.africaapps.league.dao.game.PoolPlayerPointsHistoryDao;
import com.africaapps.league.dao.hibernate.BaseHibernateDao;
import com.africaapps.league.dto.PlayerMatchStatisticSummary;
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
	
	private static final String MATCH_STATS_HISTORY 
		= "select p.first_name, p.last_name, p.block as PlayerBlock, m.start_date_time, s.name, s.points, pms.points as total, pm.player_score, t1.club_name as t1, t2.club_name as t2 " 
     +" from statistic s, player_match_statistic pms, player_match pm, player p, game_pool_player gpp, match m, team t1, team t2 " 
     +" where s.id = pms.statistic_id and pms.player_match_id = pm.id and pm.player_id = p.id and p.id = gpp.id and gpp.id = :poolPlayerId and pm.match_id = :matchId and pm.match_id = m.id and m.team1_id = t1.id and m.team2_id = t2.id " 
     +" order by pms.id";

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
			match.setPlayerBlock(formatBlock((String) history[2]));
			match.setMatchId(((BigInteger) history[3]).longValue());
			match.setMatchDate(sdf.format((Date) history[4]));
			match.setPlayerPoints((Integer) history[5]);
			match.setPoolPlayerId(poolPlayerId);
			summaries.add(match);
		}
		return summaries;
	}
	
	private String formatBlock(String b) {
		if (b != null && !b.trim().equals("") && b.length() > 1) {
			return b.substring(0, 1) + b.substring(1).toLowerCase();
		} else {
			return b;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PlayerMatchStatisticSummary> getStatsForPlayer(long poolPlayerId, long matchId) {
		List<PlayerMatchStatisticSummary> summaries = new ArrayList<PlayerMatchStatisticSummary>();
		Query query = sessionFactory.getCurrentSession().createSQLQuery(MATCH_STATS_HISTORY);
		query.setLong("poolPlayerId", poolPlayerId);
		query.setLong("matchId", matchId);		
		List<Object[]> matches = query.list();
		PlayerMatchStatisticSummary match = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		for(Object[] history : matches) {
			match = new PlayerMatchStatisticSummary();
			match.setFirstName((String) history[0]);
			match.setLastName((String) history[1]);
			match.setPlayerBlock(formatBlock((String) history[2]));
			match.setMatchId(matchId);
			match.setMatchDate(sdf.format((Date) history[3]));
			match.setStatName((String) history[4]);
			if (history[5] instanceof Integer) {
				match.setStatPoints(((Integer) history[5]).doubleValue());
			} else {
				match.setStatPoints((Double) history[5]);
			}
			if (history[6] instanceof Integer) {
				match.setStatTotal(((Integer) history[6]).doubleValue());			
			} else {
				match.setStatTotal((Double) history[6]);
			}
			match.setPoolPlayerId(poolPlayerId);
			if (history[7] instanceof Integer) {
				match.setMatchPoints(((Integer) history[7]).doubleValue());
			} else {
				match.setMatchPoints((Double) history[7]);
			}
			match.setTeamOne((String) history[8]);
			match.setTeamTwo((String) history[9]);
			summaries.add(match);
		}
		return summaries;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PoolPlayerPointsHistory> getForPlayingWeek(long poolPlayerId, long currentPlayingWeekId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(PoolPlayerPointsHistory.class);
		criteria.createAlias("poolPlayer", "pp").add(Restrictions.eq("pp.id", poolPlayerId));
		criteria.createAlias("playingWeek", "pw").add(Restrictions.eq("pw.id", currentPlayingWeekId));
		return criteria.list();
	}
}