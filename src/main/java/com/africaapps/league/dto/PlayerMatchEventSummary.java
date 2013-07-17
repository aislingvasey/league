package com.africaapps.league.dto;

public class PlayerMatchEventSummary extends BaseDto {

	private static final long serialVersionUID = 1L;

	private Long poolPlayerId;
	private String firstName;      
	private String lastName;  
	private String playerBlock;
	private Long matchId;
	private String matchDate;
	private Integer matchPoints;
	private String description;
	private String matchTime;
	private Integer eventPoints;
	private String teamOne;
	private String teamTwo;
	
	public String getMatchDate() {
		return matchDate;
	}
	public void setMatchDate(String matchDate) {
		this.matchDate = matchDate;
	}
	public String getPlayerBlock() {
		return playerBlock;
	}
	public void setPlayerBlock(String playerBlock) {
		this.playerBlock = playerBlock;
	}
	public Long getMatchId() {
		return matchId;
	}
	public void setMatchId(Long matchId) {
		this.matchId = matchId;
	}
	public Long getPoolPlayerId() {
		return poolPlayerId;
	}
	public void setPoolPlayerId(Long poolPlayerId) {
		this.poolPlayerId = poolPlayerId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getMatchTime() {
		return matchTime;
	}
	public void setMatchTime(String matchTime) {
		this.matchTime = matchTime;
	}
	public Integer getMatchPoints() {
		return matchPoints;
	}
	public void setMatchPoints(Integer matchPoints) {
		this.matchPoints = matchPoints;
	}
	public Integer getEventPoints() {
		return eventPoints;
	}
	public void setEventPoints(Integer eventPoints) {
		this.eventPoints = eventPoints;
	}
	public String getTeamOne() {
		return teamOne;
	}
	public void setTeamOne(String teamOne) {
		this.teamOne = teamOne;
	}
	public String getTeamTwo() {
		return teamTwo;
	}
	public void setTeamTwo(String teamTwo) {
		this.teamTwo = teamTwo;
	}
}
