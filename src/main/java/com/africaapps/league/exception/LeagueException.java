package com.africaapps.league.exception;

public class LeagueException extends Exception {

	private static final long serialVersionUID = 1L;

	public LeagueException() {
		super();
	}
	
	public LeagueException(String msg) {
		super(msg);
	}
	
	public LeagueException(String msg, Throwable t) {
		super(msg, t);
	}
}
