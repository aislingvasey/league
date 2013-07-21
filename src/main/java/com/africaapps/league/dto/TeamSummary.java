package com.africaapps.league.dto;

public class TeamSummary extends BaseDto {

	private static final long serialVersionUID = 1L;

	private Long leagueId;
	private Long teamId;
	private String teamName;
	private Integer currentScore;
	private Integer numberOfWeeks;
	private Integer currentRank;
	private Integer positionInLeague;
	private Integer leagueCount;
	private Long ownerId;
	private String username;
	
	public TeamSummary() {
		this.positionInLeague = Integer.valueOf(1);
	}
	
	public Integer getNumberOfWeeks() {
		return numberOfWeeks;
	}

	public void setNumberOfWeeks(Integer numberOfWeeks) {
		this.numberOfWeeks = numberOfWeeks;
	}

	public Integer getCurrentRank() {
		return currentRank;
	}

	public void setCurrentRank(Integer currentRank) {
		this.currentRank = currentRank;
	}

	public TeamSummary(Long teamId, String teamName) {
		this.teamId = teamId;
		this.teamName = teamName;
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

	public Integer getPositionInLeague() {
		return positionInLeague;
	}

	public void setPositionInLeague(Integer positionInLeague) {
		this.positionInLeague = positionInLeague;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public Long getLeagueId() {
		return leagueId;
	}

	public void setLeagueId(Long leagueId) {
		this.leagueId = leagueId;
	}

	public Integer getCurrentScore() {
		return currentScore;
	}

	public void setCurrentScore(Integer currentScore) {
		this.currentScore = currentScore;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getLeagueCount() {
		return leagueCount;
	}

	public void setLeagueCount(Integer leagueCount) {
		this.leagueCount = leagueCount;
	}
}
