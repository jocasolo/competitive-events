package es.jocasolo.competitiveeventsapi.exceptions.user;

public class UserNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;
	private static final String MSG = "Event not found";
	
	@Override
	public String getMessage() {
		return MSG;
	}
	
}
