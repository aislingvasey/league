package com.africaapps.league.dto;

public class PlayerMatchStatisticSummary extends BaseDto {

	private static final long serialVersionUID = 1L;

	private Long poolPlayerId;
	private String firstName;      
	private String lastName;  
	private String playerBlock;
	private Long matchId;
	private String matchDate;
	private Double matchPoints;
	private String statName;
	private Double statPoints;
	private Double statTotal;
	private String teamOne;	
	private String teamTwo;

	public Double getStatCount() {
		return statTotal / statPoints;
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

	public String getMatchDate() {
		return matchDate;
	}

	public void setMatchDate(String matchDate) {
		this.matchDate = matchDate;
	}

	public Double getMatchPoints() {
		return matchPoints;
	}

	public void setMatchPoints(Double matchPoints) {
		this.matchPoints = matchPoints;
	}

	public String getStatName() {
		return statName;
	}

	public void setStatName(String statName) {
		this.statName = statName;
	}

	public Double getStatPoints() {
		return statPoints;
	}

	public void setStatPoints(Double statPoints) {
		this.statPoints = statPoints;
	}

	public Double getStatTotal() {
		return statTotal;
	}

	public void setStatTotal(Double statTotal) {
		this.statTotal = statTotal;
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
