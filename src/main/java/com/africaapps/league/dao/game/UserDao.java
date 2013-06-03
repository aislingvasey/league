package com.africaapps.league.dao.game;

import com.africaapps.league.model.game.User;

public interface UserDao {

	public User getUser(String username, String password);
	
	public boolean isExistingUsername(String username);
	
	public void saveOrUpdate(User user);
	
}
