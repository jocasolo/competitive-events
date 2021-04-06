package es.jocasolo.competitiveeventsapi.exceptions.event;

public class EventUserAcceptedException extends Exception {

	private static final long serialVersionUID = 1L;
	private static final String MSG = "User is already in this event";
	
	@Override
	public String getMessage() {
		return MSG;
	}
	
}
