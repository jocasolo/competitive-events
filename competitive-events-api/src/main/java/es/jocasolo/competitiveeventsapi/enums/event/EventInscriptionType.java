package es.jocasolo.competitiveeventsapi.enums.event;

public enum EventInscriptionType {
	
	PUBLIC, LINK, INVITATION;
	
	public static EventInscriptionType getValue(EventInscriptionType newValue, EventInscriptionType actualValue) {
		return newValue != null ? newValue : actualValue;
	}

}
