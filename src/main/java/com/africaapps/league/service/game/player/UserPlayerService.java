package com.africaapps.league.service.game.player;

import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.game.UserPlayer;

public interface UserPlayerService {

	public void saveUserPlayer(UserPlayer userPlayer) throws LeagueException;
	
	public UserPlayer getPlayerOnUserTeam(long userTeamId, long playerId) throws LeagueException;
}
