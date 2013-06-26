package com.africaapps.league.service.task;

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
		MatchFilter matchFilter = null; /*new MatchFilter() {			
			@Override
			public boolean isValidMatch(Integer matchId, Date matchDateTime) {
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.YEAR, 2012);
				calendar.set(Calendar.MONTH, 8); //september
				calendar.set(Calendar.DAY_OF_MONTH, 31);
				calendar.set(Calendar.HOUR_OF_DAY, 23);
				calendar.set(Calendar.SECOND, 59);
				calendar.set(Calendar.MILLISECOND, 999);				
				if (matchDateTime.getTime() < calendar.getTime().getTime()) {
					logger.info("MatchDateTime: "+matchDateTime+" is valid for processing");
					return true;
				} else {
					logger.info("MatchDateTime: "+matchDateTime+" is invalid for processing");
					return false;
				}
			}
		};*/
		feedService.processFeed(feedSettings.getLeagueName(), feedSettings.getWsdlUrl(), feedSettings.getUsername(), feedSettings.getPassword(), matchFilter);
		logger.info("Task: completed processing matches");
	}
}
