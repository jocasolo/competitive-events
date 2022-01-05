package es.jocasolo.competitiveeventsapi.dto.eventuser;

import java.io.Serializable;
import java.util.Date;

import es.jocasolo.competitiveeventsapi.dto.DTO;
import es.jocasolo.competitiveeventsapi.enums.eventuser.EventUserPrivilegeType;
import es.jocasolo.competitiveeventsapi.enums.eventuser.EventUserStatusType;
import lombok.Getter;
import lombok.Setter;

public class EventUserDTO extends DTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private String userId;

	@Getter
	@Setter
	private String eventId;

	@Getter
	@Setter
	private Date incorporationDate;

	@Getter
	@Setter
	private Date lastStatusDate;

	@Getter
	@Setter
	private EventUserPrivilegeType privilege;

	@Getter
	@Setter
	private EventUserStatusType status;

	@Override
	public String toString() {
		return String.format("EventUserDTO [user=%s, event=%s]", userId, eventId);
	}
}
