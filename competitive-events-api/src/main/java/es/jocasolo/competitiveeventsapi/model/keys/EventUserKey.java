package es.jocasolo.competitiveeventsapi.model.keys;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class EventUserKey implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "event_id")
	private String eventId;

	@Column(name = "user_id")
	private String userId;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (o == null || getClass() != o.getClass())
			return false;

		EventUserKey that = (EventUserKey) o;
		return Objects.equals(eventId, that.eventId) && Objects.equals(userId, that.userId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(eventId, userId);
	}

	// GETTERS AND SETTERS

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
