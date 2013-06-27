package com.africaapps.league.dao.game;

import java.util.Date;

import com.africaapps.league.model.game.PlayingWeek;

public interface PlayingWeekDao {

	public PlayingWeek get(Long leagueSeasonId, Date matchDateTime);
	
}
