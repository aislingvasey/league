package com.africaapps.league.service.game.format;

import java.util.List;

import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.model.game.TeamFormat;

public interface TeamFormatService {

	public TeamFormat getDefaultFormat() throws LeagueException;
	
	public List<TeamFormat> getTeamFormats() throws LeagueException;
	
	public TeamFormat getTeamFormat(Long teamFormatId) throws LeagueException;
		
}
