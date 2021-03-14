package es.jocasolo.competitiveeventsapi.exceptions;

import java.io.IOException;

public class CustomRuntimeException extends RuntimeException {

	public CustomRuntimeException(IOException e) {
		super(e);
	}

	private static final long serialVersionUID = 1L;

}
