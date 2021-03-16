package es.jocasolo.competitiveeventsapi.dto.eventuser;

import java.io.Serializable;

import es.jocasolo.competitiveeventsapi.dto.DTO;
import es.jocasolo.competitiveeventsapi.enums.eventuser.EventUserPrivilegeType;
import es.jocasolo.competitiveeventsapi.enums.eventuser.EventUserStatusType;

public class EventUserPutDTO extends DTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String username;

	private EventUserStatusType status;

	private EventUserPrivilegeType privilege;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public EventUserStatusType getStatus() {
		return status;
	}

	public void setStatus(EventUserStatusType status) {
		this.status = status;
	}

	public EventUserPrivilegeType getPrivilege() {
		return privilege;
	}

	public void setPrivilege(EventUserPrivilegeType privilege) {
		this.privilege = privilege;
	}

	@Override
	public String toString() {
		return String.format("EventUserPostDTO [user=%s]", username);
	}

}
