package com.africaapps.league.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.africaapps.league.dao.game.UserDao;
import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.game.User;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;
	
	@Override
	public User getUser(String username, String password) throws LeagueException {
		if (username != null) {
			return userDao.getUser(username, password);			
		} else {
			return null;
		}
	}

	@Override
	public void saveUser(User user) throws LeagueException {
		if (user != null) {
			if (user.getId() == null) {
				if (!userDao.isExistingUsername(user.getUsername())) {
					userDao.saveOrUpdate(user);
				} else {
					throw new LeagueException("Dupliace username: "+user.getUsername());
				}
			} else {
				userDao.saveOrUpdate(user);
			}
		}
	}
}
