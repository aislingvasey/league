package com.africaapps.league.dao.league;

import com.africaapps.league.model.league.Position;

public interface PositionDao {

	public void saveAndUpdate(Position position);
	
	public Position getPosition(long leagueTypeId, int position);
	
}
