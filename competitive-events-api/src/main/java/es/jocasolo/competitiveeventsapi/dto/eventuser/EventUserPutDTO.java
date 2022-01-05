package es.jocasolo.competitiveeventsapi.dto.eventuser;

import java.io.Serializable;

import es.jocasolo.competitiveeventsapi.dto.DTO;
import es.jocasolo.competitiveeventsapi.enums.eventuser.EventUserPrivilegeType;
import es.jocasolo.competitiveeventsapi.enums.eventuser.EventUserStatusType;
import lombok.Getter;
import lombok.Setter;

public class EventUserPutDTO extends DTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private String username;

	@Getter
	@Setter
	private EventUserStatusType status;

	@Getter
	@Setter
	private EventUserPrivilegeType privilege;

	@Override
	public String toString() {
		return String.format("EventUserPostDTO [user=%s]", username);
	}

}
