package com.africaapps.league.service.league;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.africaapps.league.dao.game.PlayingWeekDao;
import com.africaapps.league.dao.league.LeagueDao;
import com.africaapps.league.dao.league.LeagueSeasonDao;
import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.game.PlayingWeek;
import com.africaapps.league.model.league.League;
import com.africaapps.league.model.league.LeagueSeason;
import com.africaapps.league.service.transaction.ReadTransaction;

@Service
public class LeagueServiceImpl implements LeagueService {

	@Autowired
	private LeagueDao leagueDao;
	@Autowired
	private LeagueSeasonDao leagueSeasonDao;
	@Autowired
	private PlayingWeekDao playingWeekDao;
	
	private static Logger logger = LoggerFactory.getLogger(LeagueServiceImpl.class);
	
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

	@ReadTransaction
	@Override
	public PlayingWeek getPlayingWeek(LeagueSeason leagueSeason, Date matchDateTime) throws LeagueException {
		if (leagueSeason != null && matchDateTime != null) {
			PlayingWeek playingWeek = playingWeekDao.get(leagueSeason.getId(), matchDateTime);
			logger.info("Got playing week for leagueSeason:"+leagueSeason.getId()+" matchDateTime:"+matchDateTime+" "+playingWeek);
			//TODO insert if null? need to know start date/time of each week
			return playingWeek;
		}
		return null;
	}
}
