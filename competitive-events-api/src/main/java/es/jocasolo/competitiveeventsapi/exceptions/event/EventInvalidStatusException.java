package es.jocasolo.competitiveeventsapi.exceptions.event;

public class EventInvalidStatusException extends Exception {
	
	private static final long serialVersionUID = 1L;
	private static final String MSG = "Invalid status";
	
	@Override
	public String getMessage() {
		return MSG;
	}

}
