package com.africaapps.league.service.game.team;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.africaapps.league.dao.game.UserTeamDao;
import com.africaapps.league.dao.game.UserTeamScoreHistoryDao;
import com.africaapps.league.dto.UserPlayerSummary;
import com.africaapps.league.dto.UserTeamSummary;
import com.africaapps.league.exception.InvalidPlayerException;
import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.game.Pool;
import com.africaapps.league.model.game.PoolPlayer;
import com.africaapps.league.model.game.TeamFormat;
import com.africaapps.league.model.game.User;
import com.africaapps.league.model.game.UserLeague;
import com.africaapps.league.model.game.UserPlayer;
import com.africaapps.league.model.game.UserPlayerStatus;
import com.africaapps.league.model.game.UserTeam;
import com.africaapps.league.model.game.UserTeamScoreHistory;
import com.africaapps.league.model.league.BlockType;
import com.africaapps.league.model.league.Player;
import com.africaapps.league.service.game.format.TeamFormatService;
import com.africaapps.league.service.game.league.UserLeagueService;
import com.africaapps.league.service.game.player.UserPlayerService;
import com.africaapps.league.service.player.PlayerService;
import com.africaapps.league.service.pool.PoolService;
import com.africaapps.league.service.transaction.ReadTransaction;
import com.africaapps.league.service.transaction.WriteTransaction;

@Service
public class UserTeamServiceImpl implements UserTeamService {

	@Autowired
	private UserTeamDao userTeamDao;
	@Autowired
	private UserTeamScoreHistoryDao userTeamScoreHistoryDao;

	@Autowired
	private TeamFormatService teamFormatService;
	@Autowired
	private UserLeagueService userLeagueService;
	@Autowired
	private PlayerService playerService;
	@Autowired
	private PoolService poolService;
	@Autowired
	private UserPlayerService userPlayerService;

	private static Logger logger = LoggerFactory.getLogger(UserTeamServiceImpl.class);

	@ReadTransaction
	@Override
	public List<UserTeam> getTeams(long userId) throws LeagueException {
		return userTeamDao.getTeams(userId);
	}

	@WriteTransaction
	@Override
	public void saveTeam(UserTeam userTeam) throws LeagueException {
		if (userTeam != null) {
			userTeamDao.saveOrUpdate(userTeam);
		}
	}

	@ReadTransaction
	@Override
	public UserTeam getTeam(long userId, String teamName) throws LeagueException {
		return userTeamDao.getTeam(userId, teamName);
	}

	@ReadTransaction
	@Override
	public UserTeam getTeam(long userTeamId) throws LeagueException {
		return userTeamDao.getTeam(userTeamId);
	}

	@ReadTransaction
	@Override
	public TeamFormat getDefaultTeamFormat(long leagueTypeId) throws LeagueException {
		return teamFormatService.getDefaultFormat(leagueTypeId);
	}

	@Override
	public List<TeamFormat> getTeamFormats(long leagueTypeId) throws LeagueException {
		return teamFormatService.getTeamFormats(leagueTypeId);
	}

	@WriteTransaction
	@Override
	public void setTeamFormat(Long userTeamId, Long teamFormatId) throws LeagueException {
		UserTeam userTeam = getTeam(userTeamId);
		if (userTeam != null) {
			userTeam.setCurrentFormat(teamFormatService.getTeamFormat(teamFormatId));
			saveTeam(userTeam);
		}
	}

	@ReadTransaction
	@Override
	public UserLeague getDefaultUserLeague() throws LeagueException {
		return userLeagueService.getDefaultUserLeague();
	}

	@ReadTransaction
	@Override
	public UserTeamSummary getTeamWithPlayers(long teamId) throws LeagueException {
		UserTeam team = userTeamDao.getTeamWithPlayers(teamId);
		if (team != null) {
			UserTeamSummary summary = new UserTeamSummary();
			summary.setTeamFormat(team.getCurrentFormat());
			summary.setAvailableMoney(team.getAvailableMoney());
			summary.setTeamId(teamId);
			summary.setTeamName(team.getName());
			setUserPlayers(team, summary);
			return summary;
		} else {
			return null;
		}
	}

