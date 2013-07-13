package com.africaapps.league.dao.game;

import java.util.List;

import com.africaapps.league.dto.NeededPlayer;
import com.africaapps.league.model.game.UserTeamScoreHistory;

public interface UserTeamScoreHistoryDao {

	public void save(UserTeamScoreHistory userTeamScoreHistory);
	
	public List<NeededPlayer> getIncompleteTeams(long playingWeekId);
	
	public List<UserTeamScoreHistory> getScoreHistory(long userTeamId, long playerWeekId);
	
}
