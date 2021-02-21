package es.jocasolo.competitiveeventsapi.enums.event;

public enum EventType {
	
	SPORTS, VIDEOGAMES, FAMILY, ACADEMIC, OTHER;
	
	public static EventType getValue(EventType newValue, EventType actualValue) {
		return newValue != null ? newValue : actualValue;
	}

}
