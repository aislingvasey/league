package com.africaapps.league.service.player;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.africaapps.league.dao.league.EventDao;
import com.africaapps.league.dao.league.PlayerDao;
import com.africaapps.league.dao.league.PlayerMatchEventDao;
import com.africaapps.league.dao.league.PositionDao;
import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.league.BlockType;
import com.africaapps.league.model.league.Event;
import com.africaapps.league.model.league.Player;
import com.africaapps.league.model.league.PlayerMatchEvent;
import com.africaapps.league.model.league.Position;
import com.africaapps.league.service.transaction.ReadTransaction;
import com.africaapps.league.service.transaction.WriteTransaction;

@Service
public class PlayerServiceImpl implements PlayerService {

	@Autowired
	private PlayerDao playerDao;
	@Autowired
	private PositionDao positionDao;
	@Autowired
	private EventDao eventDao;
	@Autowired
	private PlayerMatchEventDao playerMatchEventDao;

	private static Logger logger = LoggerFactory.getLogger(PlayerServiceImpl.class);
	
	@ReadTransaction
	@Override
	public Player getPlayer(int playerId) throws LeagueException {
		return playerDao.getByPlayerId(playerId);
	}

	@WriteTransaction
	@Override
	public void savePlayer(Player player) throws LeagueException {
		if (player != null) {
			player.setId(playerDao.getIdByPlayerId(player.getPlayerId()));
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

	@ReadTransaction
	@Override
	public Event getEvent(long leagueTypeId, int eventId) throws LeagueException {
		return eventDao.getEvent(leagueTypeId, eventId);
	}

	@WriteTransaction
	@Override
	public void saveEvent(Event event) throws LeagueException {
		Event existing = eventDao.getEvent(event.getLeagueType().getId(), event.getEventId());
		if (existing == null) {
			eventDao.saveOrUpdate(event);
		} else {
			logger.info("Not saving existing event: "+event);
		}		
	}

	@WriteTransaction
	@Override
	public void savePlayerMatchStats(PlayerMatchEvent playerMatchEvent) throws LeagueException {		
		PlayerMatchEvent existing = playerMatchEventDao.getEvent(playerMatchEvent.getPlayerMatch().getId(), 
																														 playerMatchEvent.getEvent().getId(), 
																														 playerMatchEvent.getMatchTime());
		if (existing == null) {
			playerMatchEventDao.saveOrUpdate(playerMatchEvent);
			logger.debug("Saved PlayerMatchEvent: "+playerMatchEvent);
		} else {
			logger.debug("Not saving existing PlayerMatchEvent: "+existing);
		}
	}

	@Override
	public List<Player> getTeamPlayersByType(long teamId, String type) {
		BlockType blockType = getBlockType(type);
		return playerDao.getByTeamIdAndPlayerType(teamId, blockType);
	}
	
	private BlockType getBlockType(String type) {
		for(BlockType bt : BlockType.values()) {
			if (bt.name().equalsIgnoreCase(type)) {
				return bt;
			}
		}
		logger.error("Unknown BlockType: "+type);
		return null;
	}
}
