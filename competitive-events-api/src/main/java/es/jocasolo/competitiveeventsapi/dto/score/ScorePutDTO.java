package es.jocasolo.competitiveeventsapi.dto.score;

import java.io.Serializable;

import es.jocasolo.competitiveeventsapi.dto.DTO;
import es.jocasolo.competitiveeventsapi.enums.score.ScoreStatusType;

public class ScorePutDTO extends DTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String value;

	private ScoreStatusType status;

	private String eventId;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public ScoreStatusType getStatus() {
		return status;
	}

	public void setStatus(ScoreStatusType status) {
		this.status = status;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	@Override
	public String toString() {
		return String.format("ScorePutDTO [value=%s]", value);
	}

}
