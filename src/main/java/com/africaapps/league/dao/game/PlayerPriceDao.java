package com.africaapps.league.dao.game;

import com.africaapps.league.model.game.PlayerPrice;

public interface PlayerPriceDao {

	public PlayerPrice getPrice(String firstName, String lastName);
	
}
