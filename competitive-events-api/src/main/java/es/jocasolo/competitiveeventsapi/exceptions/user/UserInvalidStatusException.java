package es.jocasolo.competitiveeventsapi.exceptions.user;

public class UserInvalidStatusException extends Exception {
	
	private static final long serialVersionUID = 1L;
	private static final String MSG = "Invalid status";
	
	@Override
	public String getMessage() {
		return MSG;
	}

}
