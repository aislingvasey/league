package com.africaapps.league.dao.game.hibernate;

import org.springframework.stereotype.Repository;

import com.africaapps.league.dao.game.UserTeamScoreHistoryDao;
import com.africaapps.league.dao.hibernate.BaseHibernateDao;
import com.africaapps.league.model.game.UserTeamScoreHistory;

@Repository
public class UserTeamScoreHistoryDaoImpl extends BaseHibernateDao implements UserTeamScoreHistoryDao {

	@Override
	public void save(UserTeamScoreHistory userTeamScoreHistory) {
		if (userTeamScoreHistory != null) {
			sessionFactory.getCurrentSession().save(userTeamScoreHistory);
		}
	}
}
