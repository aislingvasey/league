package com.africaapps.league.service.league;

import java.util.Date;

import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.game.TeamFormat;
import com.africaapps.league.model.league.League;
import com.africaapps.league.model.league.LeagueSeason;

public interface LeagueService {

	public League getLeague(String leagueName) throws LeagueException;
	
	public LeagueSeason getCurrentSeason(League league) throws LeagueException;
	
	public int getSquadCount(League league) throws LeagueException;
	
	public int getSubstitutesCount(League league) throws LeagueException;
	
	public int getGoalkeepersCount(League league) throws LeagueException;
	
	public long getTeamInitialMoney(League league) throws LeagueException;
	
	public TeamFormat getTeamDefaultFormat(League league) throws LeagueException;
	
	public Date getLastFeedRun(League league) throws LeagueException;
	public void setLastFeedRun(League league, Date date) throws LeagueException;
	
	public int getUsersPlayingWeekPoints(long leagueId) throws LeagueException;
	
}
