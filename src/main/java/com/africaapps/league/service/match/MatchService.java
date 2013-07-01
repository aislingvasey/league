package com.africaapps.league.service.match;

import java.util.List;

import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.league.Match;
import com.africaapps.league.model.league.PlayerMatch;
import com.africaapps.league.model.league.PlayerMatchEvent;

public interface MatchService {

	public boolean isProcessedMatch(long leageaSeasonId, int matchId) throws LeagueException;	
	public void saveMatch(Match match) throws LeagueException;
	public Match getMatch(long matchId) throws LeagueException;
	
	public void savePlayerMatch(PlayerMatch playerMatch) throws LeagueException;
	public List<PlayerMatch> getPlayerMatches(long matchId) throws LeagueException;
	public PlayerMatch getPlayerMatch(long matchId, long playerId) throws LeagueException;
	
	public void calculatePlayerScores(Match match) throws LeagueException;
		
	public PlayerMatchEvent getEvent(Long playerMatchId, Long statisticId, String matchTime) throws LeagueException;
	public void savePlayerMatchEvent(PlayerMatchEvent playerMatchEvent);
}
