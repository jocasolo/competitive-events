package es.jocasolo.competitiveeventsapi.model.event;

import java.util.Date;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import es.jocasolo.competitiveeventsapi.enums.user.UserEventStatusType;
import es.jocasolo.competitiveeventsapi.enums.user.UserPrivilegeType;
import es.jocasolo.competitiveeventsapi.model.keys.EventUserKey;
import es.jocasolo.competitiveeventsapi.model.user.User;

@Entity
@Table(name = "event_user")
public class EventUser {
	
	@EmbeddedId
	private EventUserKey id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @MapsId("eventId")
    private Event event;
 
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    private User user;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date incorporationDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastStatusDate;
    
    @Enumerated(EnumType.STRING)
	private UserPrivilegeType privilege;
    
    @Enumerated(EnumType.STRING)
	private UserEventStatusType status;
    
    // GETTERS AND SETTERS

	public EventUserKey getId() {
		return id;
	}

	public void setId(EventUserKey id) {
		this.id = id;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getIncorporationDate() {
		return incorporationDate;
	}

	public void setIncorporationDate(Date incorporationDate) {
		this.incorporationDate = incorporationDate;
	}

	public UserEventStatusType getStatus() {
		return status;
	}

	public void setStatus(UserEventStatusType status) {
		this.status = status;
	}

	public UserPrivilegeType getPrivilege() {
		return privilege;
	}

	public void setPrivilege(UserPrivilegeType privilege) {
		this.privilege = privilege;
	}

	public Date getLastStatusDate() {
		return lastStatusDate;
	}

	public void setLastStatusDate(Date lastStatusDate) {
		this.lastStatusDate = lastStatusDate;
	}
	
}
