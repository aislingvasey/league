package com.africaapps.league.dao.game.hibernate;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.africaapps.league.dao.game.UserTeamScoreHistoryDao;
import com.africaapps.league.dao.hibernate.BaseHibernateDao;
import com.africaapps.league.dto.NeededPlayer;
import com.africaapps.league.model.game.UserTeamScoreHistory;

@Repository
public class UserTeamScoreHistoryDaoImpl extends BaseHibernateDao implements UserTeamScoreHistoryDao {
	
	//Gets the userTeamId and the number of needed players for a specific playing week
	private static final String INCOMPLETE_USER_TEAM_IDS 
		= "select user_team_id, 11 - playerCount as needed from ( "
    +" select user_team_id, count(1) as playerCount "
    +" from game_user_team_score_history "
    +" where playing_week_id = :playingWeekId and captain_extra_score = false group by user_team_id) t " 
    +" where t.playerCount < 11 ";

	@Override
	public void save(UserTeamScoreHistory userTeamScoreHistory) {
		if (userTeamScoreHistory != null) {
			sessionFactory.getCurrentSession().save(userTeamScoreHistory);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NeededPlayer> getIncompleteTeams(long playingWeekId) {
		Query query = sessionFactory.getCurrentSession().createSQLQuery(INCOMPLETE_USER_TEAM_IDS);
		query.setLong("playingWeekId", playingWeekId);
		List<NeededPlayer> needed = new ArrayList<NeededPlayer>();
		List<Object[]> teams = query.list();
		for(Object[] team : teams) {
			needed.add(new NeededPlayer(((BigInteger) team[0]).longValue(), ((BigInteger) team[1]).intValue()));
		}
		return needed;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserTeamScoreHistory> getScoreHistory(long userTeamId, long playerWeekId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(UserTeamScoreHistory.class);
		criteria.createAlias("userTeam", "t").add(Restrictions.eq("t.id", userTeamId));
		criteria.createAlias("playingWeek", "w").add(Restrictions.eq("w.id", playerWeekId));
		return criteria.list();
	}
}
