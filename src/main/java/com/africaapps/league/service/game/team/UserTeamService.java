package com.africaapps.league.service.game.team;

import java.util.List;

import com.africaapps.league.dto.PlayerMatchEventSummary;
import com.africaapps.league.dto.PlayerMatchSummary;
import com.africaapps.league.dto.UserPlayerSummary;
import com.africaapps.league.dto.UserTeamSummary;
import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.game.PoolPlayer;
import com.africaapps.league.model.game.TeamFormat;
import com.africaapps.league.model.game.User;
import com.africaapps.league.model.game.UserLeague;
import com.africaapps.league.model.game.UserTeam;

public interface UserTeamService {

	public List<UserTeam> getTeams(long userId) throws LeagueException;
	
	public void saveTeam(UserTeam userTeam) throws LeagueException;

	public UserTeam getTeam(long userId, String teamName) throws LeagueException;
	public UserTeam getTeam(long userTeamId) throws LeagueException;
	
	public UserLeague getDefaultUserLeague() throws LeagueException;
	
	public TeamFormat getDefaultTeamFormat(long leagueTypeId) throws LeagueException;
	public List<TeamFormat> getTeamFormats(long leagueTypeId) throws LeagueException;
	public void setTeamFormat(Long userTeamId, Long formatId) throws LeagueException;
	
	public UserTeamSummary getTeamWithPlayers(long teamId) throws LeagueException;
	
	public List<UserPlayerSummary> getTeamPlayers(long teamId, long userTeamId, String type) throws LeagueException;
	public UserPlayerSummary getTeamPlayer(long teamId, long poolPlayerId) throws LeagueException;
	
	public String addPlayerToUserTeam(User user, long userTeamId, long teamId, long poolPlayerId, String playerType) throws LeagueException;
	public void setPlayerStatus(long teamId, long poolPlayerId, String status) throws LeagueException;
	
	public void addPointsForPoolPlayer(PoolPlayer poolPlayer, int playerScore) throws LeagueException;
	
	public String setTeam(User user, Long userTeamId) throws LeagueException;
	
	public List<PlayerMatchSummary> getPoolPlayerMatches(Long poolPlayerId) throws LeagueException;
	public List<PlayerMatchEventSummary> getPoolPlayerMatchEvents(Long poolPlayerId, Long matchId) throws LeagueException;
}
