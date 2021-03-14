package es.jocasolo.competitiveeventsapi.enums.event;

public enum EventStatusType {

	ACTIVE, FINISHED, DELETED;
	
	public static EventStatusType getValue(EventStatusType newValue, EventStatusType actualValue) {
		return newValue != null ? newValue : actualValue;
	}
	
	public static EventStatusType getEnumOrNull(EventStatusType value) {
		return value != null ? value : null;
	}
	
}
