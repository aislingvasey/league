package com.africaapps.league.service.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.service.feed.FeedService;
import com.africaapps.league.service.feed.FeedSettings;

@Service("service.task")
public class TaskServiceImpl implements TaskService {

	@Autowired
	private FeedSettings feedSettings;
	@Autowired
	private FeedService feedService;
	
	private static Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);
	
	@Override
	public void processMatches() throws LeagueException {
		logger.info("Task: starting to process matches...");
		feedService.processFeed(feedSettings.getLeagueName(), feedSettings.getWsdlUrl(), feedSettings.getUsername(), feedSettings.getPassword());
		logger.info("Task: completed processing matches");
	}
}
