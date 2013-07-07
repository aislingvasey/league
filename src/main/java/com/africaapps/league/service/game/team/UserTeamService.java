package com.africaapps.league.service.game.team;

import java.util.List;

import com.africaapps.league.dto.PlayerMatchEventSummary;
import com.africaapps.league.dto.PlayerMatchSummary;
import com.africaapps.league.dto.UserPlayerSummary;
import com.africaapps.league.dto.UserTeamListSummary;
import com.africaapps.league.dto.UserTeamScoreHistorySummary;
import com.africaapps.league.dto.UserTeamSummary;
import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.game.PoolPlayer;
import com.africaapps.league.model.game.TeamFormat;
import com.africaapps.league.model.game.User;
import com.africaapps.league.model.game.UserLeague;
import com.africaapps.league.model.game.UserTeam;
import com.africaapps.league.model.league.Match;

public interface UserTeamService {

	public List<UserTeam> getTeams(long userId) throws LeagueException;
	
	public void saveTeam(UserTeam userTeam) throws LeagueException;

	public UserTeam getTeam(long userId, String teamName) throws LeagueException;
	public UserTeam getTeam(long userTeamId) throws LeagueException;
	
	public UserLeague getDefaultUserLeague() throws LeagueException;
	
	public TeamFormat getDefaultTeamFormat(long leagueTypeId) throws LeagueException;
	public List<TeamFormat> getTeamFormats(long leagueTypeId) throws LeagueException;
	public void setTeamFormat(Long userTeamId, Long formatId) throws LeagueException;
	
	public List<UserTeamListSummary> getTeamSummaries(long userId) throws LeagueException;
	
	public UserTeamSummary getTeamWithPlayers(long teamId) throws LeagueException;
	
	public List<UserPlayerSummary> getTeamPlayers(long teamId, long userTeamId, String type) throws LeagueException;
	public UserPlayerSummary getTeamPlayer(long teamId, long poolPlayerId) throws LeagueException;
	
	public String addPlayerToUserTeam(User user, long userTeamId, long teamId, long poolPlayerId, String playerType) throws LeagueException;
	public void setPlayerStatus(long teamId, long poolPlayerId, String status) throws LeagueException;
	
	public void addPointsForPoolPlayer(Match match, PoolPlayer poolPlayer, int playerScore) throws LeagueException;
	public void addPointsForCaptain(Match match, PoolPlayer poolPlayer, int playerScore) throws LeagueException;
	
	public String setTeam(User user, Long userTeamId) throws LeagueException;
	
	//Player's summaries
	public List<PlayerMatchSummary> getPoolPlayerMatches(Long poolPlayerId) throws LeagueException;
	public List<PlayerMatchEventSummary> getPoolPlayerMatchEvents(Long poolPlayerId, Long matchId) throws LeagueException;
	
	//Team's summaries
	public List<UserTeamScoreHistorySummary> getUserTeamScoreHistory(User user, Long teamId) throws LeagueException;
	public List<UserTeamScoreHistorySummary> getUserTeamScorePlayersHistory(User user, Long teamId, Long matchId) throws LeagueException;		
	
	public Long getUserTeamPoolId(Long userTeamId) throws LeagueException;
	
}