	private void setUserPlayers(UserTeam team, UserTeamSummary summary) throws LeagueException {
		Iterator<UserPlayer> userPlayers = team.getUserPlayers().iterator();
		UserPlayer userPlayer = null;
		UserPlayerSummary playerSummary = null;
		while (userPlayers.hasNext()) {
			userPlayer = userPlayers.next();
			if (userPlayer.getStatus() != UserPlayerStatus.DROPPED) {
				playerSummary = new UserPlayerSummary();
				playerSummary.setPlayerId(userPlayer.getPoolPlayer().getPlayer().getPlayerId());
				playerSummary.setCurrentScore(userPlayer.getPoolPlayer().getPlayerCurrentScore());
				playerSummary.setFirstName(userPlayer.getPoolPlayer().getPlayer().getFirstName());
				playerSummary.setLastName(userPlayer.getPoolPlayer().getPlayer().getLastName());
				playerSummary.setPoolPlayerId(userPlayer.getPoolPlayer().getId());
				playerSummary.setPrice(userPlayer.getPoolPlayer().getPlayerPrice());
				// Check that the player is possibly a sub for the user's team
				if (userPlayer.getStatus() == UserPlayerStatus.SUBSTITUTE) {
					playerSummary.setBlock(BlockType.SUBSTITUTE);
				} else {
					playerSummary.setBlock(userPlayer.getPoolPlayer().getPlayer().getBlock());
				}
				playerSummary.setStatus(userPlayer.getStatus());
				// Add to correct player grouping
				if (BlockType.DEFENDER.equals(playerSummary.getBlock())) {
					summary.getDefenders().add(playerSummary);
				} else if (BlockType.GOALKEEPER.equals(playerSummary.getBlock())) {
					summary.getGoalKeepers().add(playerSummary);
				} else if (BlockType.MIDFIELDER.equals(playerSummary.getBlock())) {
					summary.getMidfielders().add(playerSummary);
				} else if (BlockType.STRIKER.equals(playerSummary.getBlock())) {
					summary.getStrikers().add(playerSummary);
				} else if (BlockType.SUBSTITUTE.equals(playerSummary.getBlock())) {
					summary.getSubstitutes().add(playerSummary);
				} else {
					logger.error("Unknown player BlockType: " + playerSummary.getBlock());
				}
				// Check if captain
				if (playerSummary.getStatus() == UserPlayerStatus.CAPTAIN) {
					summary.setCaptain("Selected");
				}
			}
		}
	}

	@ReadTransaction
	@Override
	public List<UserPlayerSummary> getTeamPlayers(long teamId, long userTeamId, String type) throws LeagueException {
		List<UserPlayerSummary> summaries = new ArrayList<UserPlayerSummary>();
		UserTeam team = userTeamDao.getTeamWithPlayers(userTeamId);
		if (team != null) {
			long poolId = team.getUserLeague().getPool().getId();
			List<Player> players = null;
			if (type.equalsIgnoreCase(BlockType.SUBSTITUTE.name())) {
				players = playerService.getTeamPlayers(teamId);
			} else {
				players = playerService.getTeamPlayersByType(teamId, type);
			}
			UserPlayerSummary summary = null;
			PoolPlayer poolPlayer = null;
			PoolPlayer pPlayer = null;
			for (Player player : players) {
				poolPlayer = getUserPoolPlayer(team.getUserPlayers(), player.getId());
				if (poolPlayer == null) {
					pPlayer = poolService.getPoolPlayer(poolId, player.getId());
					if (pPlayer != null) {
						summary = new UserPlayerSummary();
						summary.setPlayerId(pPlayer.getPlayer().getPlayerId());
						summary.setPoolPlayerId(pPlayer.getId());
						summary.setFirstName(player.getFirstName());
						summary.setLastName(player.getLastName());
						summary.setPrice(pPlayer.getPlayerPrice());
						summary.setCurrentScore(pPlayer.getPlayerCurrentScore());
						summary.setBlock(player.getBlock());
						summaries.add(summary);
					} else {
						logger.error("Unable to find corresponding pool player for playerId: " + player.getId());
					}
				}
			}
		} else {
			logger.error("No userTeam found for userTeamId: " + userTeamId);
		}
		return summaries;
	}

