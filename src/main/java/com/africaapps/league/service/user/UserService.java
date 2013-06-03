package com.africaapps.league.service.user;

import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.game.User;

public interface UserService {

	//TODO
	
	public User getUser(String username) throws LeagueException;
	
	public User getUser(String username, String password) throws LeagueException;
	
	public void saveUser(User user) throws LeagueException;
	
}
