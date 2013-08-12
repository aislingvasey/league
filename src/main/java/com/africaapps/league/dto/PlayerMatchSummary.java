package com.africaapps.league.dto;

public class PlayerMatchSummary extends BaseDto {

	private static final long serialVersionUID = 1L;

	private Long poolPlayerId;
	private String firstName;      
	private String lastName;  
	private String playerBlock;
	private Long matchId;
	private String matchDate;        	
	private Double playerPoints;

	public Long getMatchId() {
		return matchId;
	}

	public void setMatchId(Long matchId) {
		this.matchId = matchId;
	}

	public String getPlayerBlock() {
		return playerBlock;
	}

	public void setPlayerBlock(String playerBlock) {
		this.playerBlock = playerBlock;
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

	public String getMatchDate() {
		return matchDate;
	}

	public void setMatchDate(String matchDate) {
		this.matchDate = matchDate;
	}

	public Double getPlayerPoints() {
		return playerPoints;
	}

	public void setPlayerPoints(Double playerPoints) {
		this.playerPoints = playerPoints;
	} 
}
