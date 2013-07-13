package com.africaapps.league.service.game.player;

import java.util.List;

import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.game.UserPlayer;
import com.africaapps.league.model.league.LeagueSeason;
import com.africaapps.league.model.league.Match;

public interface UserPlayerService {

	public void saveUserPlayer(UserPlayer userPlayer) throws LeagueException;
	
	public UserPlayer getPlayerOnUserTeam(long userTeamId, long playerId) throws LeagueException;
	
	public UserPlayer getCaptain(long userTeamId) throws LeagueException;
	
	public void deleteUserPlayer(UserPlayer userPlayer) throws LeagueException;
	
	public void assignUserPlayerPoints(LeagueSeason leagueSeason, Match match) throws LeagueException;
	
	public List<UserPlayer> getUserTeamSubstitutes(long userTeamId, int subsCount) throws LeagueException;
}
