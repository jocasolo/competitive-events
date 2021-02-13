package es.jocasolo.competitiveeventsapi.dto.event;

import java.io.Serializable;

import es.jocasolo.competitiveeventsapi.dto.DTO;

public class EventDTO extends DTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String uuid;

	private String title;

	private String description;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

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
		return "EventDTO [uuid=" + uuid + "]";
	}

}
