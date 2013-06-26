package com.africaapps.league.service.player;

import java.util.List;

import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.league.BlockType;
import com.africaapps.league.model.league.Event;
import com.africaapps.league.model.league.LeagueType;
import com.africaapps.league.model.league.Match;
import com.africaapps.league.model.league.Player;
import com.africaapps.league.model.league.PlayerMatchEvent;
import com.africaapps.league.model.league.Position;
import com.africaapps.league.model.league.Team;

public interface PlayerService {

	public Player getPlayer(int playerId) throws LeagueException;
	public Player getPlayer(long id) throws LeagueException;
	public void savePlayer(Player player) throws LeagueException;
	
	public Position getPosition(long leagueTypeId, int positionNumber) throws LeagueException;
	public void savePosition(Position position) throws LeagueException;
	
	public Event getEvent(long leagueTypeId, int eventId) throws LeagueException;
	public Event getEvent(long leagueTypeId, int eventId, BlockType block) throws LeagueException;
	public void saveEvent(Event event) throws LeagueException;
	
	public void savePlayerMatchEvent(long leagueTypeId, PlayerMatchEvent playerMatchEvent) throws LeagueException;
	public void saveCleanSheetForTeam(LeagueType leagueType, Match match, Team team, String matchTime) throws LeagueException;
	
	public List<Player> getTeamPlayersByType(long teamId, String type) throws LeagueException;
	public List<Player> getTeamPlayers(long teamId) throws LeagueException;
	public List<Player> getTeamPlayers(int teamId) throws LeagueException;
}
