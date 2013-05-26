package com.africaapps.league.service.player;

import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.league.Player;
import com.africaapps.league.model.league.Position;

public interface PlayerService {

	public Player getPlayer(int playerId) throws LeagueException;
	
	public void savePlayer(Player player) throws LeagueException;
	
	public Position getPosition(long leagueTypeId, int positionNumber) throws LeagueException;
	
	public void savePosition(Position position) throws LeagueException;
	
}
