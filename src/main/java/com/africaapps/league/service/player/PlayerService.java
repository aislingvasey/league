package com.africaapps.league.service.player;

import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.league.Player;

public interface PlayerService {

	public Player getPlayer(Long playerId) throws LeagueException;
	
	public void savePlayer(Player player) throws LeagueException;
	
}
