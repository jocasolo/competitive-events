package es.jocasolo.competitiveeventsapi.exceptions.event;

public class EventWrongUpdateException extends Exception {

	private static final long serialVersionUID = 1L;
	private static final String MSG = "Event wrong update";
	
	@Override
	public String getMessage() {
		return MSG;
	}
	
}
