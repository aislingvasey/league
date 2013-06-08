package com.africaapps.league.dao.game;

import com.africaapps.league.model.game.User;

public interface UserDao {

	public User getUser(String username, String password);
	public User getUser(Long userId);
	public Long getUserId(String username);
	
	public boolean isExistingUsername(String username);
	
	public void saveOrUpdate(User user);
	
}
