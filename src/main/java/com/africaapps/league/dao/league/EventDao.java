package com.africaapps.league.dao.league;

import java.util.List;

import com.africaapps.league.model.league.BlockType;
import com.africaapps.league.model.league.Event;

public interface EventDao {

	public void saveOrUpdate(Event event);
	
	public Event getEvent(long leagueTypeId, int eventId, BlockType block);
	
	public List<Event> getEvents(long leagueTypeId);
	
}
