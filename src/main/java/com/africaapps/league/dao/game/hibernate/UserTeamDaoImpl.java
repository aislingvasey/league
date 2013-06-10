package com.africaapps.league.dao.game.hibernate;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.africaapps.league.dao.game.UserTeamDao;
import com.africaapps.league.dao.hibernate.BaseHibernateDao;
import com.africaapps.league.dto.TeamSummary;
import com.africaapps.league.model.game.UserTeam;

@Repository
public class UserTeamDaoImpl extends BaseHibernateDao implements UserTeamDao {

	private static final String USER_TEAMS 
	= "SELECT l.id as leagueId, t.id as teamId, t.current_score, t.name, t.user_details_id, u.username "
   +" FROM game_user_league l "
	 +" left join game_user_team t on l.id = t.user_league_id "
   +" left join game_user_details u on t.user_details_id = u.id "
   +" where l.id = :leagueId and t.user_details_id = :userId "
   +" order by t.current_score desc";
	
	@SuppressWarnings("unchecked")
	@Override
	public List<UserTeam> getTeams(long userId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(UserTeam.class);
		criteria.createAlias("user", "u").add(Restrictions.eq("u.id", userId));
		criteria.addOrder(org.hibernate.criterion.Order.asc("name"));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public UserTeam getTeam(long userId, String teamName) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(UserTeam.class);
		criteria.add(Restrictions.eq("name", teamName));	
		criteria.createAlias("user", "u").add(Restrictions.eq("u.id", userId));
		List<UserTeam> teams = criteria.list();
		if (teams.size() > 0) {
			return teams.get(0);
		} else {
			return null;
		}
	}

	@Override
	public void saveOrUpdate(UserTeam userTeam) {
		if (userTeam != null) {
			sessionFactory.getCurrentSession().saveOrUpdate(userTeam);
		}
	}

	@Override
	public int getTeamCount(long userLeagueId) {
		Query query = sessionFactory.getCurrentSession().createSQLQuery("select count(1) from game_user_team t where t.user_league_id = :leagueId");
		query.setLong("leagueId", userLeagueId);
		return ((BigInteger) query.uniqueResult()).intValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TeamSummary> getTeamSummary(long userLeagueId, long userId) {
		List<TeamSummary> summaries = new ArrayList<TeamSummary>();
		Query query = sessionFactory.getCurrentSession().createSQLQuery(USER_TEAMS);
		query.setLong("userId", userId);
		query.setLong("leagueId", userLeagueId);
		TeamSummary summary = null;
		List<Object[]> teams = query.list();
		for(Object[] team : teams) {
			//l.id as leagueId, t.id as teamId, t.current_score, t.name, t.user_details_id, u.username			
			summary = new TeamSummary();
			summary.setLeagueId(((BigInteger) team[0]).longValue());
			summary.setOwnerId(((BigInteger) team[4]).longValue());
			summary.setUsername((String) team[5]);
			summary.setTeamId(((BigInteger) team[1]).longValue());
			summary.setTeamName((String) team[3]);
			summary.setCurrentScore((Integer) team[2]);
			summaries.add(summary);
		}
		return summaries;
	}
}