package com.africaapps.league.service.task;

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
		MatchFilter matchFilter = null;/*new MatchFilter() {			
			@Override
			public boolean isValidMatch(Integer matchId, Date matchDateTime) {
				if (matchId < 1900) {
					return true;
				} else {
					return false;
				}
			}
		};*/
		feedService.processFeed(feedSettings.getLeagueName(), feedSettings.getWsdlUrl(), feedSettings.getUsername(), feedSettings.getPassword(), matchFilter);
		logger.info("Task: completed processing matches");
	}
}
