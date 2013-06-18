package com.africaapps.league.dao.game;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.africaapps.league.dao.hibernate.BaseHibernateDao;
import com.africaapps.league.model.game.UserPlayer;

@Repository
public class UserPlayerDaoImpl extends BaseHibernateDao implements UserPlayerDao {

	@SuppressWarnings("unchecked")
	@Override
	public UserPlayer getUserPlayer(long userTeamId, long playerId) {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(UserPlayer.class);
			criteria.createAlias("userTeam", "t").add(Restrictions.eq("t.id", userTeamId));
			criteria.createAlias("poolPlayer", "pp").add(Restrictions.eq("pp.player.id", playerId));
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
}
