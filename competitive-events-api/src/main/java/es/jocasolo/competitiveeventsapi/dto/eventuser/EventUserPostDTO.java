package es.jocasolo.competitiveeventsapi.dto.eventuser;

import java.io.Serializable;

import es.jocasolo.competitiveeventsapi.dto.DTO;

public class EventUserPostDTO extends DTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return String.format("EventUserPostDTO [user=%s]", userId);
	}

}
