package com.africaapps.league.service.cache;

import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.league.Player;
import com.africaapps.league.model.league.PlayerMatch;
import com.africaapps.league.model.league.Position;
import com.africaapps.league.model.league.Event;

public interface CacheService {

	//TODO make these all league or league type specific
	
	public PlayerMatch getPlayerMatch(Long matchId, Long playerId) throws LeagueException;
	
	public void setPlayerMatch(Long matchId, Long playerId, PlayerMatch playerMatch) throws LeagueException;
	
	public Player getPlayer(Integer playerId) throws LeagueException;
	
	public void setPlayer(Player player) throws LeagueException;
	
	public Event getEvent(Long leagueTypeId, Integer eventId) throws LeagueException;
	public void setEvent(Long leagueTypeId, Event event) throws LeagueException;
	
	public Position getPosition(Long leagueTypeId, Integer positionNumber) throws LeagueException;
	public void setPosition(Long leagueTypeId, Position position) throws LeagueException;	
}
