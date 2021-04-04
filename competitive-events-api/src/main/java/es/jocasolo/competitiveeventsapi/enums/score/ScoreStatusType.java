package es.jocasolo.competitiveeventsapi.enums.score;

public enum ScoreStatusType {
	
	VALID, NOT_VALID;
	
	public static ScoreStatusType getValue(ScoreStatusType newValue, ScoreStatusType actualValue) {
		return newValue != null ? newValue : actualValue;
	}

}
