package es.jocasolo.competitiveeventsapi.dto.comment;

import java.io.Serializable;

import es.jocasolo.competitiveeventsapi.dto.DTO;
import lombok.Getter;
import lombok.Setter;

public class CommentPutDTO extends DTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private String text;

	@Getter
	@Setter
	private String eventId;

	@Override
	public String toString() {
		return String.format("CommentPutDTO [text=%s]", text);
	}

}
