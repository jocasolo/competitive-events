package es.jocasolo.competitiveeventsapi.dto.comment;

import java.io.Serializable;

import es.jocasolo.competitiveeventsapi.dto.DTO;

public class CommentDTO extends DTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;

	private String text;

	private String date;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return String.format("CommentDTO [id=%s, text=%s]", id, text);
	}

}
