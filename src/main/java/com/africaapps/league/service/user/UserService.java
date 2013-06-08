package com.africaapps.league.service.user;

import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.game.User;

public interface UserService {
		
	public User getUser(String username, String password) throws LeagueException;
	
	public User getUser(Long userId) throws LeagueException;
	
	public Long getUserId(String username) throws LeagueException;
	
	public void saveUser(User user) throws LeagueException;
	
}
