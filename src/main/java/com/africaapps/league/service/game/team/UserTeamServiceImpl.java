package com.africaapps.league.service.game.team;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.africaapps.league.dao.game.PlayingWeekDao;
import com.africaapps.league.dao.game.UserTeamDao;
import com.africaapps.league.dao.game.UserTeamScoreHistoryDao;
import com.africaapps.league.dao.game.UserTeamTradeDao;
import com.africaapps.league.dto.NeededPlayer;
import com.africaapps.league.dto.PlayerMatchEventSummary;
import com.africaapps.league.dto.PlayerMatchSummary;
import com.africaapps.league.dto.UserPlayerSummary;
import com.africaapps.league.dto.UserTeamListSummary;
import com.africaapps.league.dto.UserTeamScoreHistorySummary;
import com.africaapps.league.dto.UserTeamSummary;
import com.africaapps.league.exception.InvalidPlayerException;
import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.game.PlayingWeek;
import com.africaapps.league.model.game.Pool;
import com.africaapps.league.model.game.PoolPlayer;
import com.africaapps.league.model.game.TeamFormat;
import com.africaapps.league.model.game.User;
import com.africaapps.league.model.game.UserLeague;
import com.africaapps.league.model.game.UserPlayer;
import com.africaapps.league.model.game.UserPlayerStatus;
import com.africaapps.league.model.game.UserTeam;
import com.africaapps.league.model.game.UserTeamScoreHistory;
import com.africaapps.league.model.game.UserTeamStatus;
import com.africaapps.league.model.game.UserTeamTrade;
import com.africaapps.league.model.league.BlockType;
import com.africaapps.league.model.league.League;
import com.africaapps.league.model.league.LeagueSeason;
import com.africaapps.league.model.league.Match;
import com.africaapps.league.model.league.Player;
import com.africaapps.league.service.fixture.FixtureService;
import com.africaapps.league.service.game.format.TeamFormatService;
import com.africaapps.league.service.game.league.UserLeagueService;
import com.africaapps.league.service.game.player.UserPlayerService;
import com.africaapps.league.service.league.LeagueService;
import com.africaapps.league.service.match.MatchService;
import com.africaapps.league.service.player.PlayerService;
import com.africaapps.league.service.pool.PoolService;
import com.africaapps.league.service.team.TeamService;
import com.africaapps.league.service.transaction.ReadTransaction;
import com.africaapps.league.service.transaction.WriteTransaction;

@Service
public class UserTeamServiceImpl implements UserTeamService {

	@Autowired
	private UserTeamDao userTeamDao;
	@Autowired
	private UserTeamScoreHistoryDao userTeamScoreHistoryDao;
	@Autowired
	private UserTeamTradeDao userTeamTradeDao;
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
	@Autowired
	private MatchService matchService;
	@Autowired
	private TeamService teamService;
	@Autowired
	private LeagueService leagueService;
	@Autowired
	private FixtureService fixtureService;

