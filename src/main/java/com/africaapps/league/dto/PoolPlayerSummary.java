package com.africaapps.league.dto;

public class PoolPlayerSummary extends BaseDto {

	private static final long serialVersionUID = 1L;

	private Long poolPlayerId;
	private String firstName;
	private String lastName;
	private String block;
	private Integer currentScore;	
	private Long price;

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

	public String getBlock() {
		return block;
	}

	public void setBlock(String block) {
		this.block = block;
	}

	public Integer getCurrentScore() {
		return currentScore;
	}

	public void setCurrentScore(Integer currentScore) {
		this.currentScore = currentScore;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}
}
