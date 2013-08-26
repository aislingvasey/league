package com.africaapps.league.service.game.playingweek;

import java.util.Date;

import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.game.PlayingWeek;
import com.africaapps.league.model.league.League;
import com.africaapps.league.model.league.LeagueSeason;

public interface PlayingWeekService {
	
	public PlayingWeek getPlayingWeek(LeagueSeason leagueSeason, Date matchDateTime) throws LeagueException;
	
	public PlayingWeek getPlayingWeek(long id) throws LeagueException;

	public void completeCurrentPlayingWeek(League league, int endDay) throws LeagueException;
	
}
