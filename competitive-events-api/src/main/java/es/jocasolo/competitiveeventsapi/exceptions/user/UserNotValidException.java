package es.jocasolo.competitiveeventsapi.exceptions.user;

public class UserNotValidException extends Exception {
	
	private static final long serialVersionUID = 1L;
	private static final String MSG = "The current user cannot perform the requested action";
	
	@Override
	public String getMessage() {
		return MSG;
	}

}
