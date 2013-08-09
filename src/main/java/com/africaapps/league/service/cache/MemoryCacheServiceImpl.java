package com.africaapps.league.service.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.league.BlockType;
import com.africaapps.league.model.league.LeagueType;
import com.africaapps.league.model.league.Player;
import com.africaapps.league.model.league.PlayerMatch;
import com.africaapps.league.model.league.Position;
import com.africaapps.league.model.league.Statistic;
import com.africaapps.league.service.statistic.StatisticService;

@Service
public class MemoryCacheServiceImpl implements CacheService {
	
	@Autowired
	private StatisticService statisticService;
	
	//Save/cache each player's id and their corresponding PlayerMatch instances per match
	private Map<Long, Map<Long, PlayerMatch>> playerMatchData = new HashMap<Long, Map<Long, PlayerMatch>>();	
	
	private Map<Integer, Player> players = new HashMap<Integer, Player>();
		
	private Map<Long, Map<Integer, Position>> leaguePositions = new HashMap<Long, Map<Integer,Position>>();
	
	//ExternalId to Points
	private Map<Integer, Statistic> goalkeepersStats;
	private Map<Integer, Statistic> defendersStats;
	private Map<Integer, Statistic> midfieldersStats;
	private Map<Integer, Statistic> strikersStats;
	
	private static Logger logger = LoggerFactory.getLogger(MemoryCacheServiceImpl.class);
	
	public MemoryCacheServiceImpl() {
		this.goalkeepersStats = new HashMap<Integer, Statistic>();
		this.defendersStats = new HashMap<Integer, Statistic>();
		this.midfieldersStats = new HashMap<Integer, Statistic>();
		this.strikersStats = new HashMap<Integer, Statistic>();
	}
	
	@Override
	public void clear() {
		playerMatchData.clear();
		players.clear();
		leaguePositions.clear();
		goalkeepersStats.clear();
		defendersStats.clear();
		midfieldersStats.clear();
		strikersStats.clear();
	}

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

	@Override
	public void loadStatistics(LeagueType leagueType) throws LeagueException {
		List<Statistic> stats = statisticService.getStatistics(leagueType);		
		//Per block type => stat
		for(Statistic stat : stats) {
			if (BlockType.GOALKEEPER.equals(stat.getBlock())) {
				goalkeepersStats.put(stat.getExternalId(), stat);
				logger.debug("Put goalkeeper stat: "+stat);
			} else if (BlockType.DEFENDER.equals(stat.getBlock())) {
				defendersStats.put(stat.getExternalId(), stat);
				logger.debug("Put def stat: "+stat);
			} else if (BlockType.MIDFIELDER.equals(stat.getBlock())) {
				midfieldersStats.put(stat.getExternalId(), stat);
				logger.debug("Put mid stat: "+stat);
			} else if (BlockType.STRIKER.equals(stat.getBlock())) {
				strikersStats.put(stat.getExternalId(), stat);
				logger.debug("Put striker stat: "+stat);
			} else {
				logger.debug("Invalid block for stat: "+stat);
			}
		}
	}

	@Override
	public Statistic getStatistic(Integer id, BlockType block) {
		logger.debug("Getting stat for id:"+id+" block:"+block);
		if (BlockType.GOALKEEPER.equals(block)) {
			return goalkeepersStats.get(id);
		}
		if (BlockType.DEFENDER.equals(block)) {
			return defendersStats.get(id);
		}
		if (BlockType.MIDFIELDER.equals(block)) {
			return midfieldersStats.get(id);
		}
		if (BlockType.STRIKER.equals(block)) {
			return strikersStats.get(id);
		}
		logger.error("Invalid statistic block: "+block);
		return null;
	}
}