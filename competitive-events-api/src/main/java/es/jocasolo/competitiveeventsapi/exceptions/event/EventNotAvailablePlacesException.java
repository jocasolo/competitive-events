package es.jocasolo.competitiveeventsapi.exceptions.event;

public class EventNotAvailablePlacesException extends Exception {

	private static final long serialVersionUID = 1L;
	private static final String MSG = "error.event.not-available-places";
	
	@Override
	public String getMessage() {
		return MSG;
	}
	
}
