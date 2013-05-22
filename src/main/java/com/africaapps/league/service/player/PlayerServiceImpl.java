package com.africaapps.league.service.player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.africaapps.league.dao.league.PlayerDao;
import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.league.Player;

@Service
public class PlayerServiceImpl implements PlayerService {

	@Autowired
	private PlayerDao playerDao;
	
	@Transactional(readOnly=true)
	@Override
	public Player getPlayer(Long playerId) throws LeagueException {
		if (playerId != null) {
			return playerDao.getById(playerId);
		} else {
			return null;
		}
	}

	@Override
	public void savePlayer(Player player) throws LeagueException {
		if (player != null) {
			playerDao.saveOrUpdate(player);
		}
	}
}
