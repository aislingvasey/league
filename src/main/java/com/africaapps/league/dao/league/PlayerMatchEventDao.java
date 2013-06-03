package com.africaapps.league.dao.league;

import java.util.List;

import com.africaapps.league.model.league.PlayerMatchEvent;

public interface PlayerMatchEventDao {

	public void saveOrUpdate(PlayerMatchEvent playerMatchEvent);
	
	public List<PlayerMatchEvent> getEvents(long playerMatchId);
	
	public PlayerMatchEvent getEvent(Long playerMatchId, Long statisticId, String matchTime);
	
}
