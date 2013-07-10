package com.africaapps.league.service.task;

import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.service.feed.FeedService;
import com.africaapps.league.service.feed.FeedSettings;
import com.africaapps.league.service.feed.MatchFilter;

@Service("service.task")
public class TaskServiceImpl implements TaskService {

	@Autowired
	private FeedSettings feedSettings;
	@Autowired
	private FeedService feedService;
	
	private static Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);
	
  //TODO add retrys for failures, save status, datetime, etc
	@Override
	public void processMatches() throws LeagueException {
		logger.info("Task: starting to process matches...");
		//TODO added filter for testing only - remove later
		MatchFilter matchFilter = new MatchFilter() {			
			@Override
			public boolean isValidMatch(Integer matchId, Date matchDateTime) {
				logger.info("Match date: "+matchDateTime);
				//Tuesday - July 2012, Wednesday - August 2012, Thursday - September 2012				
				Calendar calendar = Calendar.getInstance();
				int today = calendar.get(Calendar.DAY_OF_WEEK);
				calendar.setTime(matchDateTime);
				if (today == Calendar.TUESDAY) {
					if (calendar.get(Calendar.MONTH) == Calendar.AUGUST && calendar.get(Calendar.YEAR) == 2012) {
						return true;
					} else {
						return false;
					}
				} else if (today == Calendar.WEDNESDAY) {
					if (calendar.get(Calendar.MONTH) == Calendar.SEPTEMBER && calendar.get(Calendar.YEAR) == 2012) {
						return true;
					} else {
						return false;
					}
				} else if (today == Calendar.THURSDAY) {
					if (calendar.get(Calendar.MONTH) == Calendar.OCTOBER && calendar.get(Calendar.YEAR) == 2012) {
						return true;
					} else {
						return false;
					}
				} else if (today == Calendar.FRIDAY) {
					if (calendar.get(Calendar.MONTH) == Calendar.NOVEMBER && calendar.get(Calendar.YEAR) == 2012) {
						return true;
					} else {
						return false;
					}
				} else if (today == Calendar.SATURDAY) {
					if (calendar.get(Calendar.MONTH) == Calendar.DECEMBER && calendar.get(Calendar.YEAR) == 2012) {
						return true;
					} else {
						return false;
					}
				} else if (today == Calendar.SUNDAY) {
					if (calendar.get(Calendar.MONTH) == Calendar.FEBRUARY && calendar.get(Calendar.YEAR) == 2013) {
						return true;
					} else {
						return false;
					}
				} else if (today == Calendar.MONDAY) {
					if (calendar.get(Calendar.MONTH) == Calendar.MARCH && calendar.get(Calendar.YEAR) == 2013) {
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			}
		};
		feedService.processFeed(feedSettings.getLeagueName(), feedSettings.getWsdlUrl(), feedSettings.getUsername(), feedSettings.getPassword(), matchFilter);
		logger.info("Task: completed processing matches");
	}

	@Override
	public void onPlayingWeekEnd() throws LeagueException {
		logger.info("Task: processing playing week end...");
		// TODO step 1: check all user teams and make sure they have 11 players contributing points, otherwise add subs
		// TODO step 2: recalculate pool players price using this week's points
		logger.info("Task: completed playing week end");
	}
}