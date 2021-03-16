package es.jocasolo.competitiveeventsapi.dto.eventuser;

import java.io.Serializable;

import es.jocasolo.competitiveeventsapi.dto.DTO;

public class EventUserPostDTO extends DTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String username;

	private Boolean reject = false; // Reject invitation to join event

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Boolean getReject() {
		return reject;
	}

	public void setReject(Boolean reject) {
		this.reject = reject;
	}

	@Override
	public String toString() {
		return String.format("EventUserPostDTO [user=%s]", username);
	}

}
