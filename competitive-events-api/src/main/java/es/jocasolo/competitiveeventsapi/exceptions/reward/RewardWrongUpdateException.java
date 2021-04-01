package es.jocasolo.competitiveeventsapi.exceptions.reward;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RewardWrongUpdateException extends Exception {

	private static final long serialVersionUID = 1L;
	private static final String MSG = "Reward wrong update";
	
	@Override
	public String getMessage() {
		return MSG;
	}
	
}
