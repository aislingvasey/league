package com.africaapps.league.service.game.playingweek;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.africaapps.league.dao.game.PlayingWeekDao;
import com.africaapps.league.dto.NeededPlayer;
import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.game.PlayingWeek;
import com.africaapps.league.model.game.PoolPlayerPointsHistory;
import com.africaapps.league.model.game.UserPlayer;
import com.africaapps.league.model.game.UserTeam;
import com.africaapps.league.model.game.UserTeamScoreHistory;
import com.africaapps.league.model.league.League;
import com.africaapps.league.model.league.LeagueSeason;
import com.africaapps.league.service.game.player.UserPlayerService;
import com.africaapps.league.service.game.team.UserTeamService;
import com.africaapps.league.service.league.LeagueService;
import com.africaapps.league.service.pool.PoolService;
import com.africaapps.league.service.transaction.ReadTransaction;
import com.africaapps.league.service.transaction.WriteTransaction;

@Service
public class PlayingWeekServiceImpl implements PlayingWeekService {

	@Autowired
	private PlayingWeekDao playingWeekDao;	
	@Autowired
	private LeagueService leagueService;
	@Autowired
	private UserTeamService userTeamService;
	@Autowired
	private UserPlayerService userPlayerService;
	@Autowired
	private PoolService poolService;
	
	private static Logger logger = LoggerFactory.getLogger(PlayingWeekServiceImpl.class);
	
	@WriteTransaction
	@Override
	public void completeCurrentPlayingWeek(League league, int endDay) throws LeagueException {
		logger.info("Starting to completeCurrentPlayingWeek...");
		PlayingWeek currentPlayingWeek = getCurrentPlayingWeek(league, endDay);
		int subsCount = leagueService.getSubstitutesCount(league);
		checkUserTeamsPlayers(currentPlayingWeek, subsCount);	
		calculateNewTeamRanking(league, currentPlayingWeek);
		assignNewPoolPlayerPrices(currentPlayingWeek);		
		logger.info("End of completeCurrentPlayingWeek");
	}
	
	@ReadTransaction
	@Override
	public PlayingWeek getPlayingWeek(LeagueSeason leagueSeason, Date matchDateTime) throws LeagueException {
		if (leagueSeason != null) {
			if (matchDateTime != null) {
				logger.info("Getting playingWeek for season:"+leagueSeason.getId()+" matchDateTime: "+matchDateTime);
				PlayingWeek playingWeek = playingWeekDao.get(leagueSeason.getId(), matchDateTime);
				logger.info("Got playing week for leagueSeason:"+leagueSeason.getId()+" matchDateTime:"+matchDateTime+" playingWeek:"+playingWeek);
				return playingWeek;
			} else {
				throw new LeagueException("LeagueSeason: "+leagueSeason + " Unknown playing week for matchDateTime: "+matchDateTime);
			}
		} else {
			throw new LeagueException("Invalid leagueSeason: "+leagueSeason+" to get current playing week");
		}
	}
	
	@ReadTransaction
	private PlayingWeek getCurrentPlayingWeek(League league, int endDay) throws LeagueException {
		logger.info("Specified endDay: "+endDay);
		Calendar now = Calendar.getInstance();
		logger.info("Current date: "+now.get(Calendar.DAY_OF_WEEK));
		while(now.get(Calendar.DAY_OF_WEEK) > endDay) {
			now.add(Calendar.DAY_OF_WEEK, -1);
		}
//		logger.info("Using as end of playing week datetime: "+now.getTime());
//		return getPlayingWeek(leagueService.getCurrentSeason(league), now.getTime());
		
		//TODO for hacking purposes
		Calendar hack = Calendar.getInstance();
		hack.set(Calendar.YEAR, 2012);
		hack.set(Calendar.MONTH, 8);
		hack.set(Calendar.DAY_OF_MONTH, 5);
		logger.info("Using as end of playing week datetime: "+hack.getTime());
		return getPlayingWeek(leagueService.getCurrentSeason(league), hack.getTime());
	}
	
	private void checkUserTeamsPlayers(PlayingWeek currentPlayingWeek, int subsCount) throws LeagueException {
		logger.info("Checking for user teams that are missing players for playingWeek: "+currentPlayingWeek);		
		List<NeededPlayer> teams = userTeamService.getIncompleteUserTeams(currentPlayingWeek);
		for(NeededPlayer team : teams) {
			logger.info("Processing team: "+team.getUserTeamId()+" needed: "+team.getNeeded());			
			int count = (team.getNeeded() > subsCount ? subsCount : team.getNeeded());
			UserTeam userTeam = userTeamService.getTeam(team.getUserTeamId());
			addSubstitutes(currentPlayingWeek, userTeam, count);
		}		
	}
	
	private void addSubstitutes(PlayingWeek currentPlayingWeek, UserTeam userTeam, int count) throws LeagueException {
		List<UserPlayer> substitutes = userPlayerService.getUserTeamSubstitutes(userTeam.getId(), count);
		//TODO future enhancement: check that the UserPlayer substitute can fit into the team's format
		int totalPoints = 0;
		for(UserPlayer userPlayer : substitutes) {				
			logger.info("Adding substitute's points: "+userPlayer);
			List<PoolPlayerPointsHistory> history = poolService.getPoolPlayerHistory(userPlayer.getPoolPlayer().getId(), currentPlayingWeek.getId());
			UserTeamScoreHistory userTeamScoreHistory = null;			
			for(PoolPlayerPointsHistory poolPlayerHistory : history) {
				userTeamScoreHistory = new UserTeamScoreHistory();
				userTeamScoreHistory.setPlayerPoints(poolPlayerHistory.getPlayerPoints());
				userTeamScoreHistory.setPoolPlayer(poolPlayerHistory.getPoolPlayer());
				userTeamScoreHistory.setMatch(poolPlayerHistory.getMatch());
				userTeamScoreHistory.setUserTeam(userTeam);
				userTeamScoreHistory.setPlayingWeek(currentPlayingWeek);
				userTeamService.saveUserTeamScoreHistory(userTeamScoreHistory);
				logger.info("Saved substitute's UserTeamScoreHistory: " + history);
				totalPoints += poolPlayerHistory.getPlayerPoints();
			}				
		}
		userTeamService.addPlayersPoints(userTeam.getId(), totalPoints);
		logger.info("Added totalPoints: "+totalPoints+" to userTeam: "+userTeam.getId());
	}
	
	protected void calculateNewTeamRanking(League league, PlayingWeek currentPlayingWeek) throws LeagueException {
		logger.info("Calculating new rankings for league:"+league+" playingWeek:"+currentPlayingWeek);
		userTeamService.calculateNewRanking(league.getId(), currentPlayingWeek.getId());
		logger.info("Calculated new rankings for league:"+league+" playingWeek:"+currentPlayingWeek);
	}
	
	private void assignNewPoolPlayerPrices(PlayingWeek currentPlayingWeek) {
		//TODO assign new player prices
	}
}
