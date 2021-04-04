package es.jocasolo.competitiveeventsapi.exceptions.comment;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CommentNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;
	private static final String MSG = "Comment not found";
	
	@Override
	public String getMessage() {
		return MSG;
	}
	
}
