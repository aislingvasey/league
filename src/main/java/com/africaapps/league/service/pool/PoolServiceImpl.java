package com.africaapps.league.service.pool;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.africaapps.league.dao.game.PlayerPriceDao;
import com.africaapps.league.dao.game.PoolDao;
import com.africaapps.league.dao.game.PoolPlayerDao;
import com.africaapps.league.dao.game.PoolPlayerPointsHistoryDao;
import com.africaapps.league.dto.PlayerMatchStatisticSummary;
import com.africaapps.league.dto.PlayerMatchSummary;
import com.africaapps.league.dto.PoolPlayerSummary;
import com.africaapps.league.dto.PoolPlayersResults;
import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.game.PlayerPrice;
import com.africaapps.league.model.game.Pool;
import com.africaapps.league.model.game.PoolPlayer;
import com.africaapps.league.model.game.PoolPlayerPointsHistory;
import com.africaapps.league.model.league.BlockType;
import com.africaapps.league.model.league.LeagueSeason;
import com.africaapps.league.model.league.Match;
import com.africaapps.league.model.league.Player;
import com.africaapps.league.service.game.team.UserTeamService;
import com.africaapps.league.service.transaction.ReadTransaction;
import com.africaapps.league.service.transaction.WriteTransaction;

@Service
public class PoolServiceImpl implements PoolService {

	@Autowired
	private PoolDao poolDao;
	@Autowired
	private PoolPlayerDao poolPlayerDao;
	@Autowired
	private PoolPlayerPointsHistoryDao poolPlayerPointsHistoryDao;
	@Autowired
	private PlayerPriceDao playerPriceDao;
	
	@Autowired
	private UserTeamService userTeamService;
	
	private static Logger logger = LoggerFactory.getLogger(PoolServiceImpl.class);

	@ReadTransaction
	@Override
	public Pool getPool(LeagueSeason leagueSeason) throws LeagueException {
		if (leagueSeason != null) {
			return poolDao.getByLeagueSeason(leagueSeason.getId());
		} else {
			return null;
		}
	}

	@WriteTransaction
	@Override
	public void savePlayer(Pool pool, Player player) throws LeagueException {
		if (pool != null && player != null) {
			PoolPlayer pp = getPoolPlayer(pool.getId(), player.getId());
			if (pp == null) {
				pp = new PoolPlayer();
				pp.setPool(pool);
				pp.setPlayer(player);
				pp.setPlayerPrice(getPlayerPrice(player));
				pp.setPlayerCurrentScore(Double.valueOf(0));
				poolPlayerDao.saveOrUpdate(pp);
				if (pp.getPlayerPrice() == 0) {
					logger.error("NO PRICE for new PoolPlayer: " + pp.getId() + " " + pp.getPlayer().getFirstName() + " " + pp.getPlayer().getLastName());
				}
			}
		} else {
			LeagueException le = new LeagueException("ERROR *** PoolPlayer has unknown player: " + player);
			logger.error("Unable to save PoolPlayer:", le);
		}
	}
	
	private Long getPlayerPrice(Player player) throws LeagueException {
		String first = player.getFirstName() != null ? player.getFirstName().trim() : "";
		String second = player.getLastName() != null ? player.getLastName().trim() : "";
		PlayerPrice playerPrice = playerPriceDao.getPrice(first, second);
		if (playerPrice != null) {
			logger.info("Got price:"+playerPrice+" for player: "+player);
			return playerPrice.getPrice().longValue();
		} else {
			logger.info("No price found for for player: "+player);
			return Long.valueOf(0);
		}
	}
	
	@Override
	public BlockType getPlayerBlock(Player player) {
		String first = player.getFirstName() != null ? player.getFirstName().trim() : "";
		String second = player.getLastName() != null ? player.getLastName().trim() : "";
		PlayerPrice playerPrice = playerPriceDao.getPrice(first, second);
		if (playerPrice != null) {
			logger.info("Got block:"+playerPrice.getBlock()+" for player: "+player);
			return playerPrice.getBlock();
		} else {
			logger.info("No block found for for player: "+player);
			return null;
		}
	}
	

	@ReadTransaction
	@Override
	public PoolPlayer getPoolPlayer(long poolId, long playerId) throws LeagueException {
		return poolPlayerDao.getByPoolAndPlayer(poolId, playerId);
	}

	@ReadTransaction
	@Override
	public PoolPlayer getPoolPlayer(long poolPlayerId) throws LeagueException {
		return poolPlayerDao.get(poolPlayerId);
	}

