package es.jocasolo.competitiveeventsapi.exceptions.score;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ScoreWrongUpdateException extends Exception {

	private static final long serialVersionUID = 1L;
	private static final String MSG = "Score wrong update";
	
	@Override
	public String getMessage() {
		return MSG;
	}
	
}