	private PoolPlayer getUserPoolPlayer(Set<UserPlayer> players, long playerId) throws LeagueException {
		Iterator<UserPlayer> ps = players.iterator();
		while (ps.hasNext()) {
			UserPlayer up = ps.next();
			if (up.getPoolPlayer().getPlayer().getId().longValue() == playerId && up.getStatus() != UserPlayerStatus.DROPPED) {
				return up.getPoolPlayer();
			}
		}
		return null;
	}

	@ReadTransaction
	@Override
	public UserPlayerSummary getTeamPlayer(long userTeamId, long poolPlayerId) throws LeagueException {
		UserPlayer userPlayer = userPlayerService.getPlayerOnUserTeam(userTeamId, poolPlayerId);
		if (userPlayer != null) {
			UserPlayerSummary summary = new UserPlayerSummary();
			summary.setPlayerId(userPlayer.getPoolPlayer().getPlayer().getPlayerId());
			summary.setPoolPlayerId(userPlayer.getPoolPlayer().getId());
			summary.setFirstName(userPlayer.getPoolPlayer().getPlayer().getFirstName());
			summary.setLastName(userPlayer.getPoolPlayer().getPlayer().getLastName());
			summary.setPrice(userPlayer.getPoolPlayer().getPlayerPrice());
			summary.setCurrentScore(userPlayer.getPoolPlayer().getPlayerCurrentScore());
			summary.setBlock(userPlayer.getPoolPlayer().getPlayer().getBlock());
			summary.setStatus(userPlayer.getStatus());
			return summary;
		} else {
			logger.error("No UserPlayer found for userTeamId: " + userTeamId + " poolPlayerId:" + poolPlayerId);
			return null;
		}
	}

	@WriteTransaction
	@Override
	public void setPlayerStatus(long userTeamId, long poolPlayerId, String status) throws LeagueException {
		UserPlayer userPlayer = userPlayerService.getPlayerOnUserTeam(userTeamId, poolPlayerId);
		if (userPlayer != null) {
			UserPlayerStatus s = getStatus(status);
			if (s != null) {
				if (s == UserPlayerStatus.CAPTAIN) {
					UserPlayer existingCaptain = userPlayerService.getCaptain(userTeamId);
					if (existingCaptain != null && existingCaptain.getPoolPlayer().getId().longValue() != poolPlayerId) {
						throw new InvalidPlayerException("Team already has a captain!");
					}
				}
				if (s == UserPlayerStatus.DROPPED) {
					userPlayerService.deleteUserPlayer(userPlayer);
				} else {
					userPlayer.setStatus(s);
					userPlayerService.saveUserPlayer(userPlayer);
					logger.info("Updated player's status: " + userPlayer);
				}
			}
		} else {
			logger.error("Unknown poolPlayerId:" + poolPlayerId + " userTeamId:" + userTeamId);
		}
	}

	private UserPlayerStatus getStatus(String status) {
		for (UserPlayerStatus s : UserPlayerStatus.values()) {
			if (s.name().equalsIgnoreCase(status)) {
				return s;
			}
		}
		logger.error("Unknown UserPlayerStatus: " + status);
		return null;
	}

