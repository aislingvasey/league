package com.africaapps.league.service.cache;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.league.Player;
import com.africaapps.league.model.league.PlayerMatch;
import com.africaapps.league.model.league.Position;
import com.africaapps.league.model.league.Event;

@Service
public class MemoryCacheServiceImpl implements CacheService {
	
	//Save/cache each player's id and their corresponding PlayerMatch instances per match
	private Map<Long, Map<Long, PlayerMatch>> playerMatchData = new HashMap<Long, Map<Long, PlayerMatch>>();	
	
	private Map<Integer, Player> players = new HashMap<Integer, Player>();
	
	private Map<Long, Map<Integer, Event>> events = new HashMap<Long, Map<Integer, Event>>();
	
	private Map<Long, Map<Integer, Position>> leaguePositions = new HashMap<Long, Map<Integer,Position>>();

	@Override
	public PlayerMatch getPlayerMatch(Long matchId, Long playerId) throws LeagueException {
		if (playerMatchData.containsKey(matchId)) {
			Map<Long, PlayerMatch> playerMatches = playerMatchData.get(matchId);
			if (playerMatches.containsKey(playerId)) {
				return playerMatches.get(playerId);
			}
		}
		return null;
	}
	
	@Override
	public void setPlayerMatch(Long matchId, Long playerId, PlayerMatch playerMatch) throws LeagueException {
		if (playerMatchData.containsKey(matchId)) {
			Map<Long, PlayerMatch> playerMatches = playerMatchData.get(matchId);
			if (playerMatches.containsKey(playerId)) {
				playerMatches.remove(playerMatch);
			}
			playerMatches.put(playerId, playerMatch);
		} else {
			Map<Long, PlayerMatch> playerMatches = new HashMap<Long, PlayerMatch>();
			playerMatches.put(playerId, playerMatch);
			playerMatchData.put(matchId, playerMatches);
		}
	}

	@Override
	public Player getPlayer(Integer playerId) throws LeagueException {
		return players.get(playerId);
	}

	@Override
	public void setPlayer(Player player) throws LeagueException {
		players.put(player.getPlayerId(), player);
	}
	
	@Override
	public Event getEvent(Long leagueTypeId, Integer eventId) throws LeagueException {
		if (events.containsKey(leagueTypeId)) {
			Map<Integer, Event> e = events.get(leagueTypeId);
			if (e.containsKey(eventId)) {
				return e.get(eventId);
			}
		}
		return null;
	}
	
	@Override
	public void setEvent(Long leagueTypeId, Event event) throws LeagueException {
		if (events.containsKey(leagueTypeId)) {
			Map<Integer, Event> e = events.get(leagueTypeId);
			if (e.containsKey(event.getEventId())) {
				e.remove(event);
			}
			e.put(event.getEventId(), event);
		} else {
			Map<Integer, Event> e = new HashMap<Integer, Event>();
			e.put(event.getEventId(), event);
			events.put(leagueTypeId, e);
		}
	}

	@Override
	public Position getPosition(Long leagueTypeId, Integer positionNumber) throws LeagueException {
		if (leaguePositions.containsKey(leagueTypeId)) {
			Map<Integer, Position> poss = leaguePositions.get(leagueTypeId);
			if (poss.containsKey(positionNumber)) {
				return poss.get(positionNumber);
			}
		}
		return null;
	}

	@Override
	public void setPosition(Long leagueTypeId, Position position) throws LeagueException {
		if (leaguePositions.containsKey(leagueTypeId)) {
			Map<Integer, Position> poss = leaguePositions.get(leagueTypeId);
			if (poss.containsKey(position.getPositionNumber())) {
				poss.remove(position);
			}
			poss.put(position.getPositionNumber(), position);
		} else {
			Map<Integer, Position> poss = new HashMap<Integer, Position>();
			poss.put(position.getPositionNumber(), position);
			leaguePositions.put(leagueTypeId, poss);
		}
	}
}