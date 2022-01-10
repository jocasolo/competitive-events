package es.jocasolo.competitiveeventsapp.enums.event;

public enum EventInscriptionType {
	
	PUBLIC, PRIVATE;
	
	public static EventInscriptionType getValue(EventInscriptionType newValue, EventInscriptionType actualValue) {
		return newValue != null ? newValue : actualValue;
	}
	
	public static EventInscriptionType getEnumOrNull(EventInscriptionType value) {
		return value != null ? value : null;
	}

}
