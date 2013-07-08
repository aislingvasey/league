package com.africaapps.league.dao.game;

import com.africaapps.league.model.game.UserTeamTrade;

public interface UserTeamTradeDao {

	public void save(UserTeamTrade userTeamTrade);
	
	public int getUserTeamTradeInWeeks(long leagueSeasonId, long userTeamId, Integer[] playingWeekOrders);
	
}