	@WriteTransaction
	@Override
	public void addPointsToPoolPlayer(PoolPlayer poolPlayer, Match match, Double playerScore) throws LeagueException {
		//per player in the match, add their playerScore to their PoolPlayer's currentScore
		poolPlayerDao.addPlayerScore(poolPlayer.getId(), match.getId(), playerScore);		
		//save a history record
		PoolPlayerPointsHistory history = new PoolPlayerPointsHistory();
		history.setMatch(match);
		history.setPlayerPoints(playerScore);
		history.setPoolPlayer(poolPlayer);
		history.setPlayingWeek(match.getPlayingWeek());
		poolPlayerPointsHistoryDao.save(history);
	}

	@ReadTransaction
	@Override
	public List<PlayerMatchSummary> getPlayerMatches(Long poolPlayerId) throws LeagueException {		
		return poolPlayerPointsHistoryDao.getHistoryForPlayer(poolPlayerId);
	}

	@ReadTransaction
	@Override
	public List<PlayerMatchStatisticSummary> getMatchStats(Long poolPlayerId, Long matchId) throws LeagueException {
		return poolPlayerPointsHistoryDao.getStatsForPlayer(poolPlayerId, matchId);
	}

	@ReadTransaction
	@Override
	public PoolPlayersResults getPoolPlayers(Long userTeamId, int page, int pageSize) throws LeagueException {
		logger.info("Getting pool players: page: "+page+" pageSize:"+pageSize);
		Long poolId = userTeamService.getUserTeamPoolId(userTeamId);
		List<PoolPlayer> poolPlayers = poolPlayerDao.getByPoolId(poolId, page, pageSize);
		List<PoolPlayerSummary> summaries = new ArrayList<PoolPlayerSummary>();
		PoolPlayerSummary summary;
		for(PoolPlayer poolPlayer : poolPlayers) {
			summary = new PoolPlayerSummary();
			summary.setBlock(formatBlock(poolPlayer.getPlayer().getBlock()));
			summary.setCurrentScore(poolPlayer.getPlayerCurrentScore());
			summary.setFirstName(poolPlayer.getPlayer().getFirstName());
			summary.setLastName(poolPlayer.getPlayer().getLastName());
			summary.setPoolPlayerId(poolPlayer.getId());
			summary.setPrice(poolPlayer.getPlayerPrice());
			summaries.add(summary);
		}
		PoolPlayersResults results = new PoolPlayersResults();
		results.setPage(page);
		results.setPageSize(pageSize);
		results.setPoolPlayers(summaries);
		return results;
	}

	private String formatBlock(BlockType block) {
		if (block != null) {
			StringBuilder b = new StringBuilder();
			b.append(block.name().substring(0, 1));
			b.append(block.name().substring(1).toLowerCase());
			return b.toString();
		} else {
			return "";
		}
	}
	
	@ReadTransaction
	@Override
	public List<PoolPlayerPointsHistory> getPoolPlayerHistory(long poolPlayerId, long currentPlayingWeekId)
			throws LeagueException {
		return poolPlayerPointsHistoryDao.getForPlayingWeek(poolPlayerId, currentPlayingWeekId);
	}

	@ReadTransaction
	@Override
	public List<PoolPlayer> getPlayersByPointsOrPrice(long poolId, List<Long> existingPlayersId, boolean points, int page, int pageSize) throws LeagueException {
		return poolPlayerDao.getPlayersByPointsOrPrice(poolId, existingPlayersId, points, page, pageSize);
	}

	@ReadTransaction
	@Override
	public List<PoolPlayer> getPlayersByPointsOrPriceAndType(long poolId, List<Long> existingPlayersId, BlockType playerType, boolean points, int page, int pageSize)
			throws LeagueException {
		return poolPlayerDao.getPlayersByPointsOrPriceAndType(poolId, existingPlayersId, playerType, points, page, pageSize);
	}

	@Override
	public int getPlayersByPointsOrPriceCount(long poolId, List<Long> existingPlayersId, boolean points, int page, int pageSize)
			throws LeagueException {
		return poolPlayerDao.getPlayersByPointsOrPriceCount(poolId, existingPlayersId, points, page, pageSize);
	}

	@Override
	public int getPlayersByPointsOrPriceAndTypeCount(long poolId, List<Long> existingPlayersId, BlockType playerType,
			boolean points, int page, int pageSize) throws LeagueException {
		return poolPlayerDao.getPlayersByPointsOrPriceAndTypeCount(poolId, existingPlayersId, playerType, points, page, pageSize);
	}
}