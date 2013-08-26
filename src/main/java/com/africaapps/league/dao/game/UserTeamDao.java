package com.africaapps.league.dao.game;

import java.util.List;

import com.africaapps.league.dto.PlayerSummary;
import com.africaapps.league.dto.TeamSummary;
import com.africaapps.league.dto.UserTeamListSummary;
import com.africaapps.league.dto.UserTeamScoreHistorySummary;
import com.africaapps.league.model.game.UserTeam;
import com.africaapps.league.model.game.UserTeamStatus;

public interface UserTeamDao {

	public List<UserTeam> getTeams(long userId);
	
	public UserTeam getTeam(long userId, String teamName);
	
	public UserTeam getTeam(long userTeamId);
	
	public void saveOrUpdate(UserTeam userTeam);
	
	public int getTeamCount(long userLeagueId);
	
	public List<TeamSummary> getTeamSummary(long userLeagueId, long userId);
	
	public List<UserTeamListSummary> getTeamListSummary(long userId);
		
	public UserTeam getTeamWithPlayers(long teamId);
	
	public List<UserTeam> getTeamsWithPoolPlayer(long poolPlayerId);
	public List<UserTeam> getTeamsWithCaptain(long poolPlayerId);
	public void addPlayerPoints(List<Long> ids, Double playerPoints);
	
	public List<UserTeamScoreHistorySummary> getScoreHistory(long userTeamId);
	public List<UserTeamScoreHistorySummary> getPlayersScoreHistoryByMatch(Long userTeamId, Long matchId);
	
	public List<PlayerSummary> getHistoryForPlayingWeeks(long userTeamId);
	
	public Long getTeamPoolId(Long userTeamId);
	public UserTeamStatus getUserTeamStatus(long userTeamId);
	
	public Long getAvailableMoney(long userTeamId);
	
	public List<Long> getActiveUserTeams(long leagueId);
	public void calculateNewRanking(List<Long> ids, int perWeekPlayingPoints);
}
