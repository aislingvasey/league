package com.africaapps.league.service.player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.africaapps.league.dao.league.PlayerDao;
import com.africaapps.league.dao.league.PositionDao;
import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.league.Player;
import com.africaapps.league.model.league.Position;
import com.africaapps.league.service.transaction.ReadTransaction;
import com.africaapps.league.service.transaction.WriteTransaction;

@Service
public class PlayerServiceImpl implements PlayerService {

	@Autowired
	private PlayerDao playerDao;
	@Autowired
	private PositionDao positionDao;
	
	@ReadTransaction
	@Override
	public Player getPlayer(int playerId) throws LeagueException {
		return playerDao.getByPlayerId(playerId);
	}

	@WriteTransaction
	@Override
	public void savePlayer(Player player) throws LeagueException {
		if (player != null) {
			Player existingPlayer = playerDao.getByPlayerId(player.getPlayerId());
			if (existingPlayer != null) {
				player.setId(existingPlayer.getId());
			}
			playerDao.saveOrUpdate(player);
		}
	}

	@ReadTransaction
	@Override
	public Position getPosition(long leagueTypeId, int positionNumber) throws LeagueException {
		return positionDao.getPosition(leagueTypeId, positionNumber);
	}

	@WriteTransaction
	@Override
	public void savePosition(Position position) throws LeagueException {
		if (position != null) {
			positionDao.saveAndUpdate(position);
		}
	}
}
