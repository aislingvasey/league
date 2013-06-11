package com.africaapps.league.dao.league.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.africaapps.league.dao.hibernate.BaseHibernateDao;
import com.africaapps.league.dao.league.PlayerDao;
import com.africaapps.league.model.league.BlockType;
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

	@SuppressWarnings("unchecked")
	@Override
	public Long getIdByPlayerId(int playerId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Player.class);
		criteria.add(Restrictions.eq("playerId", playerId));
		criteria.setProjection(Projections.property("id"));
		List<Long> ids = criteria.list();
		if (ids.size() == 1) {
			return ids.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Player> getByTeamIdAndPlayerType(long teamId, BlockType blockType) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Player.class);
		criteria.add(Restrictions.eq("block", blockType));
		criteria.createAlias("team", "t").add(Restrictions.eq("t.id", teamId));
		return criteria.list();
	}
}
