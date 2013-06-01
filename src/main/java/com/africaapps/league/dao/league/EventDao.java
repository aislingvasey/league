package com.africaapps.league.dao.league;

import java.util.List;

import com.africaapps.league.model.league.Event;

public interface EventDao {

	public void saveOrUpdate(Event event);
	
	public Event getEvent(long leagueTypeId, int eventId);
	
	public List<Event> getEvents(long leagueTypeId);
	
}
