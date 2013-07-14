package com.africaapps.league.dto;

public class UserTeamListSummary extends BaseDto {

	private static final long serialVersionUID = 1L;

	protected Long userId;
	protected Long teamId;
	protected String teamStatus;
	protected String teamName;
	protected Long availableMoney;
	protected Integer currentScore;
	protected Long leagueId;
	protected String leagueName;
	protected Integer positionInLeague;	
	protected Integer leagueCount;
	protected boolean canTrade;
	
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
	public String getTeamStatus() {
		return teamStatus;
	}
	public void setTeamStatus(String teamStatus) {
		this.teamStatus = teamStatus;
	}
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public Long getAvailableMoney() {
		return availableMoney;
	}
	public void setAvailableMoney(Long availableMoney) {
		this.availableMoney = availableMoney;
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
	public Integer getPositionInLeague() {
		return positionInLeague;
	}
	public void setPositionInLeague(Integer positionInLeague) {
		this.positionInLeague = positionInLeague;
	}
	public Integer getCurrentScore() {
		return currentScore;
	}
	public void setCurrentScore(Integer currentScore) {
		this.currentScore = currentScore;
	}
	public Integer getLeagueCount() {
		return leagueCount;
	}
	public void setLeagueCount(Integer leagueCount) {
		this.leagueCount = leagueCount;
	}
	public boolean getCanTrade() {
		return canTrade;
	}
	public void setCanTrade(boolean canTrade) {
		this.canTrade = canTrade;
	}
}
