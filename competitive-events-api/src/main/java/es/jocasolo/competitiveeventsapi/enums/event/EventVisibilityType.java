package es.jocasolo.competitiveeventsapi.enums.event;

public enum EventVisibilityType {
	
	PUBLIC, PRIVATE;
	
	public static EventVisibilityType getValue(EventVisibilityType newValue, EventVisibilityType actualValue) {
		return newValue != null ? newValue : actualValue;
	}
	
}
