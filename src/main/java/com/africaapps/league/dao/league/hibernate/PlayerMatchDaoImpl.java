package com.africaapps.league.dao.league.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.africaapps.league.dao.hibernate.BaseHibernateDao;
import com.africaapps.league.dao.league.PlayerMatchDao;
import com.africaapps.league.model.league.PlayerMatch;

@Repository
public class PlayerMatchDaoImpl extends BaseHibernateDao implements PlayerMatchDao {

	@Override
	public void saveOrUpdate(PlayerMatch playerMatch) {
		if (playerMatch != null) {
			sessionFactory.getCurrentSession().saveOrUpdate(playerMatch);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public PlayerMatch getByIds(long matchId, long playerId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(PlayerMatch.class);
		criteria.createAlias("match", "m").add(Restrictions.eq("m.id", matchId));
		criteria.createAlias("player", "p").add(Restrictions.eq("p.id", playerId));
		List<PlayerMatch> playerMatches = criteria.list();
		if (playerMatches.size() == 1) {
			return playerMatches.get(0);
		}
		return null;
	}
}
