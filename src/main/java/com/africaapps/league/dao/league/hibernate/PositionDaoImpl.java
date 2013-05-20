package com.africaapps.league.dao.league.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.africaapps.league.dao.hibernate.BaseHibernateDao;
import com.africaapps.league.dao.league.PositionDao;
import com.africaapps.league.model.league.Position;

@Repository
public class PositionDaoImpl extends BaseHibernateDao implements PositionDao {

	@Override
	public void saveAndUpdate(Position position) {
		if (position != null) {
			sessionFactory.getCurrentSession().saveOrUpdate(position);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Position getPosition(long leagueTypeId, int positionNumber) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Position.class);
		criteria.add(Restrictions.eq("positionNumber", positionNumber));
		criteria.createAlias("leagueType", "t").add(Restrictions.eqOrIsNull("t.id", leagueTypeId));
		List<Position> positions = criteria.list();
		if (positions.size() == 1) {
			return positions.get(0);
		}
		return null;
	}
}