package es.jocasolo.competitiveeventsapi.dto.user;

import java.io.Serializable;
import java.util.Date;

import es.jocasolo.competitiveeventsapi.dto.DTO;
import es.jocasolo.competitiveeventsapi.dto.image.ImageDTO;
import es.jocasolo.competitiveeventsapi.enums.eventuser.EventUserPrivilegeType;
import es.jocasolo.competitiveeventsapi.enums.eventuser.EventUserStatusType;
import lombok.Getter;
import lombok.Setter;

public class UserLiteWithEventDTO extends DTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private String id;

	@Getter
	@Setter
	private String name;

	@Getter
	@Setter
	private String surname;

	@Getter
	@Setter
	private ImageDTO avatar;
	
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
		return "UserLiteWithEventDTO [id=" + id + "]";
	}

}
