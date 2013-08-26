package com.africaapps.league.service.player;

import java.util.List;

import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.league.Player;
import com.africaapps.league.model.league.PlayerMatchStatistic;
import com.africaapps.league.model.league.Position;

public interface PlayerService {

	public Player getPlayer(int playerId) throws LeagueException;
	public Player getPlayer(long id) throws LeagueException;
	public void savePlayer(Player player) throws LeagueException;
	
	public Position getPosition(long leagueTypeId, int positionNumber) throws LeagueException;
	public void savePosition(Position position) throws LeagueException;
	
	public void savePlayerMatchStatistic(long leagueTypeId, PlayerMatchStatistic matchStat) throws LeagueException;
	
	public List<Player> getTeamPlayersByType(long teamId, String type) throws LeagueException;
	public List<Player> getTeamPlayers(long teamId) throws LeagueException;
	public List<Player> getTeamPlayers(int teamId) throws LeagueException;	
}
