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
import com.africaapps.league.dto.PlayerMatchEventSummary;
import com.africaapps.league.dto.PlayerMatchSummary;
import com.africaapps.league.dto.UserPlayerSummary;
import com.africaapps.league.dto.UserTeamListSummary;
import com.africaapps.league.dto.UserTeamScoreHistorySummary;
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
import com.africaapps.league.model.game.UserTeamStatus;
import com.africaapps.league.model.league.BlockType;
import com.africaapps.league.model.league.Match;
import com.africaapps.league.model.league.Player;
import com.africaapps.league.service.game.format.TeamFormatService;
import com.africaapps.league.service.game.league.UserLeagueService;
import com.africaapps.league.service.game.player.UserPlayerService;
import com.africaapps.league.service.match.MatchService;
import com.africaapps.league.service.player.PlayerService;
import com.africaapps.league.service.pool.PoolService;
import com.africaapps.league.service.team.TeamService;
import com.africaapps.league.service.transaction.ReadTransaction;
import com.africaapps.league.service.transaction.WriteTransaction;

@Service
public class UserTeamServiceImpl implements UserTeamService {

	//TODO move to properties file?
	private static final int SQUAD_SIZE = 15;
	private static final int GOALKEEPER_COUNT = 1;
	private static final int SUBSTITUTE_COUNT = 4;

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
	@Autowired
	private MatchService matchService;
	@Autowired
	private TeamService teamService;

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
			TeamFormat newFormat = teamFormatService.getTeamFormat(teamFormatId);
			//Check if the player numbers are ok for the new format
			Map<BlockType, Integer> playerTypeCounts = getPlayerTypeCounts(userTeam);
			logger.info("Checking player's count: " + playerTypeCounts.toString());
			if (playerTypeCounts.get(BlockType.DEFENDER) > newFormat.getDefenderCount()) {
				throw new InvalidPlayerException("You can only have " + newFormat.getDefenderCount() + " defenders on the team to use the new format");
			}
			if (playerTypeCounts.get(BlockType.MIDFIELDER) > newFormat.getMidfielderCount()) {
				throw new InvalidPlayerException("You can only have " + newFormat.getMidfielderCount() + " midfielders on the team to use the new format");
			}
			if (playerTypeCounts.get(BlockType.STRIKER) > newFormat.getStrikerCount()) {
				throw new InvalidPlayerException("You can only have " + newFormat.getStrikerCount() + " strikers on the team to use the new format");
			}
			if (playerTypeCounts.get(BlockType.GOALKEEPER) > GOALKEEPER_COUNT) {
				throw new InvalidPlayerException("You can only have "+GOALKEEPER_COUNT+" goalkeeper on the team to use the new format");
			}
			if (playerTypeCounts.get(BlockType.SUBSTITUTE) > SUBSTITUTE_COUNT) {
				throw new InvalidPlayerException("You can only have "+SUBSTITUTE_COUNT+" substitutes on the team to use the new format");
			}
			//Set the new format
			userTeam.setCurrentFormat(newFormat);			
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
			summary.setTeamStatus(team.getStatus().name());
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
					//Increase team;s available money
					UserTeam userTeam = userTeamDao.getTeam(userTeamId);
					userTeam.setAvailableMoney(userTeam.getAvailableMoney() + userPlayer.getPoolPlayer().getPlayerPrice());
				  //Update team's status
					if (userTeam.getUserPlayers().size() < SQUAD_SIZE && userTeam.getStatus() == UserTeamStatus.COMPLETE) {
						userTeam.setStatus(UserTeamStatus.INCOMPLETE);
						logger.info("UserTeam is no longer complete: "+userTeamId);
					}
					userTeamDao.saveOrUpdate(userTeam);
				} else {
					if (s == UserPlayerStatus.PLAYER && userPlayer.getStatus() == UserPlayerStatus.SUBSTITUTE) {
						//Check number of existing players of the specified block
						UserTeam userTeam = userTeamDao.getTeam(userTeamId);
						checkValidPlayerType(userTeam, userPlayer.getPoolPlayer().getPlayer().getBlock());
					}
					//Otherwise update the player's status
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
				DecimalFormat df = new DecimalFormat("R#,###");
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
		if (getTeamPlayersCount(userTeam) >= SQUAD_SIZE) {
			throw new InvalidPlayerException("Too many players on the squad!");
		}
		Map<BlockType, Integer> playerTypeCounts = getPlayerTypeCounts(userTeam);
		logger.info("Checking player's count: " + playerTypeCounts.toString()+" for block: "+block);
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
				userTeamScoreHistoryDao.save(history);
				logger.info("Saved capatin UserTeam's score history: " + history);
			}
		} else {
			logger.info("No current UserTeams for Captain's poolPlayer:" + poolPlayer.getId() + " " + poolPlayer.getPlayer().getFirstName()
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

	@WriteTransaction
	@Override
	public String setTeam(User user, Long userTeamId) throws InvalidPlayerException, LeagueException {
		logger.info("Setting team for user:" + user.getUsername() + " userTeamId:" + userTeamId);
		UserTeam userTeam = userTeamDao.getTeamWithPlayers(userTeamId);
		if (userTeam != null) {
			if (userTeam.getUserPlayers().size() == SQUAD_SIZE) {
				String message = checkPlayersCount(userTeam);
				if (message != null) {
					return message;
				}
				UserPlayer captain = getUserTeamCaptain(userTeam);
				if (captain == null) {
					throw new InvalidPlayerException("You need to assign a captain");
				}
				//Update the team's status
				userTeam.setStatus(UserTeamStatus.COMPLETE);
				userTeamDao.saveOrUpdate(userTeam);
				logger.info("Updated the status of the userTeam: "+userTeam);
				return "Your team is complete and ready to play!";
			} else {
				throw new InvalidPlayerException("You need " + SQUAD_SIZE + " players in your squad");
			}
		} else {
			throw new LeagueException("Unknown team");
		}
	}
	
	private String checkPlayersCount(UserTeam userTeam) throws LeagueException {
		Map<BlockType, Integer> playerTypeCounts = getPlayerTypeCounts(userTeam);
		logger.info("Checking player's count: " + playerTypeCounts.toString());
		if (playerTypeCounts.get(BlockType.DEFENDER) != userTeam.getCurrentFormat().getDefenderCount()) {
			return "You need " + userTeam.getCurrentFormat().getDefenderCount() + " defenders on the team";
		}
		if (playerTypeCounts.get(BlockType.MIDFIELDER) != userTeam.getCurrentFormat().getMidfielderCount()) {
			return "You need " + userTeam.getCurrentFormat().getMidfielderCount() + " midfielders on the team";
		}
		if (playerTypeCounts.get(BlockType.STRIKER) != userTeam.getCurrentFormat().getStrikerCount()) {
			return "You need " + userTeam.getCurrentFormat().getStrikerCount() + " strikers on the team";
		}
		if (playerTypeCounts.get(BlockType.GOALKEEPER) != GOALKEEPER_COUNT) {
			return "You need at least "+GOALKEEPER_COUNT+" goalkeeper on the team";
		}
		if (playerTypeCounts.get(BlockType.SUBSTITUTE) != SUBSTITUTE_COUNT) {
			return "You need at least "+SUBSTITUTE_COUNT+" substitutes on the team";
		}
		return null;
	}
	
	private UserPlayer getUserTeamCaptain(UserTeam userTeam) {
		for(UserPlayer userPlayer : userTeam.getUserPlayers()) {
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
		List<UserTeamScoreHistorySummary> scores = userTeamDao.getScoreHistoryByMatch(userTeamId);
		Match match = null;
		Map<Long, Match> matches = new HashMap<Long, Match>();
		for(UserTeamScoreHistorySummary score : scores) {
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
		for(UserTeamScoreHistorySummary score : scores) {
			matchPoints += score.getPlayerPoints();
			//set the player's team name
			if (teams.containsKey(score.getPlayerTeamId())) {
				team = teams.get(score.getPlayerTeamId());
			} else {
				logger.info("Getting team name for id: "+score.getPlayerTeamId());
				team = teamService.getTeamName(score.getPlayerTeamId());
				teams.put(score.getPlayerTeamId(), team);
			}
			score.setTeamOneName(team);
			logger.info("Set player's team name: "+team);
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
}