package com.africaapps.league.service.game.team;

import java.util.List;

import com.africaapps.league.dto.UserPlayerSummary;
import com.africaapps.league.dto.UserTeamSummary;
import com.africaapps.league.exception.LeagueException;
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
	
	public TeamFormat getDefaultTeamFormat() throws LeagueException;
	public List<TeamFormat> getTeamFormats(long leagueId) throws LeagueException;
	public void setTeamFormat(Long userTeamId, Long formatId) throws LeagueException;
	
	public UserTeamSummary getTeamWithPlayers(long teamId) throws LeagueException;
	
	public List<UserPlayerSummary> getTeamPlayers(long teamId, long userTeamId, String type) throws LeagueException;
	
	public String addPlayerToUserTeam(User user, long userTeamId, long teamId, long playerId, String playerType) throws LeagueException;
}
