package com.africaapps.league.dto;


public class UserPlayerSummary extends BaseDto {

	private static final long serialVersionUID = 1L;

	private Integer playerId; //TODO external id for testing only
	private Long poolPlayerId;
	private String firstName;
	private String lastName;
	private String block;
	private String originalBlock; //for substitutes that have SUBSTITUTE in their block attribute
	private long price;	
	private int currentScore;
	private String status;
	
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

	public long getPrice() {
		return price;
	}
	
	public void setPrice(long price) {
		this.price = price;
	}
	
	public int getCurrentScore() {
		return currentScore;
	}
	
	public void setCurrentScore(int currentScore) {
		this.currentScore = currentScore;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public Integer getPlayerId() {
		return playerId;
	}
	
	public void setPlayerId(Integer playerId) {
		this.playerId = playerId;
	}

	public String getBlock() {
		return block;
	}

	public void setBlock(String block) {
		this.block = block;
	}

	public String getOriginalBlock() {
		return originalBlock;
	}

	public void setOriginalBlock(String originalBlock) {
		this.originalBlock = originalBlock;
	}
}
