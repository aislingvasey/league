package com.africaapps.league.dao.league.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.africaapps.league.dao.hibernate.BaseHibernateDao;
import com.africaapps.league.dao.league.PlayerDao;
import com.africaapps.league.model.league.Player;

@Repository
public class PlayerDaoImpl extends BaseHibernateDao implements PlayerDao {

	@Override
	public void saveOrUpdate(Player player) {
		if (player != null) {
			sessionFactory.getCurrentSession().saveOrUpdate(player);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Player getByPlayerId(int playerId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Player.class);
		criteria.add(Restrictions.eq("playerId", playerId));
		List<Player> players = criteria.list();
		if (players.size() == 1) {
			return players.get(0);
		} else {
			return null;
		}
	}
}
