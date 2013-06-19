package com.africaapps.league.service.feed;

import org.datacontract.schemas._2004._07.livemediastructs.MatchFilActionStruct;
import org.datacontract.schemas._2004._07.livemediastructs.TeamStruct;

import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.game.Pool;
import com.africaapps.league.model.league.League;
import com.africaapps.league.model.league.LeagueSeason;

public interface FeedService {

	public void processFeed(String leagueName, String wsdlUrl, String username, String password, MatchFilter matchFilter) 
		throws LeagueException;
	
	public boolean isProcessedMatch(long leagueSeasonId, int matchId) throws LeagueException;
	public void saveTeamAndPlayers(League league, LeagueSeason leagueSeason, Pool pool, TeamStruct teamStruct) throws LeagueException;
	public void processMatch(League league, LeagueSeason leagueSeason, MatchFilActionStruct matchStruct) throws LeagueException;
}
