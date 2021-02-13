package es.jocasolo.competitiveeventsapi.dto.event;

import java.io.Serializable;

import es.jocasolo.competitiveeventsapi.dto.DTO;

public class EventPostDTO extends DTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String title;

	private String description;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "EventPostDTO [title=" + title + "]";
	}

}
