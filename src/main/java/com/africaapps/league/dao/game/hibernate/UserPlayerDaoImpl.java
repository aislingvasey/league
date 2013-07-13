package com.africaapps.league.dao.game.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.africaapps.league.dao.game.UserPlayerDao;
import com.africaapps.league.dao.hibernate.BaseHibernateDao;
import com.africaapps.league.model.game.UserPlayer;
import com.africaapps.league.model.game.UserPlayerStatus;

@Repository
public class UserPlayerDaoImpl extends BaseHibernateDao implements UserPlayerDao {
	
	@SuppressWarnings("unchecked")
	@Override
	public UserPlayer getUserPlayer(long userTeamId, long playerId) {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(UserPlayer.class);
			criteria.createAlias("userTeam", "t").add(Restrictions.eq("t.id", userTeamId));
			criteria.createAlias("poolPlayer", "pp").add(Restrictions.eq("pp.id", playerId));
			List<UserPlayer> players = criteria.list();
			if (players.size() == 1) {
				return players.get(0);
			} else {
				return null;
			}
		}

	@Override
	public void saveOrUpdate(UserPlayer userPlayer) {
		if (userPlayer != null) {
			sessionFactory.getCurrentSession().saveOrUpdate(userPlayer);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public UserPlayer getPlayerByStatus(long userTeamId, UserPlayerStatus status) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(UserPlayer.class);
		criteria.createAlias("userTeam", "t").add(Restrictions.eq("t.id", userTeamId));
		criteria.add(Restrictions.eq("status", status));
		List<UserPlayer> players = criteria.list();
		if (players.size() > 0) {
			return players.get(0);
		} else {
			return null;
		}
	}

	@Override
	public void delete(UserPlayer userPlayer) {
		sessionFactory.getCurrentSession().delete(userPlayer);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserPlayer> getSubstitutes(long userTeamId, int subsCount) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(UserPlayer.class);
		criteria.createAlias("userTeam", "t").add(Restrictions.eq("t.id", userTeamId));
		criteria.add(Restrictions.eq("status", UserPlayerStatus.SUBSTITUTE));
		criteria.addOrder(Order.asc("id"));
		criteria.setMaxResults(subsCount);
		return criteria.list();
	}
}