package com.africaapps.league.dto;

public class NeededPlayer extends BaseDto {

	private static final long serialVersionUID = 1L;

	private long userTeamId;
	private int needed;
	
	public NeededPlayer() {
		this.userTeamId = 0;
		this.needed = 0;
	}
	
	public NeededPlayer(long userTeamId, int needed) {
		this.userTeamId = userTeamId;
		this.needed = needed;
	}

	public long getUserTeamId() {
		return userTeamId;
	}

	public void setUserTeamId(long userTeamId) {
		this.userTeamId = userTeamId;
	}

	public int getNeeded() {
		return needed;
	}

	public void setNeeded(int needed) {
		this.needed = needed;
	}
}
