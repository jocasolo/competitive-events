package es.jocasolo.competitiveeventsapi.dto.eventuser;

import java.io.Serializable;

import es.jocasolo.competitiveeventsapi.dto.DTO;
import lombok.Getter;
import lombok.Setter;

public class EventUserPostDTO extends DTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private String username;

	@Getter
	@Setter
	private Boolean reject = false; // Reject invitation to join event

	@Override
	public String toString() {
		return String.format("EventUserPostDTO [user=%s]", username);
	}

}
