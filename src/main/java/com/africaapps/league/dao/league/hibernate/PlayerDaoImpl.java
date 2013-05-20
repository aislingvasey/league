package com.africaapps.league.dao.league.hibernate;

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

	@Override
	public Player getById(long playerId) {
		return (Player) sessionFactory.getCurrentSession().get(Player.class, playerId);
	}
}
