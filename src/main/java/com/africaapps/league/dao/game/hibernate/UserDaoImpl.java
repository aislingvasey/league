package com.africaapps.league.dao.game.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.africaapps.league.dao.game.UserDao;
import com.africaapps.league.dao.hibernate.BaseHibernateDao;
import com.africaapps.league.model.game.User;

@Repository
public class UserDaoImpl extends BaseHibernateDao implements UserDao {

	@SuppressWarnings("unchecked")
	@Override
	public User getUser(String username, String password) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(User.class);
		criteria.add(Restrictions.eq("username", username).ignoreCase());
		if (password != null && !password.trim().equals("")) {
			criteria.add(Restrictions.eq("password", password));
		}
		List<User> users = criteria.list();
		if (users.size() == 1) {
			return users.get(0);
		} else {
			return null;
		}
	}
	
	@Override
	public User getUser(Long userId) {
		return (User) sessionFactory.getCurrentSession().get(User.class, userId);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Long getUserId(String username) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(User.class);
		criteria.add(Restrictions.eq("username", username).ignoreCase());
		criteria.setProjection(Projections.property("id"));
		List<Long> users = criteria.list();
		if (users.size() == 1) {
			return users.get(0);
		} else {
			return null;
		}
	}
	
	@Override
	public boolean isExistingUsername(String username) {
		if (username != null) {
			Query query = sessionFactory.getCurrentSession().getNamedQuery("isExistingUsername").setString("username", username.toLowerCase());
			Long count = (Long) query.uniqueResult();
			if (count > 0) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void saveOrUpdate(User user) {
		if (user != null) {
			sessionFactory.getCurrentSession().saveOrUpdate(user);
		}
	}
}
