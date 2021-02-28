package es.jocasolo.competitiveeventsapi.exceptions.user;

public class UserUsenameExistsException extends Exception {
	
	private static final long serialVersionUID = 1L;
	private static final String MSG = "Username already exists";
	
	@Override
	public String getMessage() {
		return MSG;
	}

}
