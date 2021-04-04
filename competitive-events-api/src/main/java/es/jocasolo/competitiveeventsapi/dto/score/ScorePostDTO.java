package es.jocasolo.competitiveeventsapi.dto.score;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import es.jocasolo.competitiveeventsapi.dto.DTO;

public class ScorePostDTO extends DTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String value;

	@NotBlank
	private String eventId;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	@Override
	public String toString() {
		return "ScorePostDTO [value=" + value + "]";
	}

}
