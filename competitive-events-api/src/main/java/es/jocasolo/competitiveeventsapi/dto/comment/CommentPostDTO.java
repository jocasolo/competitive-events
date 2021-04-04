package es.jocasolo.competitiveeventsapi.dto.comment;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import es.jocasolo.competitiveeventsapi.dto.DTO;

public class CommentPostDTO extends DTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String text;

	@NotBlank
	private String eventId;

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "CommentPostDTO [text=" + text + "]";
	}

}
