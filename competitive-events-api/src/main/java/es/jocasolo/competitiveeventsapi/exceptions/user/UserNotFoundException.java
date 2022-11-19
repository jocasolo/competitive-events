package es.jocasolo.competitiveeventsapi.exceptions.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;
	private static final String MSG = "error.user.not-found";
	
	@Override
	public String getMessage() {
		return MSG;
	}
	
}
