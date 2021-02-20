package es.jocasolo.competitiveeventsapi.exceptions.user;

public class UserWrongUpdateException extends Exception {

	private static final long serialVersionUID = 1L;
	private static final String MSG = "The user name or password is incorrect";
	
	@Override
	public String getMessage() {
		return MSG;
	}
	
}
