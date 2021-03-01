package es.jocasolo.competitiveeventsapi.enums.event;

public enum EventInscriptionType {
	
	PUBLIC, PRIVATE;
	
	public static EventInscriptionType getValue(EventInscriptionType newValue, EventInscriptionType actualValue) {
		return newValue != null ? newValue : actualValue;
	}

}
