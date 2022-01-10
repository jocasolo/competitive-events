package es.jocasolo.competitiveeventsapp.enums.event;

public enum EventStatusType {

	NOT_ACTIVE, ACTIVE, FINISHED, DELETED;
	
	public static EventStatusType getValue(EventStatusType newValue, EventStatusType actualValue) {
		return newValue != null ? newValue : actualValue;
	}
	
	public static EventStatusType getEnumOrNull(EventStatusType value) {
		return value != null ? value : null;
	}
	
}
