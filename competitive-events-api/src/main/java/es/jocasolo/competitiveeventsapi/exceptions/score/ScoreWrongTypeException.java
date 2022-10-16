package es.jocasolo.competitiveeventsapi.exceptions.score;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import es.jocasolo.competitiveeventsapi.enums.score.ScoreValueType;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ScoreWrongTypeException extends Exception {

	private static final long serialVersionUID = 1L;
	private static final String MSG = "The score value is not of the type valid for the event. Expected type: %s";
	
	private final ScoreValueType type;
	
	public ScoreWrongTypeException(ScoreValueType type) {
		this.type = type;
	}
	
	@Override
	public String getMessage() {
		return String.format(MSG, type);
	}
	
}
