package es.jocasolo.competitiveeventsapi.exceptions.user;

public class UserEmailExistsException extends Exception {
	
	private static final long serialVersionUID = 1L;
	private static final String MSG = "Email already exists";
	
	@Override
	public String getMessage() {
		return MSG;
	}

}
