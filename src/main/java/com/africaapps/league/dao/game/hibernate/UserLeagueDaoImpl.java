package com.africaapps.league.dao.game.hibernate;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.africaapps.league.dao.game.UserLeagueDao;
import com.africaapps.league.dao.hibernate.BaseHibernateDao;
import com.africaapps.league.dto.TeamSummary;
import com.africaapps.league.model.game.UserLeague;

@Repository
public class UserLeagueDaoImpl extends BaseHibernateDao implements UserLeagueDao {
	
	private static final String LEAGUE_TEAMS 
	= "SELECT l.id as leagueId, t.id as teamId, t.current_score, t.number_of_weeks, t.current_rank, t.name, t.user_details_id, u.username "
   +" FROM game_user_league l "
	 +" left join game_user_team t on l.id = t.user_league_id "
   +" left join game_user_details u on t.user_details_id = u.id "
   +" where l.id = :leagueId and t.status = 'COMPLETE' "
   +" order by t.current_rank desc, t.id asc "
   +" limit :limit offset :offset";

	@Override
	public void saveOrUpdate(UserLeague userLeague) {
		if (userLeague != null) {
			sessionFactory.getCurrentSession().saveOrUpdate(userLeague);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public UserLeague getDefault() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(UserLeague.class);
		criteria.add(Restrictions.eq("defaultUserLeague", true));
		List<UserLeague> leagues = criteria.list();
		if (leagues.size() > 0) {
			return leagues.get(0);
		}
		return null;
	}

	@Override
	public String getLeagueName(long userLeagueId) {
		return (String) sessionFactory.getCurrentSession()
				                          .createCriteria(UserLeague.class)
				                          .add(Restrictions.eq("id", userLeagueId))
				                          .setProjection(Projections.property("name"))
				                          .uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TeamSummary> getLeagueTeamSummary(long userLeagueId, int teamNumber, int start) {
		List<TeamSummary> summaries = new ArrayList<TeamSummary>();
		Query query = sessionFactory.getCurrentSession().createSQLQuery(LEAGUE_TEAMS);
		query.setLong("leagueId", userLeagueId);
		query.setInteger("limit", teamNumber);
		query.setInteger("offset", start);
		TeamSummary summary = null;
		List<Object[]> teams = query.list();
		Object[] team = null;
		for(int i=0;i<teams.size();i++) {
			team = teams.get(i);
			summary = new TeamSummary();
			summary.setPositionInLeague(i+1+start);
			summary.setLeagueId(((BigInteger) team[0]).longValue());
			summary.setTeamId(((BigInteger) team[1]).longValue());
			summary.setCurrentScore((Integer) team[2]);
			summary.setNumberOfWeeks((Integer) team[3]);
			summary.setCurrentRank((Integer) team[4]);
			summary.setTeamName((String) team[5]);
			summary.setOwnerId(((BigInteger) team[6]).longValue());
			summary.setUsername((String) team[7]);
			summaries.add(summary);
		}
		return summaries;
	}
}