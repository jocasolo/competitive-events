package es.jocasolo.competitiveeventsapi.exceptions.user;

public class UserIdentifierExistsException extends Exception {
	
	private static final long serialVersionUID = 1L;
	private static final String MSG = "Identifier already exists";
	
	@Override
	public String getMessage() {
		return MSG;
	}

}
