package com.africaapps.league.exception;

public class InvalidLeagueException extends LeagueException {

	private static final long serialVersionUID = 1L;

	public InvalidLeagueException() {
		super();
	}
	
	public InvalidLeagueException(String msg) {
		super(msg);
	}
}
