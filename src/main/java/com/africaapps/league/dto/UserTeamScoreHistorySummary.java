package com.africaapps.league.dto;

public class UserTeamScoreHistorySummary extends BaseDto {

	private static final long serialVersionUID = 1L;

	private Long userTeamId;
	private String teamName;
	private Integer teamCurrentScore;
	private Long matchId;
	private String matchDate;
	private Integer matchPoints;
	private String playerFirstName;
	private String playerLastName;	
	private Integer playerPoints;
	
	public Integer getMatchPoints() {
		return matchPoints;
	}

	public void setMatchPoints(Integer matchPoints) {
		this.matchPoints = matchPoints;
	}

	public Long getUserTeamId() {
		return userTeamId;
	}

	public void setUserTeamId(Long userTeamId) {
		this.userTeamId = userTeamId;
	}

	public Long getMatchId() {
		return matchId;
	}

	public void setMatchId(Long matchId) {
		this.matchId = matchId;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public Integer getTeamCurrentScore() {
		return teamCurrentScore;
	}

	public void setTeamCurrentScore(Integer teamCurrentScore) {
		this.teamCurrentScore = teamCurrentScore;
	}

	public String getMatchDate() {
		return matchDate;
	}

	public void setMatchDate(String matchDate) {
		this.matchDate = matchDate;
	}

	public String getPlayerFirstName() {
		return playerFirstName;
	}

	public void setPlayerFirstName(String playerFirstName) {
		this.playerFirstName = playerFirstName;
	}

	public String getPlayerLastName() {
		return playerLastName;
	}

	public void setPlayerLastName(String playerLastName) {
		this.playerLastName = playerLastName;
	}

	public Integer getPlayerPoints() {
		return playerPoints;
	}

	public void setPlayerPoints(Integer playerPoints) {
		this.playerPoints = playerPoints;
	}
}
