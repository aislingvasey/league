package com.africaapps.league.service.feed;

public class FeedSettings {

	private String wsdlUrl;
	private String leagueName;
	private String username;
	private String password;
	private int endOfPlayingWeekDay;
	
	public String getWsdlUrl() {
		return wsdlUrl;
	}
	
	public void setWsdlUrl(String wsdlUrl) {
		this.wsdlUrl = wsdlUrl;
	}
	
	public String getLeagueName() {
		return leagueName;
	}
	
	public void setLeagueName(String leagueName) {
		this.leagueName = leagueName;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public int getEndOfPlayingWeekDay() {
		return endOfPlayingWeekDay;
	}

	public void setEndOfPlayingWeekDay(int endOfPlayingWeekDay) {
		this.endOfPlayingWeekDay = endOfPlayingWeekDay;
	}
}