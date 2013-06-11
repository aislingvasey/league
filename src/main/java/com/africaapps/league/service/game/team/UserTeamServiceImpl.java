package com.africaapps.league.service.game.team;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.africaapps.league.dao.game.UserTeamDao;
import com.africaapps.league.dto.UserPlayerSummary;
import com.africaapps.league.dto.UserTeamSummary;
import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.game.PoolPlayer;
import com.africaapps.league.model.game.TeamFormat;
import com.africaapps.league.model.game.UserLeague;
import com.africaapps.league.model.game.UserPlayer;
import com.africaapps.league.model.game.UserTeam;
import com.africaapps.league.model.league.BlockType;
import com.africaapps.league.model.league.Player;
import com.africaapps.league.service.game.format.TeamFormatService;
import com.africaapps.league.service.game.league.UserLeagueService;
import com.africaapps.league.service.player.PlayerService;
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
	
	@Override
	public UserTeam getTeam(long userTeamId) throws LeagueException {
		return userTeamDao.getTeam(userTeamId);
	}

	@ReadTransaction
	@Override
	public TeamFormat getDefaultTeamFormat() throws LeagueException {
		return teamFormatService.getDefaultFormat();
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
			if (BlockType.Defender.equals(playerSummary.getType())) {
				summary.getDefenders().add(playerSummary);
			} else if (BlockType.Goalkeeper.equals(playerSummary.getType())) {
				summary.getGoalKeepers().add(playerSummary);
			} else if (BlockType.Midfielder.equals(playerSummary.getType())) {
				summary.getMidfielders().add(playerSummary);
			} else if (BlockType.Striker.equals(playerSummary.getType())) {
				summary.getStrikers().add(playerSummary);
			} else {
				logger.error("Unknown player BlockType: "+userPlayer.getPoolPlayer().getPlayer());
			}
		}
	}

	@ReadTransaction
	@Override
	public List<UserPlayerSummary> getTeamPlayers(long teamId, long userTeamId, String type) throws LeagueException {
		List<UserPlayerSummary> summaries = new ArrayList<UserPlayerSummary>();
		UserTeam team = userTeamDao.getTeamWithPlayers(userTeamId);
		if (team != null) {
			List<Player> players = playerService.getTeamPlayersByType(teamId, type);
			UserPlayerSummary summary = null;
			PoolPlayer poolPlayer = null;
			for(Player player : players) {
				poolPlayer = getUserPoolPlayer(team.getUserPlayers(), player.getId());
				if (poolPlayer == null) {
					summary = new UserPlayerSummary();
					summary.setFirstName(player.getFirstName());
					summary.setLastName(player.getLastName());
//				TODO get PoolPlayer and set price and ppid 
//					summary.setPrice(price);
					summaries.add(summary);
				}
			}
		} else {
			logger.error("No userTeam found: "+userTeamId);
		}
		return summaries;
	}
	
	private PoolPlayer getUserPoolPlayer(Set<UserPlayer> players, long playerId) throws LeagueException {
		Iterator<UserPlayer> ps = players.iterator();
		while(ps.hasNext()) {
			UserPlayer up = ps.next();
			if (up.getPoolPlayer().getPlayer().getId().longValue() == playerId) {
				return up.getPoolPlayer();
			}
		}
		return null;
	}
}