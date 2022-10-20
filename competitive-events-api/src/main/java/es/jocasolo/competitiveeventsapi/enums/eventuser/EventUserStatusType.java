package es.jocasolo.competitiveeventsapi.enums.eventuser;

public enum EventUserStatusType {

	INVITED, WAITING_APPROVAL, ACCEPTED, REJECTED, DELETED;
	
	public static EventUserStatusType getEnumOrNull(EventUserStatusType value) {
		return value != null ? value : null;
	}
	
}
