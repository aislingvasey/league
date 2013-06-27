package com.africaapps.league.service.league;

import java.util.Date;

import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.game.PlayingWeek;
import com.africaapps.league.model.league.League;
import com.africaapps.league.model.league.LeagueSeason;

public interface LeagueService {

	public League getLeague(String leagueName) throws LeagueException;
	
	public LeagueSeason getCurrentSeason(League league) throws LeagueException;
	
	public PlayingWeek getPlayingWeek(LeagueSeason leagueSeason, Date matchDateTime) throws LeagueException;
	
}
