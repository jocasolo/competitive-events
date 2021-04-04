package es.jocasolo.competitiveeventsapi.enums.score;

public enum ScoreValueType {
	
	NUMERIC, DECIMAL, TIME;
	
	public static ScoreValueType getValue(ScoreValueType newValue, ScoreValueType actualValue) {
		return newValue != null ? newValue : actualValue;
	}

}
