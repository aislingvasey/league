package com.africaapps.league.dao.game.hibernate;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.africaapps.league.dao.game.UserTeamDao;
import com.africaapps.league.dao.hibernate.BaseHibernateDao;
import com.africaapps.league.dto.TeamSummary;
import com.africaapps.league.dto.UserTeamScoreHistorySummary;
import com.africaapps.league.model.game.UserPlayerStatus;
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
	
	private static final String ADD_PLAYER_POINTS = "UPDATE game_user_team "
			+"SET current_score = current_score + :playerPoints WHERE id in ( :ids )";
	
	private static final String SCORE_HISTORY_BY_MATCH 
		= "select t.name, t.current_score, m.id as matchid, m.start_date_time, sum(h.player_points)" 
     +" from game_user_team_score_history h left join match m on h.match_id = m.id left join game_user_team t on h.user_team_id = t.id"
     +" where h.user_team_id = :userTeamId group by m.id, t.id order by m.start_date_time";
	
	private static final String PLAYERS_SCORE_HISTORY_BY_MATCH 
	= "select t.name, t.current_score, m.id as matchid, m.start_date_time, p.first_name, p.last_name, p.team_id, h.player_points" 
   +" from game_user_team_score_history h, match m , game_pool_player gpp, player p, game_user_team t"
   +" where h.match_id = m.id and h.pool_player_id = gpp.id and gpp.player_id = p.id and h.user_team_id = t.id"
   +" and m.id = :matchId and t.id = :userTeamId"
   +" order by p.first_name, p.last_name";
	
	private static final String POOL_ID = "select l.pool_id"
			+" from game_user_team t left join game_user_league l on t.user_league_id = l.id"
			+" where t.id = :userTeamId";
	
	private static Logger logger = LoggerFactory.getLogger(UserTeamDaoImpl.class);
	
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
	public UserTeam getTeam(long userTeamId) {
		return (UserTeam) sessionFactory.getCurrentSession().get(UserTeam.class, userTeamId);
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

	@Override
	public UserTeam getTeamWithPlayers(long teamId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(UserTeam.class)
    .add(Restrictions.idEq(teamId))
    .setFetchMode("players", FetchMode.JOIN); 
		return (UserTeam) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserTeam> getTeamsWithPoolPlayer(long poolPlayerId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(UserTeam.class);
		criteria.createAlias("userPlayers", "uplayers");
		//don't include the player if they are currently a substitute
		criteria.add(Restrictions.ne("uplayers.status", UserPlayerStatus.SUBSTITUTE));
		criteria.createAlias("uplayers.poolPlayer", "p").add(Restrictions.eq("p.id", poolPlayerId));
		return criteria.list();
	}

	@Override
	public void addPlayerPoints(List<Long> ids, int playerPoints) {
		logger.info("Added playerPoints to teams - points:"+playerPoints+" teams:"+ids.toString());
		Query query = sessionFactory.getCurrentSession().createSQLQuery(ADD_PLAYER_POINTS);
		query.setParameterList("ids", ids);
		query.setInteger("playerPoints", playerPoints);
		int rowsUpdated = query.executeUpdate();
		logger.info("Actual teams updated: "+rowsUpdated);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserTeamScoreHistorySummary> getScoreHistoryByMatch(long userTeamId) {
		List<UserTeamScoreHistorySummary> scores = new ArrayList<UserTeamScoreHistorySummary>();
		Query query = sessionFactory.getCurrentSession().createSQLQuery(SCORE_HISTORY_BY_MATCH);
		query.setLong("userTeamId", userTeamId);
		List<Object[]> matchScores = query.list();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		UserTeamScoreHistorySummary summary = null;
		for(Object[] matchScore : matchScores) {
			summary = new UserTeamScoreHistorySummary();
			summary.setTeamName((String) matchScore[0]);
			summary.setTeamCurrentScore((Integer) matchScore[1]);
			summary.setMatchId(((BigInteger) matchScore[2]).longValue());
			summary.setMatchDate(sdf.format((Date) matchScore[3]));
			summary.setMatchPoints(((BigInteger) matchScore[4]).intValue());
			scores.add(summary);
		}
		return scores;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserTeamScoreHistorySummary> getPlayersScoreHistoryByMatch(Long userTeamId, Long matchId) {
		List<UserTeamScoreHistorySummary> scores = new ArrayList<UserTeamScoreHistorySummary>();
		Query query = sessionFactory.getCurrentSession().createSQLQuery(PLAYERS_SCORE_HISTORY_BY_MATCH);
		query.setLong("userTeamId", userTeamId);
		query.setLong("matchId", matchId);
		List<Object[]> matchScores = query.list();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		UserTeamScoreHistorySummary summary = null;
		for(Object[] matchScore : matchScores) {
			summary = new UserTeamScoreHistorySummary();
			summary.setTeamName((String) matchScore[0]);
			summary.setTeamCurrentScore((Integer) matchScore[1]);
			summary.setMatchId(((BigInteger) matchScore[2]).longValue());
			summary.setMatchDate(sdf.format((Date) matchScore[3]));
			summary.setPlayerFirstName((String) matchScore[4]);
			summary.setPlayerLastName((String) matchScore[5]);
			summary.setPlayerTeamId(((BigInteger) matchScore[6]).longValue());
			summary.setPlayerPoints(((Integer) matchScore[7]));
			scores.add(summary);
		}
		return scores;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Long getTeamPoolId(Long userTeamId) {
		Query query = sessionFactory.getCurrentSession().createSQLQuery(POOL_ID);
		query.setLong("userTeamId", userTeamId);
		List<BigInteger> ids = query.list();
		if (ids.size() > 0) {
			return ids.get(0).longValue();
		} else {
			return null;
		}
	}
}