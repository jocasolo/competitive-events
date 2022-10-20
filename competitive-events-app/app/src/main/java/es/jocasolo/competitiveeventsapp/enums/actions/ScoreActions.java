package es.jocasolo.competitiveeventsapp.enums.actions;

public enum ScoreActions {

	VALIDATE, INVALIDATE;
	
	public static ScoreActions getValue(ScoreActions newValue, ScoreActions actualValue) {
		return newValue != null ? newValue : actualValue;
	}

}