	@WriteTransaction
	@Override
	public String addPlayerToUserTeam(User user, long userTeamId, long teamId, long poolPlayerId, String playerType)
			throws LeagueException {
		UserPlayer userPlayer = userPlayerService.getPlayerOnUserTeam(userTeamId, poolPlayerId);
		if (userPlayer == null || UserPlayerStatus.DROPPED.equals(userPlayer.getStatus())) {
			UserTeam userTeam = userTeamDao.getTeam(userTeamId);
			BlockType block = getBlockType(playerType);
			logger.info("New player's block:" + playerType);
			checkValidPlayerType(userTeam, block);
			Pool pool = userTeam.getUserLeague().getPool();
			PoolPlayer poolPlayer = poolService.getPoolPlayer(userTeam.getUserLeague().getPool().getId(), poolPlayerId);
			if (poolPlayer.getPlayerPrice() < userTeam.getAvailableMoney()) {
				if (userPlayer == null) {
					userPlayer = new UserPlayer();
					userPlayer.setPool(pool);
					userPlayer.setPoolPlayer(poolPlayer);
					if (block == BlockType.SUBSTITUTE) {
						userPlayer.setStatus(UserPlayerStatus.SUBSTITUTE);
					} else {
						userPlayer.setStatus(UserPlayerStatus.PLAYER);
					}
					logger.info("Set status on new player: " + userPlayer.getStatus());
					userPlayer.setUserTeam(userTeam);
				} else {
					if (block == BlockType.SUBSTITUTE) {
						userPlayer.setStatus(UserPlayerStatus.SUBSTITUTE);
					} else {
						userPlayer.setStatus(UserPlayerStatus.PLAYER);
					}
					logger.info("Set status on existing player: " + userPlayer.getStatus());
				}
				userPlayerService.saveUserPlayer(userPlayer);
				// update the user's team
				userTeam.setAvailableMoney(userTeam.getAvailableMoney() - poolPlayer.getPlayerPrice());
				userTeamDao.saveOrUpdate(userTeam);
				return null;
			} else {
				DecimalFormat df = new DecimalFormat("R##.00");
				return "Too little money. You only have " + df.format(userTeam.getAvailableMoney()) + " to spend.";
			}
		} else {
			return "Player is already on your team";
		}
	}

	private BlockType getBlockType(String playerType) throws InvalidPlayerException {
		if (playerType != null) {
			if (BlockType.DEFENDER.name().equalsIgnoreCase(playerType)) {
				return BlockType.DEFENDER;
			}
			if (BlockType.GOALKEEPER.name().equalsIgnoreCase(playerType)) {
				return BlockType.GOALKEEPER;
			}
			if (BlockType.MIDFIELDER.name().equalsIgnoreCase(playerType)) {
				return BlockType.MIDFIELDER;
			}
			if (BlockType.STRIKER.name().equalsIgnoreCase(playerType)) {
				return BlockType.STRIKER;
			}
			if (BlockType.SUBSTITUTE.name().equalsIgnoreCase(playerType)) {
				return BlockType.SUBSTITUTE;
			}
		}
		throw new InvalidPlayerException("Unknown player type: " + playerType);
	}

	private void checkValidPlayerType(UserTeam userTeam, BlockType block) throws InvalidPlayerException, LeagueException {
		if (getTeamPlayersCount(userTeam) >= 15) {
			throw new InvalidPlayerException("Too many players on the squad!");
		}
		Map<BlockType, Integer> playerTypeCounts = getPlayerTypeCounts(userTeam);
		logger.info("Checking player's count: " + playerTypeCounts.toString());

		switch (block) {
		case DEFENDER:
			if (playerTypeCounts.get(BlockType.DEFENDER) >= userTeam.getCurrentFormat().getDefenderCount()) {
				throw new InvalidPlayerException("Too many defenders on the team!");
			}
			break;
		case MIDFIELDER:
			if (playerTypeCounts.get(BlockType.MIDFIELDER) >= userTeam.getCurrentFormat().getMidfielderCount()) {
				throw new InvalidPlayerException("Too many midfielders on the team!");
			}
			break;
		case STRIKER:
			if (playerTypeCounts.get(BlockType.STRIKER) >= userTeam.getCurrentFormat().getMidfielderCount()) {
				throw new InvalidPlayerException("Too many strikers on the team!");
			}
			break;
		}
	}

