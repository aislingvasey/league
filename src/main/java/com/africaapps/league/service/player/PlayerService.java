package com.africaapps.league.service.player;

import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.league.Player;
import com.africaapps.league.model.league.PlayerMatchEvent;
import com.africaapps.league.model.league.Position;
import com.africaapps.league.model.league.Event;

public interface PlayerService {

	public Player getPlayer(int playerId) throws LeagueException;
	public void savePlayer(Player player) throws LeagueException;
	
	public Position getPosition(long leagueTypeId, int positionNumber) throws LeagueException;
	public void savePosition(Position position) throws LeagueException;
	
	public Event getEvent(long leagueTypeId, int eventId) throws LeagueException;
	public void saveEvent(Event event) throws LeagueException;
	
	public void savePlayerMatchStats(PlayerMatchEvent playerMatchEvent) throws LeagueException;
}
