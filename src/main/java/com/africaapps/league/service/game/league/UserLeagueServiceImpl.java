package com.africaapps.league.service.game.league;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.africaapps.league.dao.game.UserLeagueDao;

@Service
public class UserLeagueServiceImpl implements UserLeagueService {

	@Autowired
	private UserLeagueDao userLeagueDao;
	
}
