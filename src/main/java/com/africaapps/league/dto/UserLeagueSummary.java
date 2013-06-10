package com.africaapps.league.dto;

import java.util.ArrayList;
import java.util.List;

public class UserLeagueSummary extends BaseDto {

	private static final long serialVersionUID = 1L;

	private Long leagueId;
	private String leagueName;
	private int teamCount;
	private int currentCount; //how many teams are being displayed currently
	private List<TeamSummary> userTeamSummary;
	private List<TeamSummary> leagueTeamSummary;
	
	public UserLeagueSummary() {
		this.teamCount = 0;
		this.userTeamSummary = new ArrayList<TeamSummary>();
		this.leagueTeamSummary = new ArrayList<TeamSummary>();
	}
	
	public int getTeamCount() {
		return teamCount;
	}

	public void setTeamCount(int teamCount) {
		this.teamCount = teamCount;
	}

	public void addUserTeamSummary(TeamSummary teamSummary) {
		this.userTeamSummary.add(teamSummary);
	}
	
	public void addLeagueTeamSummary(TeamSummary teamSummary) {
		this.leagueTeamSummary.add(teamSummary);
	}

	public List<TeamSummary> getUserTeamSummary() {
		return userTeamSummary;
	}

	public void setUserTeamSummary(List<TeamSummary> userTeamSummary) {
		this.userTeamSummary = userTeamSummary;
	}

	public List<TeamSummary> getLeagueTeamSummary() {
		return leagueTeamSummary;
	}

	public void setLeagueTeamSummary(List<TeamSummary> leagueTeamSummary) {
		this.leagueTeamSummary = leagueTeamSummary;
	}

	public Long getLeagueId() {
		return leagueId;
	}

	public void setLeagueId(Long leagueId) {
		this.leagueId = leagueId;
	}

	public String getLeagueName() {
		return leagueName;
	}

	public void setLeagueName(String leagueName) {
		this.leagueName = leagueName;
	}

	public int getCurrentCount() {
		return currentCount;
	}

	public void setCurrentCount(int currentCount) {
		this.currentCount = currentCount;
	}
}
