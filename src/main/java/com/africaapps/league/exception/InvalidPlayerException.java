package com.africaapps.league.exception;

public class InvalidPlayerException extends LeagueException {

	private static final long serialVersionUID = 1L;

	public InvalidPlayerException() {
		super();
	}
	
	public InvalidPlayerException(String msg) {
		super(msg);
	}
}
