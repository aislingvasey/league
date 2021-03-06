package com.africaapps.league.dao.game;

import java.util.List;

import com.africaapps.league.model.game.TeamFormat;

public interface TeamFormatDao {

	public TeamFormat getDefault(long leagueTypeId);
	
	public TeamFormat get(long teamFormatId);
	
	public List<TeamFormat> getAll(long leagueTypeId);
		
}
