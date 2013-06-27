package com.africaapps.league.service.player;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.africaapps.league.dao.league.EventDao;
import com.africaapps.league.dao.league.PlayerDao;
import com.africaapps.league.dao.league.PositionDao;
import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.league.BlockType;
import com.africaapps.league.model.league.Event;
import com.africaapps.league.model.league.LeagueType;
import com.africaapps.league.model.league.Match;
import com.africaapps.league.model.league.Player;
import com.africaapps.league.model.league.PlayerMatch;
import com.africaapps.league.model.league.PlayerMatchEvent;
import com.africaapps.league.model.league.Position;
import com.africaapps.league.model.league.Team;
import com.africaapps.league.service.match.MatchService;
import com.africaapps.league.service.transaction.ReadTransaction;
import com.africaapps.league.service.transaction.WriteTransaction;

@Service
public class PlayerServiceImpl implements PlayerService {

	// TODO move to db or properties file
	public static Integer GOAL_EVENT_ID = Integer.valueOf(5);
	public static Integer MATCH_APPEARANCE_EVENT = Integer.valueOf(-1);
	public static Integer GOAL_CONCEEDED_EVENT = Integer.valueOf(-2);
	public static Integer TEAM_CLEAN_SHEET_EVENT = Integer.valueOf(-3);

	@Autowired
	private PlayerDao playerDao;
	@Autowired
	private PositionDao positionDao;
	@Autowired
	private EventDao eventDao;
	@Autowired
	private MatchService matchService;

	private static Logger logger = LoggerFactory.getLogger(PlayerServiceImpl.class);

	@ReadTransaction
	@Override
	public Player getPlayer(int playerId) throws LeagueException {
		return playerDao.getByPlayerId(playerId);
	}

	@ReadTransaction
	@Override
	public Player getPlayer(long id) throws LeagueException {
		return playerDao.getPlayer(id);
	}

	@WriteTransaction
	@Override
	public void savePlayer(Player player) throws LeagueException {
		if (player != null) {
			player.setId(playerDao.getIdByPlayerId(player.getPlayerId()));
			playerDao.saveOrUpdate(player);
		}
	}

	@ReadTransaction
	@Override
	public Position getPosition(long leagueTypeId, int positionNumber) throws LeagueException {
		return positionDao.getPosition(leagueTypeId, positionNumber);
	}

	@WriteTransaction
	@Override
	public void savePosition(Position position) throws LeagueException {
		if (position != null) {
			positionDao.saveAndUpdate(position);
		}
	}

	@ReadTransaction
	@Override
	public Event getEvent(long leagueTypeId, int eventId) throws LeagueException {
		return eventDao.getEvent(leagueTypeId, eventId, null);
	}

	@ReadTransaction
	@Override
	public Event getEvent(long leagueTypeId, int eventId, BlockType block) throws LeagueException {
		return eventDao.getEvent(leagueTypeId, eventId, block);
	}

	@WriteTransaction
	@Override
	public void saveEvent(Event event) throws LeagueException {
		Event existing = eventDao.getEvent(event.getLeagueType().getId(), event.getEventId(), event.getBlock());
		if (existing == null) {
			eventDao.saveOrUpdate(event);
		} else {
			logger.info("Not saving existing event: " + event);
		}
	}

	@WriteTransaction
	@Override
	public void savePlayerMatchEvent(long leagueTypeId, PlayerMatchEvent playerMatchEvent) throws LeagueException {
		if (playerMatchEvent != null) {
			Event event = playerMatchEvent.getEvent();			
			Event adjustedEvent = getPlayerTypeEvent(playerMatchEvent);
			if (adjustedEvent != null) {
				playerMatchEvent.setEvent(adjustedEvent);
			}
			PlayerMatchEvent existing = matchService.getEvent(playerMatchEvent.getPlayerMatch().getId(), playerMatchEvent
					.getEvent().getId(), playerMatchEvent.getMatchTime());
			if (existing == null) {
				matchService.savePlayerMatchEvent(playerMatchEvent);
				logger.info("Saved player's event: "+playerMatchEvent.getPlayerMatch().getPlayer().getPlayerId()+" event:" + playerMatchEvent.getEvent().getDescription()+" "+playerMatchEvent.getEvent().getPoints());
			} else {
				logger.debug("Not saving existing PlayerMatchEvent: " + existing);
			}
			saveOppositePlayerMatchEvent(leagueTypeId, playerMatchEvent, event);
		}
	}

	@ReadTransaction
	private Event getPlayerTypeEvent(PlayerMatchEvent playerMatchEvent) throws LeagueException {
		Player player = playerMatchEvent.getPlayerMatch().getPlayer();
		if (GOAL_EVENT_ID.equals(playerMatchEvent.getEvent().getEventId())) {
			if (player.getBlock() != null) {
				Long leagueTypeId = playerMatchEvent.getEvent().getLeagueType().getId();
				Integer eventId = playerMatchEvent.getEvent().getEventId();
				BlockType block = player.getBlock();
				Event newEvent = getEvent(leagueTypeId, eventId, block);
				logger.info("Replace event:" + playerMatchEvent.getEvent().getEventId() + " with new version:" + newEvent);
				return newEvent;
			} else {
				logger.warn("Unable to get alt event as player's block is null");
			}
		}
		return null;
	}

