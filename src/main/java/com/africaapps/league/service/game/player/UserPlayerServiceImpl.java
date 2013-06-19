package com.africaapps.league.service.game.player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.africaapps.league.dao.game.UserPlayerDao;
import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.game.UserPlayer;
import com.africaapps.league.model.game.UserPlayerStatus;
import com.africaapps.league.service.transaction.ReadTransaction;
import com.africaapps.league.service.transaction.WriteTransaction;

@Service
public class UserPlayerServiceImpl implements UserPlayerService {

	@Autowired
	private UserPlayerDao userPlayerDao;
	
	@WriteTransaction
	@Override
	public void saveUserPlayer(UserPlayer userPlayer) throws LeagueException {
		userPlayerDao.saveOrUpdate(userPlayer);		
	}

	@ReadTransaction
	@Override
	public UserPlayer getPlayerOnUserTeam(long userTeamId, long playerId) throws LeagueException {
		return userPlayerDao.getUserPlayer(userTeamId, playerId);
	}

	@Override
	public UserPlayer getCaptain(long userTeamId) throws LeagueException {
		return userPlayerDao.getPlayerByStatus(userTeamId, UserPlayerStatus.CAPTAIN);
	}
}
