package com.africaapps.league.service.league;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.africaapps.league.dao.league.LeagueDao;
import com.africaapps.league.dao.league.LeagueDataDao;
import com.africaapps.league.dao.league.LeagueSeasonDao;
import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.game.TeamFormat;
import com.africaapps.league.model.league.League;
import com.africaapps.league.model.league.LeagueData;
import com.africaapps.league.model.league.LeagueSeason;
import com.africaapps.league.service.transaction.ReadTransaction;
import com.africaapps.league.service.transaction.WriteTransaction;

@Service("LeagueService")
public class LeagueServiceImpl implements LeagueService {
	
	//Since the data is not updated anywhere yet, we can cache it in memory
	private Map<Long, LeagueData> leagueDataCache;

	@Autowired
	private LeagueDao leagueDao;
	@Autowired
	private LeagueSeasonDao leagueSeasonDao;
	@Autowired
	private LeagueDataDao leagueDataDao;
	
	private static Logger logger = LoggerFactory.getLogger(LeagueServiceImpl.class);
	
	public LeagueServiceImpl() {
		this.leagueDataCache = new HashMap<Long, LeagueData>();
	}
	
	@ReadTransaction
	@Override
	public League getLeague(String leagueName) throws LeagueException {
		return leagueDao.getLeagueByName(leagueName);
	}

	@ReadTransaction
	@Override
	public LeagueSeason getCurrentSeason(League league) throws LeagueException {
		if (league != null) {
			return leagueSeasonDao.getCurrentSeason(league.getId());
		}
		return null;
	}
	
	@Override
	public LeagueSeason getCurrentSeason(long userTeamId) throws LeagueException {
		return leagueSeasonDao.getCurrentSeasonForUserTeam(userTeamId);
	}

	@Override
	public int getSquadCount(League league) throws LeagueException {
		LeagueData data = getLeagueData(league);
		return data.getSquadSize();
	}

	@Override
	public int getSubstitutesCount(League league) throws LeagueException {
		LeagueData data = getLeagueData(league);
		return data.getNumberOfSubstitutes();
	}

	@Override
	public int getGoalkeepersCount(League league) throws LeagueException {
		LeagueData data = getLeagueData(league);
		return data.getNumberOfGoalkeepers();
	}

	@ReadTransaction
	@Override
	public long getTeamInitialMoney(League league) throws LeagueException {
		LeagueData data = getLeagueData(league);
		return data.getInitTeamMoney();
	}

	@Override
	public TeamFormat getTeamDefaultFormat(League league) throws LeagueException {
		LeagueData data = getLeagueData(league);
		return data.getDefaultTeamFormat();
	}

	@Override
	public Date getLastFeedRun(League league) throws LeagueException {
		LeagueData data = getLeagueData(league);
		return data.getLastFeedRun();
	}
	
	@WriteTransaction
	@Override
	public void setLastFeedRun(League league, Date date) throws LeagueException {
		LeagueData leagueData = null;
		if (leagueDataCache.containsKey(league.getId())) {
			leagueData = leagueDataCache.get(league.getId());
		} else {
			leagueData = getLeagueData(league);
		}
		leagueData.setLastFeedRun(date);
		leagueDataDao.saveOrUpdate(leagueData);
		logger.info("Set last feed run: "+leagueData.getLastFeedRun());
	}

	private LeagueData getLeagueData(League league) throws LeagueException {
		if (league == null) {
			throw new LeagueException("Invalid League to get LeagueData");
		}
		return getLeagueData(league.getId());
	}
	
	private LeagueData getLeagueData(long leagueId) throws LeagueException {
		if (leagueDataCache.containsKey(leagueId)) {
			return leagueDataCache.get(leagueId);
		} else {
			LeagueData leagueData = leagueDataDao.get(leagueId);
			if (leagueData == null) {
				throw new LeagueException("No LeagueData setup for: "+leagueId);
			} else {
				logger.debug("Got leagueData for cache: "+leagueData);
				leagueDataCache.put(leagueId, leagueData);
				return leagueData;
			}
		}
	}

	@Override
	public int getUsersPlayingWeekPoints(long leagueId) throws LeagueException {
		return getLeagueData(leagueId).getUserPointsPlayingWeek();
	}
}
