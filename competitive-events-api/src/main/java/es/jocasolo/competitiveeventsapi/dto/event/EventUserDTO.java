package es.jocasolo.competitiveeventsapi.dto.event;

import java.io.Serializable;
import java.util.Date;

import es.jocasolo.competitiveeventsapi.dto.DTO;
import es.jocasolo.competitiveeventsapi.enums.user.UserEventStatusType;
import es.jocasolo.competitiveeventsapi.enums.user.UserPrivilegeType;

public class EventUserDTO extends DTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userId;

	private String eventId;

	private Date incorporationDate;

	private Date lastStatusDate;

	private UserPrivilegeType privilege;

	private UserEventStatusType status;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public Date getIncorporationDate() {
		return incorporationDate;
	}

	public void setIncorporationDate(Date incorporationDate) {
		this.incorporationDate = incorporationDate;
	}

	public Date getLastStatusDate() {
		return lastStatusDate;
	}

	public void setLastStatusDate(Date lastStatusDate) {
		this.lastStatusDate = lastStatusDate;
	}

	public UserPrivilegeType getPrivilege() {
		return privilege;
	}

	public void setPrivilege(UserPrivilegeType privilege) {
		this.privilege = privilege;
	}

	public UserEventStatusType getStatus() {
		return status;
	}

	public void setStatus(UserEventStatusType status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return String.format("EventUserDTO [user=%s, event=%s]", userId, eventId);
	}
}
