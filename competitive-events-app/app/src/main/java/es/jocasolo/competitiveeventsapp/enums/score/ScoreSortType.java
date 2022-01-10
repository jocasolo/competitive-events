package es.jocasolo.competitiveeventsapp.enums.score;

public enum ScoreSortType {
	
	ASC, DESC;
	
	public static ScoreSortType getValue(ScoreSortType newValue, ScoreSortType actualValue) {
		return newValue != null ? newValue : actualValue;
	}

}
