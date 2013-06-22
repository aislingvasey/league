package com.africaapps.league.service.game.player;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.africaapps.league.dao.game.UserPlayerDao;
import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.game.Pool;
import com.africaapps.league.model.game.PoolPlayer;
import com.africaapps.league.model.game.UserPlayer;
import com.africaapps.league.model.game.UserPlayerStatus;
import com.africaapps.league.model.league.LeagueSeason;
import com.africaapps.league.model.league.Match;
import com.africaapps.league.model.league.Player;
import com.africaapps.league.model.league.PlayerMatch;
import com.africaapps.league.service.game.team.UserTeamService;
import com.africaapps.league.service.match.MatchService;
import com.africaapps.league.service.player.PlayerService;
import com.africaapps.league.service.pool.PoolService;
import com.africaapps.league.service.transaction.ReadTransaction;
import com.africaapps.league.service.transaction.WriteTransaction;

@Service
public class UserPlayerServiceImpl implements UserPlayerService {

	@Autowired
	private MatchService matchService;
	@Autowired
	private PoolService poolService;
	@Autowired
	private PlayerService playerService;
	@Autowired
	private UserTeamService userTeamService;
	@Autowired
	private UserPlayerDao userPlayerDao;
	
	private static Logger logger = LoggerFactory.getLogger(UserPlayerServiceImpl.class);
	
	@WriteTransaction
	@Override
	public void saveUserPlayer(UserPlayer userPlayer) throws LeagueException {
		userPlayerDao.saveOrUpdate(userPlayer);		
	}

	@ReadTransaction
	@Override
	public UserPlayer getPlayerOnUserTeam(long userTeamId, long playerId) throws LeagueException {
		return userPlayerDao.getUserPlayer(userTeamId, playerId);
	}

	@Override
	public UserPlayer getCaptain(long userTeamId) throws LeagueException {
		return userPlayerDao.getPlayerByStatus(userTeamId, UserPlayerStatus.CAPTAIN);
	}

	@Override
	public void deleteUserPlayer(UserPlayer userPlayer) throws LeagueException {
		if (userPlayer != null) {
			userPlayerDao.delete(userPlayer);
		}
	}

	@WriteTransaction
	@Override
	public void assignUserPlayerPoints(LeagueSeason leagueSeason, Match match) throws LeagueException {
		if (match != null) {
			Pool pool = poolService.getPool(leagueSeason);
			logger.info("Current pool: "+pool);			
			logger.info("Assigning UserPlayer points for match: "+match);
			List<PlayerMatch> playerMatches = matchService.getPlayerMatches(match.getId());
			for(PlayerMatch playerMatch : playerMatches) {
				logger.info("Adding PlayerMatch: "+playerMatch);
				Player actualPlayer = playerService.getPlayer(playerMatch.getPlayer().getFirstName(), playerMatch.getPlayer().getLastName());
				logger.info("Using actualPlayer: "+actualPlayer);
				PoolPlayer poolPlayer = poolService.getPoolPlayer(pool.getId(), actualPlayer.getId());
				userTeamService.addPointsForPoolPlayer(poolPlayer, playerMatch.getPlayerScore());
			}
		}
	}
}