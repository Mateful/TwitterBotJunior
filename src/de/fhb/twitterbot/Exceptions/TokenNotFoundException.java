package de.fhb.twitterbot.Exceptions;

public class TokenNotFoundException extends Exception {
	public TokenNotFoundException() {
		super();
	}

	public TokenNotFoundException(String message) {
		super(message);
	}
}
