package es.jocasolo.competitiveeventsapi.exceptions.user;

public class UserPhoneExistsException extends Exception {
	
	private static final long serialVersionUID = 1L;
	private static final String MSG = "error.phone.exists";
	
	@Override
	public String getMessage() {
		return MSG;
	}
	
}
