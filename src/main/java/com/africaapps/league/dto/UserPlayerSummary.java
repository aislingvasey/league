package com.africaapps.league.dto;

import com.africaapps.league.model.game.UserPlayerStatus;
import com.africaapps.league.model.league.BlockType;

public class UserPlayerSummary extends BaseDto {

	private static final long serialVersionUID = 1L;

	private Long poolPlayerId;
	private String firstName;
	private String lastName;
	private BlockType block;
	private long price;	
	private int currentScore;
	private UserPlayerStatus status;
	
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
	public BlockType getBlock() {
		return block;
	}
	public void setBlock(BlockType type) {
		this.block = type;
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
	public UserPlayerStatus getStatus() {
		return status;
	}
	public void setStatus(UserPlayerStatus status) {
		this.status = status;
	}
}
