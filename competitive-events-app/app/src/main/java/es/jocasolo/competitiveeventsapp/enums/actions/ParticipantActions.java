package es.jocasolo.competitiveeventsapp.enums.actions;

public enum ParticipantActions {

	REJECT, DELETE, ACCEPT;
	
	public static ParticipantActions getValue(ParticipantActions newValue, ParticipantActions actualValue) {
		return newValue != null ? newValue : actualValue;
	}

}
