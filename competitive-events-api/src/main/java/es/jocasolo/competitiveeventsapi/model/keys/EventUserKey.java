package es.jocasolo.competitiveeventsapi.model.keys;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class EventUserKey implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "event_id")
	private Integer eventId;

	@Column(name = "username")
	private String username;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (o == null || getClass() != o.getClass())
			return false;

		EventUserKey that = (EventUserKey) o;
		return Objects.equals(eventId, that.eventId) && Objects.equals(username, that.username);
	}

	@Override
	public int hashCode() {
		return Objects.hash(eventId, username);
	}

	// GETTERS AND SETTERS

	public Integer getEventId() {
		return eventId;
	}

	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
