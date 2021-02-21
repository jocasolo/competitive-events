package es.jocasolo.competitiveeventsapi.enums.event;

public enum EventSortScoreType {
	
	ASC, DESC;
	
	public static EventSortScoreType getValue(EventSortScoreType newValue, EventSortScoreType actualValue) {
		return newValue != null ? newValue : actualValue;
	}

}
