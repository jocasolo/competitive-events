package es.jocasolo.competitiveeventsapi.exceptions.event;

public class EventUserRejectedException extends Exception {

	private static final long serialVersionUID = 1L;
	private static final String MSG = "User is rejected from this event";
	
	@Override
	public String getMessage() {
		return MSG;
	}
	
}
