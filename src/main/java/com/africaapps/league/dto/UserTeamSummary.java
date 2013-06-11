package com.africaapps.league.dto;

import java.util.ArrayList;
import java.util.List;

import com.africaapps.league.model.game.TeamFormat;

public class UserTeamSummary extends BaseDto {

	private static final long serialVersionUID = 1L;

	private Long userId;
	private Long teamId;
	private String teamName;
	private Long availableMoney;
	private TeamFormat teamFormat;
	private List<UserPlayerSummary> goalKeepers;
	private List<UserPlayerSummary> defenders;
	private List<UserPlayerSummary> midfielders;
	private List<UserPlayerSummary> strikers;
	
	public UserTeamSummary() {
		this.goalKeepers = new ArrayList<UserPlayerSummary>();
		this.defenders = new ArrayList<UserPlayerSummary>();
		this.midfielders = new ArrayList<UserPlayerSummary>();
		this.strikers = new ArrayList<UserPlayerSummary>();
	}
	
	public boolean getRequiresGoalKeepers() {
		return goalKeepers.size() == 0;
	}
	
	public boolean getRequiresDefenders() {
		return defenders.size() < teamFormat.getDefenderCount();
	}
	
	public boolean getRequiresMidFielders() {
		return midfielders.size() < teamFormat.getMidfielderCount();
	}
	
	public boolean getRequiresStrikers() {
		return strikers.size() < teamFormat.getStrikerCount();
	}
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getTeamId() {
		return teamId;
	}
	public void setTeamId(Long teamId) {
		this.teamId = teamId;
	}
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public TeamFormat getTeamFormat() {
		return teamFormat;
	}
	public void setTeamFormat(TeamFormat teamFormat) {
		this.teamFormat = teamFormat;
	}

	public List<UserPlayerSummary> getGoalKeepers() {
		return goalKeepers;
	}

	public void setGoalKeepers(List<UserPlayerSummary> goalKeepers) {
		this.goalKeepers = goalKeepers;
	}

	public List<UserPlayerSummary> getDefenders() {
		return defenders;
	}

	public void setDefenders(List<UserPlayerSummary> defenders) {
		this.defenders = defenders;
	}

	public List<UserPlayerSummary> getMidfielders() {
		return midfielders;
	}

	public void setMidfielders(List<UserPlayerSummary> midfielders) {
		this.midfielders = midfielders;
	}

	public List<UserPlayerSummary> getStrikers() {
		return strikers;
	}

	public void setStrikers(List<UserPlayerSummary> strikers) {
		this.strikers = strikers;
	}

	public Long getAvailableMoney() {
		return availableMoney;
	}

	public void setAvailableMoney(Long availableMoney) {
		this.availableMoney = availableMoney;
	}
	
	public int getPlayersCount() {
		return (defenders.size() + midfielders.size() + goalKeepers.size() + strikers.size());
	}
}
