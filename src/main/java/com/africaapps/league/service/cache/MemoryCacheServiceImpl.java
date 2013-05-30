package com.africaapps.league.service.cache;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.league.Player;
import com.africaapps.league.model.league.PlayerMatch;
import com.africaapps.league.model.league.Position;
import com.africaapps.league.model.league.Statistic;

@Service
public class MemoryCacheServiceImpl implements CacheService {
	
	//Save/cache each player's id and their corresponding PlayerMatch instances per match
	private Map<Long, Map<Long, PlayerMatch>> playerMatchData = new HashMap<Long, Map<Long, PlayerMatch>>();	
	
	private Map<Integer, Player> players = new HashMap<Integer, Player>();
	
	private Map<Long, Map<Integer, Statistic>> statistics = new HashMap<Long, Map<Integer, Statistic>>();
	
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
	public Statistic getStatistic(Long leagueTypeId, Integer statsId) throws LeagueException {
		if (statistics.containsKey(leagueTypeId)) {
			Map<Integer, Statistic> stats = statistics.get(leagueTypeId);
			if (stats.containsKey(statsId)) {
				return stats.get(statsId);
			}
		}
		return null;
	}
	
	@Override
	public void setStatistic(Long leagueTypeId, Statistic statistic) throws LeagueException {
		if (statistics.containsKey(leagueTypeId)) {
			Map<Integer, Statistic> stats = statistics.get(leagueTypeId);
			if (stats.containsKey(statistic.getStatsId())) {
				stats.remove(statistic);
			}
			stats.put(statistic.getStatsId(), statistic);
		} else {
			Map<Integer, Statistic> stats = new HashMap<Integer, Statistic>();
			stats.put(statistic.getStatsId(), statistic);
			statistics.put(leagueTypeId, stats);
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