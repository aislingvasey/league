package com.africaapps.league.dto;

public class PlayerSummary extends BaseDto {

	private static final long serialVersionUID = 1L;
	
	private Long playerId;
	private Long poolPlayerId;
	private String firstName;
	private String lastName;
	private String block;
	private Double currentScore;	
	private Long price;
	
	//Used for displaying playing week history scores
	private Long matchId;
	private Long playingWeekId;
	private boolean captainExtraPoints = false;

	public Long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(Long playerId) {
		this.playerId = playerId;
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

	public String getBlock() {
		return block;
	}

	public void setBlock(String block) {
		this.block = block;
	}

	public Double getCurrentScore() {
		return currentScore;
	}

	public void setCurrentScore(Double currentScore) {
		this.currentScore = currentScore;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}

	public Long getPoolPlayerId() {
		return poolPlayerId;
	}

	public void setPoolPlayerId(Long poolPlayerId) {
		this.poolPlayerId = poolPlayerId;
	}

	public Long getPlayingWeekId() {
		return playingWeekId;
	}

	public void setPlayingWeekId(Long playingWeekId) {
		this.playingWeekId = playingWeekId;
	}

	public boolean isCaptainExtraPoints() {
		return captainExtraPoints;
	}

	public void setCaptainExtraPoints(boolean captainExtraPoints) {
		this.captainExtraPoints = captainExtraPoints;
	}

	public Long getMatchId() {
		return matchId;
	}

	public void setMatchId(Long matchId) {
		this.matchId = matchId;
	}
}
