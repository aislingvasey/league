package com.africaapps.league.service.task;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.league.League;
import com.africaapps.league.service.feed.FeedService;
import com.africaapps.league.service.feed.FeedSettings;
import com.africaapps.league.service.feed.MatchFilter;
import com.africaapps.league.service.game.playingweek.PlayingWeekService;
import com.africaapps.league.service.league.LeagueService;

@Service("service.task")
public class TaskServiceImpl implements TaskService {

	@Autowired
	private FeedSettings feedSettings;
	@Autowired
	private LeagueService leagueService;
	@Autowired
	private FeedService feedService;
	@Autowired
	private PlayingWeekService playingWeekService;
	
	private static Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);
	
  //TODO add retrys for failures, save status, date time, etc
	@Override
	public void processMatches() throws LeagueException {
		logger.info("Task: starting to process matches...");
		League league = getLeague(feedSettings.getLeagueName());
		int endDay = getEndOfPlayingWeekDay();
		processFeed(league);
		if (isEndOfPlayingWeek(endDay)) {
			playingWeekService.completeCurrentPlayingWeek(league, endDay);
		}
		logger.info("Task: completed processing matches");
	}
	
	private int getEndOfPlayingWeekDay() {
		return feedSettings.getEndOfPlayingWeekDay();
	}
	
	private void processFeed(League league) throws LeagueException {
		MatchFilter matchFilter = createMatchFilter();
		feedService.processFeed(league, feedSettings.getWsdlUrl(), feedSettings.getUsername(), feedSettings.getPassword(), matchFilter);
	}
	
	protected League getLeague(String leagueName) throws LeagueException {
		League league = leagueService.getLeague(leagueName);
		if (league == null) {
			throw new LeagueException("Unknown league for name: " + leagueName);
		} else {
			logger.info("Got league: " + league.getName());
			return league;
		}
	}
	
	private boolean isEndOfPlayingWeek(int endDay) {		
		Calendar now = Calendar.getInstance();
		if (now.get(Calendar.DAY_OF_WEEK) == endDay) {
			return true;
		} else {
			return false;
		}
	}
	
	private MatchFilter createMatchFilter() {
		return null;
		//TODO filter for testing only
//		MatchFilter matchFilter = new MatchFilter() {			
//			@Override
//			public boolean isValidMatch(Integer matchId, Date matchDateTime) {
//				logger.info("Match date: "+matchDateTime);
//				Calendar calendar = Calendar.getInstance();
//				int today = calendar.get(Calendar.DAY_OF_WEEK);
//				calendar.setTime(matchDateTime);
//				if (today == Calendar.SUNDAY) {
//					if (calendar.get(Calendar.MONTH) == Calendar.AUGUST && calendar.get(Calendar.YEAR) == 2012) {
//						return true;
//					} else {
//						return false;
//					}
//				} else if (today == Calendar.MONDAY) {
//					if (calendar.get(Calendar.MONTH) == Calendar.SEPTEMBER && calendar.get(Calendar.YEAR) == 2012) {
//						return true;
//					} else {
//						return false;
//					}
//				} else if (today == Calendar.TUESDAY) {
//					if (calendar.get(Calendar.MONTH) == Calendar.OCTOBER && calendar.get(Calendar.YEAR) == 2012) {
//						return true;
//					} else {
//						return false;
//					}
//				} else if (today == Calendar.WEDNESDAY) {
//					if (calendar.get(Calendar.MONTH) == Calendar.NOVEMBER && calendar.get(Calendar.YEAR) == 2012) {
//						return true;
//					} else {
//						return false;
//					}
//				} else if (today == Calendar.THURSDAY) {
//					if (calendar.get(Calendar.MONTH) == Calendar.DECEMBER && calendar.get(Calendar.YEAR) == 2012) {
//						return true;
//					} else {
//						return false;
//					}
//				} else if (today == Calendar.FRIDAY) {
//					if (calendar.get(Calendar.MONTH) == Calendar.FEBRUARY && calendar.get(Calendar.YEAR) == 2013) {
//						return true;
//					} else {
//						return false;
//					}
//				} else if (today == Calendar.SATURDAY) {
//					if (calendar.get(Calendar.MONTH) == Calendar.MARCH && calendar.get(Calendar.YEAR) == 2013) {
//						return true;
//					} else {
//						return false;
//					}
//				} else {
//					return false;
//				}
//			}
//		};
//		return matchFilter;
	}
}