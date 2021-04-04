package es.jocasolo.competitiveeventsapi.dto.comment;

import java.io.Serializable;

import es.jocasolo.competitiveeventsapi.dto.DTO;

public class CommentPutDTO extends DTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String text;

	private String eventId;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	@Override
	public String toString() {
		return String.format("CommentPutDTO [text=%s]", text);
	}

}