	// TODO for testing only remove later
	@Autowired
	private PlayingWeekDao playingWeekDao;

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
	public TeamFormat getDefaultTeamFormat(League league) throws LeagueException {
		return leagueService.getTeamDefaultFormat(league);
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
			TeamFormat newFormat = teamFormatService.getTeamFormat(teamFormatId);
			// Check if the player numbers are ok for the new format
			Map<BlockType, Integer> playerTypeCounts = getPlayerTypeCounts(userTeam);
			logger.info("Checking player's count: " + playerTypeCounts.toString());
			if (playerTypeCounts.get(BlockType.DEFENDER) > newFormat.getDefenderCount()) {
				throw new InvalidPlayerException("You can only have " + newFormat.getDefenderCount()
						+ " defenders on the team to use the new format");
			}
			if (playerTypeCounts.get(BlockType.MIDFIELDER) > newFormat.getMidfielderCount()) {
				throw new InvalidPlayerException("You can only have " + newFormat.getMidfielderCount()
						+ " midfielders on the team to use the new format");
			}
			if (playerTypeCounts.get(BlockType.STRIKER) > newFormat.getStrikerCount()) {
				throw new InvalidPlayerException("You can only have " + newFormat.getStrikerCount()
						+ " strikers on the team to use the new format");
			}
			if (playerTypeCounts.get(BlockType.GOALKEEPER) > leagueService.getGoalkeepersCount(userTeam.getUserLeague()
					.getLeague())) {
				throw new InvalidPlayerException("You can only have "
						+ leagueService.getGoalkeepersCount(userTeam.getUserLeague().getLeague())
						+ " goalkeeper on the team to use the new format");
			}
			if (playerTypeCounts.get(BlockType.SUBSTITUTE) > leagueService.getSubstitutesCount(userTeam.getUserLeague()
					.getLeague())) {
				throw new InvalidPlayerException("You can only have "
						+ leagueService.getSubstitutesCount(userTeam.getUserLeague().getLeague())
						+ " substitutes on the team to use the new format");
			}
			// Set the new format
			userTeam.setCurrentFormat(newFormat);
			saveTeam(userTeam);
		}
	}

	@ReadTransaction
	@Override
	public UserLeague getDefaultUserLeague() throws LeagueException {
		return userLeagueService.getDefaultUserLeague();
	}

	@Override
	public Long getDefaultAvailableMoney(League league) throws LeagueException {
		return leagueService.getTeamInitialMoney(league);
	}

	@ReadTransaction
	@Override
	public UserTeamSummary getTeamWithPlayers(long teamId) throws LeagueException {
		UserTeam team = userTeamDao.getTeamWithPlayers(teamId);
		if (team != null) {
			UserTeamSummary summary = new UserTeamSummary();
			summary.setTeamStatus(team.getStatus().name());
			summary.setTeamFormat(team.getCurrentFormat());
			summary.setAvailableMoney(team.getAvailableMoney());
			summary.setTeamId(teamId);
			summary.setTeamName(team.getName());
			setUserPlayers(team, summary, null);
			LeagueSeason leagueSeason = leagueService.getCurrentSeason(teamId);
			summary.setCanSubstitute(isTeamChangesPossible(leagueSeason));
			summary.setCanTrade(isUserTeamTradeAvailable(leagueSeason, teamId));
			sortPlayers(summary);
			return summary;
		} else {
			return null;
		}
	}

	private void sortPlayers(UserTeamSummary summary) {
		Comparator<UserPlayerSummary> comparator = new Comparator<UserPlayerSummary>() {
			@Override
			public int compare(UserPlayerSummary o1, UserPlayerSummary o2) {
				String n1 = o1.getFirstName() + " " + o1.getLastName();
				String n2 = o2.getFirstName() + " " + o2.getFirstName();
				return n1.compareTo(n2);
			}
		};
		Collections.sort(summary.getDefenders(), comparator);
		Collections.sort(summary.getMidfielders(), comparator);
		Collections.sort(summary.getStrikers(), comparator);
		Collections.sort(summary.getGoalKeepers(), comparator);
		Collections.sort(summary.getSubstitutes(), comparator);
	}

	@ReadTransaction
	@Override
	public UserTeamSummary getTeamWithSamePlayers(long teamId, long poolPlayerId) throws LeagueException {
		UserTeam team = userTeamDao.getTeamWithPlayers(teamId);
		if (team != null) {
			UserPlayer player = userPlayerService.getPlayerOnUserTeam(teamId, poolPlayerId);
			if (player != null) {
				UserTeamSummary summary = new UserTeamSummary();
				summary.setTeamStatus(team.getStatus().name());
				summary.setTeamFormat(team.getCurrentFormat());
				summary.setAvailableMoney(team.getAvailableMoney());
				summary.setTeamId(teamId);
				summary.setTeamName(team.getName());
				BlockType blockType = player.getPoolPlayer().getPlayer().getBlock();
				logger.info("Only getting player for blockType: " + blockType);
				setUserPlayers(team, summary, blockType);
				Collections.sort(summary.getDefenders(), new Comparator<UserPlayerSummary>() {
					@Override
					public int compare(UserPlayerSummary o1, UserPlayerSummary o2) {
						return o1.getPlayerId().compareTo(o2.getPlayerId());
					}
				});
				return summary;
			} else {
				logger.error("Unknown userPlayer: " + poolPlayerId + " teamId:" + teamId);
				throw new LeagueException("Unknown user player");
			}
		} else {
			return null;
		}
	}

	private void setUserPlayers(UserTeam team, UserTeamSummary summary, BlockType requiredType) throws LeagueException {
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
				logger.debug("Checking player's status: " + userPlayer.getStatus());
				if (userPlayer.getStatus() == UserPlayerStatus.SUBSTITUTE) {
					playerSummary.setBlock(formatEnum(BlockType.SUBSTITUTE));
					playerSummary.setOriginalBlock(formatEnum(userPlayer.getPoolPlayer().getPlayer().getBlock()));
					logger.info("Player is a substitute: " + playerSummary.getPoolPlayerId());
				} else {
					playerSummary.setBlock(formatEnum(userPlayer.getPoolPlayer().getPlayer().getBlock()));
				}
				playerSummary.setStatus(formatStatus(userPlayer.getStatus()));
				if (requiredType == null || requiredType.name().equalsIgnoreCase(playerSummary.getBlock())) {
					// Add to correct player grouping
					if (BlockType.DEFENDER.name().equalsIgnoreCase(playerSummary.getBlock())) {
						summary.getDefenders().add(playerSummary);
					} else if (BlockType.GOALKEEPER.name().equalsIgnoreCase(playerSummary.getBlock())) {
						summary.getGoalKeepers().add(playerSummary);
					} else if (BlockType.MIDFIELDER.name().equalsIgnoreCase(playerSummary.getBlock())) {
						summary.getMidfielders().add(playerSummary);
					} else if (BlockType.STRIKER.name().equalsIgnoreCase(playerSummary.getBlock())) {
						summary.getStrikers().add(playerSummary);
					} else if (BlockType.SUBSTITUTE.name().equalsIgnoreCase(playerSummary.getBlock())) {
						summary.getSubstitutes().add(playerSummary);
					} else {
						logger.error("Unknown player BlockType: " + playerSummary.getBlock());
					}
				}
				// Check if captain
				if (playerSummary.getStatus().equalsIgnoreCase(UserPlayerStatus.CAPTAIN.name())) {
					summary.setCaptain(playerSummary.getFirstName() + " " + playerSummary.getLastName());
					summary.setCaptainId(playerSummary.getPoolPlayerId());
				}
			}
		}
	}

	private String formatStatus(UserPlayerStatus status) {
		if (status != null) {
			String firstLetter = status.name().substring(0, 1).toUpperCase();
			return firstLetter + status.name().substring(1).toLowerCase();
		} else {
			return "";
		}
	}

	private String formatEnum(BlockType block) {
		if (block != null) {
			String firstLetter = block.name().substring(0, 1).toUpperCase();
			return firstLetter + block.name().substring(1).toLowerCase();
		} else {
			return "";
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
						summary.setBlock(formatEnum(player.getBlock()));
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
			summary.setBlock(formatEnum(userPlayer.getPoolPlayer().getPlayer().getBlock()));
			summary.setStatus(formatStatus(userPlayer.getStatus()));
			return summary;
		} else {
			logger.error("No UserPlayer found for userTeamId: " + userTeamId + " poolPlayerId:" + poolPlayerId);
			return null;
		}
	}

	@WriteTransaction
	@Override
	public void setPlayerStatus(long userTeamId, long poolPlayerId, String status) throws LeagueException {
		checkTeamChangesPossible(userTeamId);
		UserPlayer userPlayer = userPlayerService.getPlayerOnUserTeam(userTeamId, poolPlayerId);
		if (userPlayer != null) {
			UserPlayerStatus s = getStatus(status);
			if (s != null) {
				if (s == UserPlayerStatus.CAPTAIN) {
					UserPlayer existingCaptain = userPlayerService.getCaptain(userTeamId);
					if (existingCaptain != null && existingCaptain.getPoolPlayer().getId().longValue() != poolPlayerId) {
						existingCaptain.setStatus(UserPlayerStatus.PLAYER);
						userPlayerService.saveUserPlayer(existingCaptain);
						logger.info("Changed existing captain back to a player: " + existingCaptain);
					}
				}
				if (s == UserPlayerStatus.DROPPED) {
					LeagueSeason leagueSeason = leagueService.getCurrentSeason(userTeamId);
					dropUser(leagueSeason, userTeamId, userPlayer);
				} else {
					updatePlayerStatus(userTeamId, userPlayer, s);
				}
			}
		} else {
			throw new LeagueException("Unknown poolPlayerId:" + poolPlayerId + " userTeamId:" + userTeamId);
		}
	}

	@WriteTransaction
	@Override
	public void swapPlayers(long userTeamId, long substitutePlayerId, Long playerId) throws LeagueException {
		checkTeamChangesPossible(userTeamId);
		UserPlayer substitutePlayer = userPlayerService.getPlayerOnUserTeam(userTeamId, substitutePlayerId);
		if (substitutePlayer != null) {
			UserPlayer player = userPlayerService.getPlayerOnUserTeam(userTeamId, playerId);
			if (player != null) {
				if (UserPlayerStatus.SUBSTITUTE.equals(substitutePlayer.getStatus())) {
					if (UserPlayerStatus.PLAYER.equals(player.getStatus())) {
						substitutePlayer.setStatus(UserPlayerStatus.PLAYER);
						userPlayerService.saveUserPlayer(substitutePlayer);
						logger.info("Saved substitute as player: " + player);
						player.setStatus(UserPlayerStatus.SUBSTITUTE);
						userPlayerService.saveUserPlayer(player);
						logger.info("Saved player as substitute: " + player);
					} else {
						logger.error("Expected player: " + player);
						throw new LeagueException("Player is not valid swap player");
					}
				} else {
					logger.error("Expected substitute: " + substitutePlayer);
					throw new LeagueException("Player is not a substitute");
				}
			} else {
				throw new LeagueException("Unknown player:" + playerId);
			}
		} else {
			throw new LeagueException("Unknown substitute:" + substitutePlayerId);
		}
	}

	private void dropUser(LeagueSeason leagueSeason, long userTeamId, UserPlayer userPlayer) throws LeagueException {		
		UserTeamStatus status = userTeamDao.getUserTeamStatus(userTeamId);
		if (UserTeamStatus.COMPLETE.equals(status)) {
			checkTeamChangesPossible(userTeamId);
			if (!isUserTeamTradeAvailable(leagueSeason, userTeamId)) {
				throw new InvalidPlayerException("You can not drop a player as you can not trade in a new pool player");
			}
		}
		userPlayerService.deleteUserPlayer(userPlayer);
		// Increase team's available money
		UserTeam userTeam = userTeamDao.getTeam(userTeamId);
		userTeam.setAvailableMoney(userTeam.getAvailableMoney() + userPlayer.getPoolPlayer().getPlayerPrice());
		// Update team's status
		if (userTeam.getUserPlayers().size() < leagueService.getSquadCount(userTeam.getUserLeague().getLeague())
				&& userTeam.getStatus() == UserTeamStatus.COMPLETE) {
			userTeam.setStatus(UserTeamStatus.INCOMPLETE);
			logger.info("UserTeam is no longer complete: " + userTeamId);
		}
		userTeamDao.saveOrUpdate(userTeam);
		logger.info("Dropped user: " + userPlayer.getId());
	}
	
	private void checkTeamChangesPossible(long userTeamId) throws LeagueException {
		UserTeamStatus teamStatus = userTeamDao.getUserTeamStatus(userTeamId);
		if (UserTeamStatus.COMPLETE.equals(teamStatus)) {
			LeagueSeason leagueSeason = leagueService.getCurrentSeason(userTeamId);
			if (!isTeamChangesPossible(leagueSeason)) {
				throw new LeagueException("Unable to make changes to your team during and after matches");
			}
		}
	}

	private boolean isTeamChangesPossible(LeagueSeason leagueSeason) throws LeagueException {
		return fixtureService.isTradeOrSubstitutionAllowed(leagueSeason, new Date());
	}

	private boolean isUserTeamTradeAvailable(LeagueSeason leagueSeason, long userTeamId) throws LeagueException {
		PlayingWeek currentPlayingWeek = getCurrentPlayingWeek();
		if (currentPlayingWeek != null) {
			logger.debug("Current playing week: " + currentPlayingWeek);
			int playingWeek2 = 0;
			if (currentPlayingWeek.getOrder() % 2 == 0) {
				// Even week
				playingWeek2 = currentPlayingWeek.getOrder() - 1;
			} else {
				// Odd week
				playingWeek2 = currentPlayingWeek.getOrder() + 1;
			}
			logger.info("Checking if UserTeam has traded in weeks: " + currentPlayingWeek.getOrder() + ", " + playingWeek2);
			int trades = userTeamTradeDao.getUserTeamTradeInWeeks(currentPlayingWeek.getLeagueSeason().getId(), userTeamId,
					new Integer[] { currentPlayingWeek.getOrder(), playingWeek2 });
			logger.info("Got trades: " + trades);
			if (trades > 0) {
				return false;
			} else {
				return true;
			}
		} else {
			logger.debug("No current playing week so players can be traded");
			// You can trade until the season starts...
			return true;
		}
	}

	private PlayingWeek getCurrentPlayingWeek() throws LeagueException {
		UserLeague userLeague = getDefaultUserLeague();
		League league = userLeague.getLeague();
		LeagueSeason leagueSeason = leagueService.getCurrentSeason(league);
		// TODO for testing only
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 2012);
		calendar.set(Calendar.MONTH, 7);
		calendar.set(Calendar.DAY_OF_MONTH, 20);
		PlayingWeek currentPlayingWeek = playingWeekDao.get(leagueSeason.getId(), calendar.getTime());
		// END TODO
		// PlayingWeek currentPlayingWeek = leagueService.getPlayingWeek(leagueSeason, new Date());
		logger.info("Current playing week: " + currentPlayingWeek);
		return currentPlayingWeek;
	}

	private void updatePlayerStatus(long userTeamId, UserPlayer userPlayer, UserPlayerStatus s) throws LeagueException {
		if (s == UserPlayerStatus.PLAYER && userPlayer.getStatus() == UserPlayerStatus.SUBSTITUTE) {
			// Check number of existing players of the specified block
			UserTeam userTeam = userTeamDao.getTeam(userTeamId);
			checkValidPlayerType(userTeam, userPlayer.getPoolPlayer().getPlayer().getBlock());
		}
		// Otherwise update the player's status
		userPlayer.setStatus(s);
		userPlayerService.saveUserPlayer(userPlayer);
		logger.info("Updated player's status: " + userPlayer);
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
	public void addPlayerToUserTeam(User user, long userTeamId, long poolPlayerId, String playerType) throws LeagueException {
		checkTeamChangesPossible(userTeamId);
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
					LeagueSeason leagueSeason = leagueService.getCurrentSeason(userTeamId);
					if (UserTeamStatus.COMPLETE.equals(userTeam.getStatus()) && !isUserTeamTradeAvailable(leagueSeason, userTeamId)) {
						throw new InvalidPlayerException("You can not trade in a new pool player now");
					}
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
			} else {
				DecimalFormat df = new DecimalFormat("R#,###");
				throw new InvalidPlayerException("Player is too expensive. You only have " + df.format(userTeam.getAvailableMoney())
						+ " to spend.");
			}
		} else {
			logger.error("Existing poolPlayerId: " + poolPlayerId);
			throw new InvalidPlayerException("Player is already on your team");
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
		if (getTeamPlayersCount(userTeam) >= leagueService.getSquadCount(userTeam.getUserLeague().getLeague())) {
			throw new InvalidPlayerException("Too many players on the squad!");
		}
		Map<BlockType, Integer> playerTypeCounts = getPlayerTypeCounts(userTeam);
		logger.info("Checking player's count: " + playerTypeCounts.toString() + " for block: " + block);
		if (block != null) {
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
				if (playerTypeCounts.get(BlockType.STRIKER) >= userTeam.getCurrentFormat().getStrikerCount()) {
					throw new InvalidPlayerException("Too many strikers on the team!");
				}
				break;
			}
		} else {
			throw new InvalidPlayerException("Player does not have a position currently assigned to them");
		}
	}

	private int getTeamPlayersCount(UserTeam userTeam) {
		int count = 0;
		for (UserPlayer player : userTeam.getUserPlayers()) {
			if (player.getStatus() != UserPlayerStatus.DROPPED) {
				logger.info("userPlayer: " + player.getId());
				count++;
			}
		}
		logger.info("Players count: " + count + " for userTeam:" + userTeam);
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
	public void addPointsForPoolPlayer(Match match, PoolPlayer poolPlayer, int playerPoints) throws LeagueException {
		logger.info("Added playerPoints:" + playerPoints + " for poolPlayer:" + poolPlayer);
		List<UserTeam> userTeams = userTeamDao.getTeamsWithPoolPlayer(poolPlayer.getId());
		List<Long> ids = getUserTeamIds(userTeams);
		if (ids.size() > 0) {
			userTeamDao.addPlayerPoints(ids, playerPoints);
			// Save a team's history
			UserTeamScoreHistory history = null;
			for (UserTeam userTeam : userTeams) {
				history = new UserTeamScoreHistory();
				history.setPlayerPoints(playerPoints);
				history.setPoolPlayer(poolPlayer);
				history.setMatch(match);
				history.setUserTeam(userTeam);
				history.setPlayingWeek(match.getPlayingWeek());
				userTeamScoreHistoryDao.save(history);
				logger.info("Saved UserTeam's score history: " + history);
			}
		} else {
			logger.info("No current UserTeams for poolPlayer:" + poolPlayer.getId() + " " + poolPlayer.getPlayer().getFirstName()
					+ " " + poolPlayer.getPlayer().getLastName());
		}
	}

	@WriteTransaction
	@Override
	public void addPlayersPoints(long userTeamId, int points) throws LeagueException {
		List<Long> ids = new ArrayList<Long>();
		ids.add(userTeamId);
		userTeamDao.addPlayerPoints(ids, points);
	}

	@WriteTransaction
	@Override
	public void addPointsForCaptain(Match match, PoolPlayer poolPlayer, int playerPoints) throws LeagueException {
		logger.info("Added Captain's playerPoints:" + playerPoints + " for poolPlayer:" + poolPlayer);
		List<UserTeam> userTeams = userTeamDao.getTeamsWithCaptain(poolPlayer.getId());
		List<Long> ids = getUserTeamIds(userTeams);
		if (ids.size() > 0) {
			userTeamDao.addPlayerPoints(ids, playerPoints);
			// Save a team's history
			UserTeamScoreHistory history = null;
			for (UserTeam userTeam : userTeams) {
				history = new UserTeamScoreHistory();
				history.setPlayerPoints(playerPoints);
				history.setPoolPlayer(poolPlayer);
				history.setMatch(match);
				history.setUserTeam(userTeam);
				history.setPlayingWeek(match.getPlayingWeek());
				history.setCaptainExtraScore(true);
				userTeamScoreHistoryDao.save(history);
				logger.info("Saved capatin UserTeam's score history: " + history);
			}
		} else {
			logger.info("No current UserTeams for Captain's poolPlayer:" + poolPlayer.getId() + " "
					+ poolPlayer.getPlayer().getFirstName() + " " + poolPlayer.getPlayer().getLastName());
		}
	}

	private List<Long> getUserTeamIds(List<UserTeam> userTeams) throws LeagueException {
		List<Long> ids = new ArrayList<Long>();
		for (UserTeam userTeam : userTeams) {
			ids.add(userTeam.getId());
		}
		return ids;
	}

	@WriteTransaction
	@Override
	public void setTeam(User user, Long userTeamId) throws InvalidPlayerException, LeagueException {
		logger.info("Setting team for user:" + user.getUsername() + " userTeamId:" + userTeamId);
		UserTeam userTeam = userTeamDao.getTeamWithPlayers(userTeamId);
		if (userTeam != null) {
			if (userTeam.getUserPlayers().size() == leagueService.getSquadCount(userTeam.getUserLeague().getLeague())) {
				checkPlayersCount(userTeam);
				UserPlayer captain = getUserTeamCaptain(userTeam);
				if (captain == null) {
					throw new InvalidPlayerException("You need to assign a captain");
				}
				// Update the team's status
				userTeam.setStatus(UserTeamStatus.COMPLETE);
				userTeamDao.saveOrUpdate(userTeam);
				logger.info("Updated the status of the userTeam: " + userTeam);
			} else {
				throw new InvalidPlayerException("You need " + leagueService.getSquadCount(userTeam.getUserLeague().getLeague())
						+ " players in your squad");
			}
		} else {
			throw new LeagueException("Unknown team");
		}
	}

	private void checkPlayersCount(UserTeam userTeam) throws InvalidPlayerException, LeagueException {
		Map<BlockType, Integer> playerTypeCounts = getPlayerTypeCounts(userTeam);
		logger.info("Checking player's count: " + playerTypeCounts.toString());
		if (playerTypeCounts.get(BlockType.DEFENDER) != userTeam.getCurrentFormat().getDefenderCount()) {
			throw new InvalidPlayerException("You need " + userTeam.getCurrentFormat().getDefenderCount()
					+ " defenders on the team");
		}
		if (playerTypeCounts.get(BlockType.MIDFIELDER) != userTeam.getCurrentFormat().getMidfielderCount()) {
			throw new InvalidPlayerException("You need " + userTeam.getCurrentFormat().getMidfielderCount()
					+ " midfielders on the team");
		}
		if (playerTypeCounts.get(BlockType.STRIKER) != userTeam.getCurrentFormat().getStrikerCount()) {
			throw new InvalidPlayerException("You need " + userTeam.getCurrentFormat().getStrikerCount() + " strikers on the team");
		}
		if (playerTypeCounts.get(BlockType.GOALKEEPER) != leagueService
				.getGoalkeepersCount(userTeam.getUserLeague().getLeague())) {
			throw new InvalidPlayerException("You need at least "
					+ leagueService.getGoalkeepersCount(userTeam.getUserLeague().getLeague()) + " goalkeeper on the team");
		}
		if (playerTypeCounts.get(BlockType.SUBSTITUTE) != leagueService
				.getSubstitutesCount(userTeam.getUserLeague().getLeague())) {
			throw new InvalidPlayerException("You need at least "
					+ leagueService.getSubstitutesCount(userTeam.getUserLeague().getLeague()) + " substitutes on the team");
		}
	}

	private UserPlayer getUserTeamCaptain(UserTeam userTeam) {
		for (UserPlayer userPlayer : userTeam.getUserPlayers()) {
			if (UserPlayerStatus.CAPTAIN.equals(userPlayer.getStatus())) {
				return userPlayer;
			}
		}
		return null;
	}

	@Override
	public List<PlayerMatchSummary> getPoolPlayerMatches(Long poolPlayerId) throws LeagueException {
		return poolService.getPlayerMatches(poolPlayerId);
	}

	@Override
	public List<PlayerMatchEventSummary> getPoolPlayerMatchEvents(Long poolPlayerId, Long matchId) throws LeagueException {
		return poolService.getMatchEvents(poolPlayerId, matchId);
	}

	@ReadTransaction
	@Override
	public List<UserTeamScoreHistorySummary> getUserTeamScoreHistory(User user, Long userTeamId) throws LeagueException {
		List<UserTeamScoreHistorySummary> scores = userTeamDao.getScoreHistory(userTeamId);
		Match match = null;
		Map<Long, Match> matches = new HashMap<Long, Match>();
		for (UserTeamScoreHistorySummary score : scores) {
			if (matches.containsKey(score.getMatchId())) {
				match = matches.get(score.getMatchId());
			} else {
				match = matchService.getMatch(score.getMatchId());
				matches.put(score.getMatchId(), match);
			}
			score.setTeamOneName(match.getTeam1().getClubName());
			score.setTeamTwoName(match.getTeam2().getClubName());
		}
		return scores;
	}

	@ReadTransaction
	@Override
	public List<UserTeamScoreHistorySummary> getUserTeamScorePlayersHistory(User user, Long userTeamId, Long matchId)
			throws LeagueException {
		List<UserTeamScoreHistorySummary> scores = userTeamDao.getPlayersScoreHistoryByMatch(userTeamId, matchId);
		Map<Long, String> teams = new HashMap<Long, String>();
		String team = null;
		Integer matchPoints = 0;
		for (UserTeamScoreHistorySummary score : scores) {
			matchPoints += score.getPlayerPoints();
			// set the player's team name
			if (teams.containsKey(score.getPlayerTeamId())) {
				team = teams.get(score.getPlayerTeamId());
			} else {
				logger.info("Getting team name for id: " + score.getPlayerTeamId());
				team = teamService.getTeamName(score.getPlayerTeamId());
				teams.put(score.getPlayerTeamId(), team);
			}
			score.setTeamOneName(team);
			logger.info("Set player's team name: " + team);
		}
		scores.get(0).setMatchPoints(matchPoints);
		return scores;
	}

	@ReadTransaction
	@Override
	public Long getUserTeamPoolId(Long userTeamId) throws LeagueException {
		return userTeamDao.getTeamPoolId(userTeamId);
	}

	@ReadTransaction
	@Override
	public List<UserTeamListSummary> getTeamSummaries(long userId) throws LeagueException {
		return userTeamDao.getTeamListSummary(userId);
	}

	@ReadTransaction
	@Override
	public boolean isUserTeamAbleToTrade(long userTeamId) throws LeagueException {
		LeagueSeason leagueSeason = leagueService.getCurrentSeason(userTeamId);
		if (!isUserTeamTradeAvailable(leagueSeason, userTeamId)) {
			return false;
		} else {
			return true;
		}
	}

	@ReadTransaction
	@Override
	public Long getAvailableMoney(long userTeamId) throws LeagueException {
		return userTeamDao.getAvailableMoney(userTeamId);
	}

	@Override
	public List<NeededPlayer> getIncompleteUserTeams(PlayingWeek playingWeek) throws LeagueException {
		if (playingWeek != null) {
			return userTeamScoreHistoryDao.getIncompleteTeams(playingWeek.getId());
		} else {
			throw new LeagueException("Invalid playing week");
		}
	}

	@Override
	public void saveUserTeamScoreHistory(UserTeamScoreHistory userTeamScoreHistory) throws LeagueException {
		if (userTeamScoreHistory != null) {
			userTeamScoreHistoryDao.save(userTeamScoreHistory);
		}
	}

	@ReadTransaction
	@Override
	public List<UserTeamScoreHistory> getScoreHistory(long userTeamId, long playingWeekId) throws LeagueException {
		return userTeamScoreHistoryDao.getScoreHistory(userTeamId, playingWeekId);
	}

	@WriteTransaction
	@Override
	public void tradePlayers(User user, long userTeamId, long poolPlayerId, long selectedPoolPlayerId)
			throws InvalidPlayerException, LeagueException {
		logger.info("Trading players userTeamId:" + userTeamId + " poolPlayerId: " + poolPlayerId + " selectedPoolPlayerId:"
				+ selectedPoolPlayerId);
		UserTeam userTeam = userTeamDao.getTeamWithPlayers(userTeamId);
		if (userTeam != null) {
			UserPlayer existingPlayer = getExistingUserPlayer(userTeam, selectedPoolPlayerId);
			if (existingPlayer != null) {
				if (!isAlreadyTeamPlayer(userTeam, poolPlayerId)) {
					PoolPlayer poolPlayer = poolService.getPoolPlayer(poolPlayerId);
					if (poolPlayer != null) {
						long total = userTeam.getAvailableMoney() + existingPlayer.getPoolPlayer().getPlayerPrice();
						logger.info("Checking team's money: " + total + " poolPlayer's price: " + poolPlayer.getPlayerPrice());
						if (total > poolPlayer.getPlayerPrice()) {
							// remove the player from the user team as well
							userTeam.getUserPlayers().remove(existingPlayer);
							userTeamDao.saveOrUpdate(userTeam);
							// Drop old player
							logger.info("Dropping player: " + existingPlayer.getPoolPlayer().getId());
							setPlayerStatus(userTeamId, selectedPoolPlayerId, UserPlayerStatus.DROPPED.name());
							// Add new pool player
							logger.info("Adding trade player: " + poolPlayerId);
							if (UserPlayerStatus.SUBSTITUTE.equals(existingPlayer.getStatus())) {
								logger.info("Adding new player as substitute...");
								addPlayerToUserTeam(user, userTeamId, poolPlayerId, BlockType.SUBSTITUTE.name());
							} else {
								addPlayerToUserTeam(user, userTeamId, poolPlayerId, existingPlayer.getPoolPlayer().getPlayer().getBlock()
										.name());
							}
							// Update the team's status back to complete
							userTeam.setStatus(UserTeamStatus.COMPLETE);
							userTeamDao.saveOrUpdate(userTeam);
							// Save the trade
							saveTrade(userTeam, poolPlayer);
						} else {
							logger.error("Too expensive selectedPoolPlayerId: " + selectedPoolPlayerId);
							throw new InvalidPlayerException("You don't have enough money to trade this player!");
						}
					} else {
						logger.error("Invalid selectedPoolPlayerId: " + selectedPoolPlayerId);
						throw new InvalidPlayerException("Invalid pool player");
					}
				} else {
					logger.error("PoolPlayerId: " + poolPlayerId + " is already on the user's team");
					throw new InvalidPlayerException("Player is already on your team");
				}
			} else {
				logger.error("Invalid existingPlayer: " + selectedPoolPlayerId);
				throw new InvalidPlayerException("Unknown selected player");
			}
		} else {
			throw new LeagueException("Unknown user team");
		}
	}

	private boolean isAlreadyTeamPlayer(UserTeam userTeam, long selectedPoolPlayerId) {
		for (UserPlayer userPlayer : userTeam.getUserPlayers()) {
			if (userPlayer.getPoolPlayer().getId().longValue() == selectedPoolPlayerId) {
				logger.error("PoolPlayer:" + selectedPoolPlayerId + " is already on the user's team: " + " " + userTeam.getId());
				return true;
			}
		}
		return false;
	}

	private UserPlayer getExistingUserPlayer(UserTeam userTeam, long poolPlayerId) {
		for (UserPlayer userPlayer : userTeam.getUserPlayers()) {
			if (userPlayer.getPoolPlayer().getId().longValue() == poolPlayerId) {
				return userPlayer;
			}
		}
		return null;
	}

	@WriteTransaction
	private void saveTrade(UserTeam userTeam, PoolPlayer poolPlayer) throws LeagueException {
		UserTeamTrade trade = new UserTeamTrade();
		trade.setPlayingWeek(getCurrentPlayingWeek());
		trade.setPoolPlayer(poolPlayer);
		trade.setUserTeam(userTeam);
		userTeamTradeDao.save(trade);
		logger.info("Save trade: " + trade);
	}

	@WriteTransaction
	@Override
	public void calculateNewRanking(long leagueId, long currentPlayingWeekId) throws LeagueException {
		List<Long> ids = userTeamDao.getActiveUserTeams(leagueId);
		if (ids != null && ids.size() > 0) {
			userTeamDao.calculateNewRanking(ids, leagueService.getUsersPlayingWeekPoints(leagueId));
		}
	}
}