	private int getTeamPlayersCount(UserTeam userTeam) {
		int count = 0;
		for (UserPlayer player : userTeam.getUserPlayers()) {
			if (player.getStatus() != UserPlayerStatus.DROPPED) {
				count++;
			}
		}
		return count;
	}

	private Map<BlockType, Integer> getPlayerTypeCounts(UserTeam userTeam) throws LeagueException {
		Map<BlockType, Integer> counts = new HashMap<BlockType, Integer>();
		counts.put(BlockType.DEFENDER, Integer.valueOf(0));
		counts.put(BlockType.GOALKEEPER, Integer.valueOf(0));
		counts.put(BlockType.MIDFIELDER, Integer.valueOf(0));
		counts.put(BlockType.STRIKER, Integer.valueOf(0));
		counts.put(BlockType.SUBSTITUTE, Integer.valueOf(0));
		for (UserPlayer userPlayer : userTeam.getUserPlayers()) {
			if (userPlayer.getStatus() != UserPlayerStatus.DROPPED) {
				if (userPlayer.getStatus() == UserPlayerStatus.SUBSTITUTE) {
					int newCount = counts.get(BlockType.SUBSTITUTE) + 1;
					counts.put(BlockType.SUBSTITUTE, newCount);
				} else {
					if (BlockType.DEFENDER.equals(userPlayer.getPoolPlayer().getPlayer().getBlock())) {
						int newCount = counts.get(BlockType.DEFENDER) + 1;
						counts.put(BlockType.DEFENDER, newCount);
					} else if (BlockType.GOALKEEPER.equals(userPlayer.getPoolPlayer().getPlayer().getBlock())) {
						int newCount = counts.get(BlockType.GOALKEEPER) + 1;
						counts.put(BlockType.GOALKEEPER, newCount);
					} else if (BlockType.MIDFIELDER.equals(userPlayer.getPoolPlayer().getPlayer().getBlock())) {
						int newCount = counts.get(BlockType.MIDFIELDER) + 1;
						counts.put(BlockType.MIDFIELDER, newCount);
					} else if (BlockType.STRIKER.equals(userPlayer.getPoolPlayer().getPlayer().getBlock())) {
						int newCount = counts.get(BlockType.STRIKER) + 1;
						counts.put(BlockType.STRIKER, newCount);
					}
				}
			}
		}
		return counts;
	}

	@WriteTransaction
	@Override
	public void addPointsForPoolPlayer(PoolPlayer poolPlayer, int playerPoints) throws LeagueException {
		logger.info("Added playerPoints:" + playerPoints + " for poolPlayer:" + poolPlayer);
		List<UserTeam> userTeams = userTeamDao.getTeamsWithPoolPlayer(poolPlayer.getId());
		List<Long> ids = getUserTeamIds(userTeams);
		if (ids.size() > 0) {
			logger.info("Added playerPoints:" + playerPoints + " to UserTeams:" + ids.toString());
			userTeamDao.addPlayerPoints(ids, playerPoints);
			// Save a team's history
			UserTeamScoreHistory history = new UserTeamScoreHistory();
			history.setPlayerPoints(playerPoints);
			history.setPoolPlayer(poolPlayer);
			for (UserTeam userTeam : userTeams) {
				history.setUserTeam(userTeam);
				userTeamScoreHistoryDao.save(history);
				logger.info("Saved team's history: " + history);
			}
		} else {
			logger.info("No current UserTeams for poolPlayer:" + poolPlayer.getId() + " " + poolPlayer.getPlayer().getFirstName()
					+ " " + poolPlayer.getPlayer().getLastName());
		}
	}

	private List<Long> getUserTeamIds(List<UserTeam> userTeams) throws LeagueException {
		List<Long> ids = new ArrayList<Long>();
		for (UserTeam userTeam : userTeams) {
			ids.add(userTeam.getId());
		}
		return ids;
	}
}