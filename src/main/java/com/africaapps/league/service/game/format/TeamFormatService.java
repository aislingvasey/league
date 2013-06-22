package com.africaapps.league.service.game.format;

import java.util.List;

import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.game.TeamFormat;

public interface TeamFormatService {

	public TeamFormat getDefaultFormat(long leagueTypeId) throws LeagueException;
	
	public List<TeamFormat> getTeamFormats(long leagueTypeId) throws LeagueException;
	
	public TeamFormat getTeamFormat(Long teamFormatId) throws LeagueException;
		
}
