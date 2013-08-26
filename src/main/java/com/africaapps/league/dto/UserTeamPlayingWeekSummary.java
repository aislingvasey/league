package com.africaapps.league.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.africaapps.league.model.game.PlayingWeek;

public class UserTeamPlayingWeekSummary extends BaseDto {

	private static final long serialVersionUID = 1L;

	private Map<PlayingWeek, List<PlayerSummary>> summaries;
	
	public UserTeamPlayingWeekSummary() {
		this.summaries = new HashMap<PlayingWeek, List<PlayerSummary>>();
	}

	public void addPlayerSummary(PlayingWeek week, PlayerSummary player) {
		if (!summaries.containsKey(week)) {
			summaries.put(week, new ArrayList<PlayerSummary>());
		}
		summaries.get(week).add(player);
	}

	public Map<PlayingWeek, List<PlayerSummary>> getSummaries() {
		return summaries;
	}
}