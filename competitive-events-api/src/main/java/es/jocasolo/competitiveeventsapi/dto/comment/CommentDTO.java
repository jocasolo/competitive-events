package es.jocasolo.competitiveeventsapi.dto.comment;

import java.io.Serializable;

import es.jocasolo.competitiveeventsapi.dto.DTO;
import lombok.Getter;
import lombok.Setter;

public class CommentDTO extends DTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private Integer id;

	@Getter
	@Setter
	private String text;

	@Getter
	@Setter
	private String date;

	@Override
	public String toString() {
		return String.format("CommentDTO [id=%s, text=%s]", id, text);
	}

}
