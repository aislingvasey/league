package com.africaapps.league.service.player;

import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.league.Player;
import com.africaapps.league.model.league.PlayerMatchStats;
import com.africaapps.league.model.league.Position;
import com.africaapps.league.model.league.Statistic;

public interface PlayerService {

	public Player getPlayer(int playerId) throws LeagueException;
	public void savePlayer(Player player) throws LeagueException;
	
	public Position getPosition(long leagueTypeId, int positionNumber) throws LeagueException;
	public void savePosition(Position position) throws LeagueException;
	
	public Statistic getStatistic(long leagueTypeId, int statsId) throws LeagueException;
	public void saveStatistic(Statistic statistic) throws LeagueException;
	
	public void savePlayerMatchStats(PlayerMatchStats playerMatchStats) throws LeagueException;
}
