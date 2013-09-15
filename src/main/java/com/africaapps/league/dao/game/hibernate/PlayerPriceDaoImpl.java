package com.africaapps.league.dao.game.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.africaapps.league.dao.game.PlayerPriceDao;
import com.africaapps.league.dao.hibernate.BaseHibernateDao;
import com.africaapps.league.model.game.PlayerPrice;

@Repository
public class PlayerPriceDaoImpl extends BaseHibernateDao implements PlayerPriceDao {

	private static Logger logger = LoggerFactory.getLogger(PlayerPriceDaoImpl.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public PlayerPrice getPrice(String firstName, String lastName) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(PlayerPrice.class);
		if (firstName != null) {
			criteria.add(Restrictions.like("firstName", firstName+"%").ignoreCase());
		} else {
			criteria.add(Restrictions.like("firstName", firstName).ignoreCase());
		}
		criteria.add(Restrictions.eq("lastName", lastName).ignoreCase());
		criteria.addOrder(Order.desc("price"));
		List<PlayerPrice> prices = criteria.list();
		if (prices.size() > 0) {
			return prices.get(0);
		} else {
			int space = firstName.indexOf(" ");
			if (space > -1) {
				String onlyFirstName = firstName.substring(0, space);
				logger.warn("Trying with onlyFirstName: "+onlyFirstName+" lastName: "+lastName);
				return getPrice(onlyFirstName, lastName);
			} else {
				logger.error("Unknown Player: "+firstName+" "+lastName);
				return null;
			}
		}
	}
}
