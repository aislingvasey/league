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
	public TeamFormat getDefaultTeamFormat() throws LeagueException {
		return teamFormatService.getDefaultFormat();
	}
	
	@Override
	public List<TeamFormat> getTeamFormats(long leagueId) throws LeagueException {
		//TODO should be specific for a league?
		return teamFormatService.getTeamFormats(/*leagueId*/);
	}

	@WriteTransaction
	@Override
	public void setTeamFormat(Long userTeamId, Long teamFormatId) throws LeagueException {
		UserTeam userTeam = getTeam(userTeamId);
		if (userTeam != null) {
			//TODO specific league?
			userTeam.setCurrentFormat(teamFormatService.getTeamFormat(/* leagueId, */teamFormatId));
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
			playerSummary = new UserPlayerSummary();
			playerSummary.setCurrentScore(userPlayer.getPoolPlayer().getPlayerCurrentScore());
			playerSummary.setFirstName(userPlayer.getPoolPlayer().getPlayer().getFirstName());
			playerSummary.setLastName(userPlayer.getPoolPlayer().getPlayer().getLastName());
			playerSummary.setPoolPlayerId(userPlayer.getPoolPlayer().getId());
			playerSummary.setPrice(userPlayer.getPoolPlayer().getPlayerPrice());
			playerSummary.setType(userPlayer.getPoolPlayer().getPlayer().getBlock());
			playerSummary.setStatus(userPlayer.getStatus());
			if (BlockType.DEFENDER.equals(playerSummary.getType())) {
				summary.getDefenders().add(playerSummary);
			} else if (BlockType.GOALKEEPER.equals(playerSummary.getType())) {
				summary.getGoalKeepers().add(playerSummary);
			} else if (BlockType.MIDFIELDER.equals(playerSummary.getType())) {
				summary.getMidfielders().add(playerSummary);
			} else if (BlockType.STRIKER.equals(playerSummary.getType())) {
				summary.getStrikers().add(playerSummary);
			} else if (BlockType.SUBSTITUTE.equals(playerSummary.getType())) {
				summary.getSubstitutes().add(playerSummary);	
			} else {
				logger.error("Unknown player BlockType: " + userPlayer.getPoolPlayer().getPlayer());
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
			List<Player> players = playerService.getTeamPlayersByType(teamId, type);
			UserPlayerSummary summary = null;
			PoolPlayer poolPlayer = null;
			PoolPlayer pPlayer = null;
			for (Player player : players) {
				poolPlayer = getUserPoolPlayer(team.getUserPlayers(), player.getId());
				if (poolPlayer == null) {
					pPlayer = poolService.getPoolPlayer(poolId, player.getId());
					if (pPlayer != null) {
						summary = new UserPlayerSummary();
						summary.setPoolPlayerId(pPlayer.getId());
						summary.setFirstName(player.getFirstName());
						summary.setLastName(player.getLastName());
						summary.setPrice(pPlayer.getPlayerPrice());
						summary.setCurrentScore(pPlayer.getPlayerCurrentScore());
						summary.setType(player.getBlock());
						summaries.add(summary);
					} else {
						logger.error("Unable to find corresponding pool player for playerId: "+player.getId());
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
			if (up.getPoolPlayer().getPlayer().getId().longValue() == playerId) {
				return up.getPoolPlayer();
			}
		}
		return null;
	}

	@WriteTransaction
	@Override
	public String addPlayerToUserTeam(User user, long userTeamId, long teamId, long playerId, String playerType)
			throws LeagueException {
		UserPlayer userPlayer = userPlayerService.getPlayerOnUserTeam(userTeamId, playerId);
		if (userPlayer == null || UserPlayerStatus.DROPPED.equals(userPlayer.getStatus())) {
			UserTeam userTeam = userTeamDao.getTeam(userTeamId);
			BlockType block = getBlockType(playerType);
			checkValidPlayerType(userTeam, block);
			Pool pool = userTeam.getUserLeague().getPool();
			PoolPlayer poolPlayer = poolService.getPoolPlayer(userTeam.getUserLeague().getPool().getId(), playerId);
			if (poolPlayer.getPlayerPrice() < userTeam.getAvailableMoney()) {
				if (userPlayer == null) {
					userPlayer = new UserPlayer();
					userPlayer.setPool(pool);
					userPlayer.setPoolPlayer(poolPlayer);
					userPlayer.setStatus(UserPlayerStatus.PLAYER);
					userPlayer.setUserTeam(userTeam);
				} else {
					userPlayer.setStatus(UserPlayerStatus.PLAYER);
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
		if (userTeam.getUserPlayers().size() >= 15) {
			throw new InvalidPlayerException("Too many players on the squad!");
		}
		Map<BlockType, Integer> playerTypeCounts = getPlayerTypeCounts(userTeam);
		switch (block) {
		case DEFENDER: 
			if (userTeam.getCurrentFormat().getDefenderCount() >= playerTypeCounts.get(BlockType.DEFENDER)) {
				throw new InvalidPlayerException("Too many defenders on the team!");
			}
			break;
		case MIDFIELDER: 
			if (userTeam.getCurrentFormat().getMidfielderCount() >= playerTypeCounts.get(BlockType.MIDFIELDER)) {
				throw new InvalidPlayerException("Too many midfielders on the team!");
			}
			break;	
		case STRIKER: 
			if (userTeam.getCurrentFormat().getMidfielderCount() >= playerTypeCounts.get(BlockType.STRIKER)) {
				throw new InvalidPlayerException("Too many strikers on the team!");
			}
			break;		
		}
	}
	
	private Map<BlockType, Integer> getPlayerTypeCounts(UserTeam userTeam) throws LeagueException {
		Map<BlockType, Integer> counts = new HashMap<BlockType, Integer>();
		counts.put(BlockType.DEFENDER, Integer.valueOf(0));
		counts.put(BlockType.GOALKEEPER, Integer.valueOf(0));
		counts.put(BlockType.MIDFIELDER, Integer.valueOf(0));
		counts.put(BlockType.STRIKER, Integer.valueOf(0));
		counts.put(BlockType.SUBSTITUTE, Integer.valueOf(0));
		for(UserPlayer userPlayer : userTeam.getUserPlayers()) {
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
			} else {
				int newCount = counts.get(BlockType.SUBSTITUTE) + 1;
				counts.put(BlockType.SUBSTITUTE, newCount);
			}
		}
		return counts;
	}
}