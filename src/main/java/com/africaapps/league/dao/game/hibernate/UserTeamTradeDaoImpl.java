package com.africaapps.league.dao.game.hibernate;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.africaapps.league.dao.game.UserTeamTradeDao;
import com.africaapps.league.dao.hibernate.BaseHibernateDao;
import com.africaapps.league.model.game.UserTeamTrade;

@Repository
public class UserTeamTradeDaoImpl extends BaseHibernateDao implements UserTeamTradeDao {

	@Override
	public void save(UserTeamTrade userTeamTrade) {
		if (userTeamTrade != null) {
			save(userTeamTrade);
		}
	}

	@Override
	public int getUserTeamTradeInWeeks(long leagueSeasonId, long userTeamId, Integer[] playingWeekOrders) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(UserTeamTrade.class);
		criteria.createAlias("userTeam", "team").add(Restrictions.eq("team.id", userTeamId));
		criteria.createAlias("playingWeek", "week").add(
				Restrictions.and(Restrictions.eq("week.leagueSeason.id", leagueSeasonId), Restrictions.in("week.order", playingWeekOrders)));
		criteria.setProjection(Projections.rowCount());
		return ((Number) criteria.uniqueResult()).intValue();
	}
}