	@WriteTransaction
	private void saveOppositePlayerMatchEvent(long leagueTypeId, PlayerMatchEvent playerMatchEvent, Event event)
			throws LeagueException {
		// TODO save any opposite events for the current event
		if (GOAL_EVENT_ID.equals(playerMatchEvent.getEvent().getEventId())) {
			saveOppositeTeamGoalConceeded(leagueTypeId, playerMatchEvent, event);
		}
	}

	private void saveOppositeTeamGoalConceeded(long leagueTypeId, PlayerMatchEvent playerMatchEvent, Event event)
			throws LeagueException {
		Match match = playerMatchEvent.getPlayerMatch().getMatch();
		Long teamId = playerMatchEvent.getPlayerMatch().getPlayer().getTeam().getId();
		Long oppTeamId = null;
		if (match.getTeam1().getId().equals(teamId)) {
			oppTeamId = match.getTeam2().getId();
		} else if (match.getTeam2().getId().equals(teamId)) {
			oppTeamId = match.getTeam1().getId();
		} else {
			logger.error("Unknown teamId:" + teamId + " for match: " + match);
		}
		if (oppTeamId != null) {
			// Goalkeeper
			Player player = getPlayer(oppTeamId, BlockType.GOALKEEPER);
			if (player != null) {
				PlayerMatchEvent newEvent = new PlayerMatchEvent();
				newEvent.setEvent(eventDao.getEvent(leagueTypeId, GOAL_CONCEEDED_EVENT, BlockType.GOALKEEPER));
				newEvent.setMatchTime(playerMatchEvent.getMatchTime());
				newEvent.setPlayerMatch(getPlayerMatch(playerMatchEvent.getPlayerMatch().getMatch(), player));
				matchService.savePlayerMatchEvent(newEvent);
				logger.info("Saved goalkeeper conceeds goal: " + newEvent);
			} else {
				logger.error("No goalkeeper found for team:" + oppTeamId);
			}
			// Defenders
			List<Player> defenders = getPlayers(oppTeamId, BlockType.DEFENDER);
			if (defenders.size() > 0) {
				Event defenderConceedGoal = eventDao.getEvent(leagueTypeId, GOAL_CONCEEDED_EVENT, BlockType.DEFENDER);
				if (defenderConceedGoal != null) {
					for (Player defender : defenders) {
						PlayerMatchEvent newEvent2 = new PlayerMatchEvent();
						newEvent2.setEvent(defenderConceedGoal);
						newEvent2.setMatchTime(playerMatchEvent.getMatchTime());
						newEvent2.setPlayerMatch(getPlayerMatch(playerMatchEvent.getPlayerMatch().getMatch(), defender));
						matchService.savePlayerMatchEvent(newEvent2);
						logger.info("Saved defender conceeds goal: " + newEvent2);
					}
				} else {
					logger.error("No event found for defender conceeds goal!");
				}
			} else {
				logger.error("No defenders found for team: " + oppTeamId);
			}
		}
	}

	private PlayerMatch getPlayerMatch(Match match, Player player) throws LeagueException {
		PlayerMatch playerMatch = matchService.getPlayerMatch(match.getId(), player.getId());
		if (playerMatch == null) {
			playerMatch = new PlayerMatch();
			playerMatch.setMatch(match);
			playerMatch.setPlayer(player);
			playerMatch.setPlayerScore(0);
			matchService.savePlayerMatch(playerMatch);
		}
		return playerMatch;
	}

	@ReadTransaction
	private Player getPlayer(long teamId, BlockType block) throws LeagueException {
		return playerDao.getByTeamIdAndBlock(teamId, block);
	}

	@ReadTransaction
	private List<Player> getPlayers(long teamId, BlockType block) throws LeagueException {
		return playerDao.getByTeamIdAndPlayerType(teamId, block);
	}

	@ReadTransaction
	@Override
	public List<Player> getTeamPlayersByType(long teamId, String type) {
		BlockType blockType = getBlockType(type);
		return playerDao.getByTeamIdAndPlayerType(teamId, blockType);
	}

	private BlockType getBlockType(String type) {
		for (BlockType bt : BlockType.values()) {
			if (bt.name().equalsIgnoreCase(type)) {
				return bt;
			}
		}
		logger.error("Unknown BlockType: " + type);
		return null;
	}

	@ReadTransaction
	@Override
	public List<Player> getTeamPlayers(long teamId) {
		return playerDao.getByTeamId(teamId);
	}

	@ReadTransaction
	@Override
	public List<Player> getTeamPlayers(int teamId) {
		return playerDao.getTeamPlayers(teamId);
	}

	@WriteTransaction
	@Override
	public void saveCleanSheetForTeam(LeagueType leagueType, Match match, Team team, String matchTime) throws LeagueException {
		logger.info("Saving clean sheet event for player from team: " + team);
		Event cleanTeamSheetEvent = getEvent(leagueType.getId(), TEAM_CLEAN_SHEET_EVENT);
		if (cleanTeamSheetEvent != null) {
			List<Player> players = getTeamPlayers(team.getTeamId());
			PlayerMatchEvent playerMatchEvent = new PlayerMatchEvent();
			playerMatchEvent.setEvent(cleanTeamSheetEvent);
			playerMatchEvent.setMatchTime(matchTime);
			for (Player player : players) {
				PlayerMatch playerMatch = getPlayerMatch(match, player);
				playerMatchEvent.setPlayerMatch(playerMatch);
				matchService.savePlayerMatchEvent(playerMatchEvent);
				logger.info("Saved team clean sheet event: " + playerMatchEvent);
			}
		} else {
			logger.error("Unknown clean sheet team event!");
		}
	}
}
