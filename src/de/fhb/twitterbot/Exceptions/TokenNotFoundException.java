package de.fhb.twitterbot.Exceptions;

public class TokenNotFoundException extends Exception {
	private static final long serialVersionUID = -8930303975130588474L;

	public TokenNotFoundException() {
		super();
	}

	public TokenNotFoundException(String message) {
		super(message);
	}
}
