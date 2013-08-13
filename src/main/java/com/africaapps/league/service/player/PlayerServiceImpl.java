package com.africaapps.league.service.player;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.africaapps.league.dao.league.PlayerDao;
import com.africaapps.league.dao.league.PositionDao;
import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.league.BlockType;
import com.africaapps.league.model.league.Player;
import com.africaapps.league.model.league.PlayerMatchStatistic;
import com.africaapps.league.model.league.Position;
import com.africaapps.league.service.match.MatchService;
import com.africaapps.league.service.pool.PoolService;
import com.africaapps.league.service.transaction.ReadTransaction;
import com.africaapps.league.service.transaction.WriteTransaction;

@Service
public class PlayerServiceImpl implements PlayerService {

	@Autowired
	private PlayerDao playerDao;
	@Autowired
	private PositionDao positionDao;
	@Autowired
	private MatchService matchService;
	@Autowired
	private PoolService poolService;
	
	private static Logger logger = LoggerFactory.getLogger(PlayerServiceImpl.class);

	@ReadTransaction
	@Override
	public Player getPlayer(int playerId) throws LeagueException {
		return playerDao.getByPlayerId(playerId);
	}

	@ReadTransaction
	@Override
	public Player getPlayer(long id) throws LeagueException {
		return playerDao.getPlayer(id);
	}

	@WriteTransaction
	@Override
	public void savePlayer(Player player) throws LeagueException {
		if (player != null) {
			boolean existingPlayer = false;
//			Long existingId = playerDao.getIdByPlayerId(player.getPlayerId());
			Object[] info = playerDao.getInfoByPlayerId(player.getPlayerId());
			if (info != null) {
				if (info[0] != null) {
					player.setId((Long) info[0]);
					logger.info("Set exiting playerId: "+info[0]);
					existingPlayer = true;
				}				
			}
			if (player.getBlock() == null && existingPlayer) {
				logger.error("Not re-saving existing player with null block: "+player);
				return;
			} else if (player.getBlock() == null && !existingPlayer) {
				logger.info("Trying to get block for new incoming player");
				player.setBlock(poolService.getPlayerBlock(player));
				logger.info("Set block ? "+player.getBlock());
			} else if (player.getBlock() != null && existingPlayer) {
				if (info[1] != null && !player.getBlock().equals((BlockType) info[1])) {
					logger.warn("Warning: changing existing player's block from: "+info[1]+" to: "+player.getBlock());
//					player.setBlock((BlockType) info[1]);
//					logger.info("Set old block again for incoming player: "+player.getBlock());
				}
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

	@WriteTransaction
	@Override
	public void savePlayerMatchStatistic(long leagueTypeId, PlayerMatchStatistic playerMatchStatistic) throws LeagueException {
		if (playerMatchStatistic != null) {
			PlayerMatchStatistic existing = matchService.getPlayerMatchStatistic(playerMatchStatistic.getPlayerMatch().getId(), 
					playerMatchStatistic.getStatistic().getId());
			if (existing == null) {
				matchService.savePlayerMatchStatistic(playerMatchStatistic);
				logger.info("Saved: "+playerMatchStatistic);
			} else {
				logger.debug("Not saving existing PlayerMatchStatistic: " + existing);
			}
		}
	}

	@ReadTransaction
	@Override
	public List<Player> getTeamPlayersByType(long teamId, String type) {
		BlockType blockType = getBlockType(type);
		return playerDao.getByTeamIdAndPlayerType(teamId, blockType);
	}

	private BlockType getBlockType(String type) {
		for (BlockType bt : BlockType.values()) {
			if (bt.name().equalsIgnoreCase(type)) {
				return bt;
			}
		}
		logger.error("Unknown BlockType: " + type);
		return null;
	}

	@ReadTransaction
	@Override
	public List<Player> getTeamPlayers(long teamId) {
		return playerDao.getByTeamId(teamId);
	}

	@ReadTransaction
	@Override
	public List<Player> getTeamPlayers(int teamId) {
		return playerDao.getTeamPlayers(teamId);
	}
}
