package com.africaapps.league.dao.game;

import java.util.List;

import com.africaapps.league.model.game.TeamFormat;

public interface TeamFormatDao {

	public TeamFormat getDefault();
	
	public List<TeamFormat> getAll();
	
}
