package es.jocasolo.competitiveeventsapi.model;

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

import es.jocasolo.competitiveeventsapi.enums.eventuser.EventUserPrivilegeType;
import es.jocasolo.competitiveeventsapi.enums.eventuser.EventUserStatusType;
import es.jocasolo.competitiveeventsapi.model.keys.EventUserKey;

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
	private EventUserPrivilegeType privilege;
    
    @Enumerated(EnumType.STRING)
	private EventUserStatusType status;
    
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

	public Date getLastStatusDate() {
		return lastStatusDate;
	}

	public void setLastStatusDate(Date lastStatusDate) {
		this.lastStatusDate = lastStatusDate;
	}
	
	// UTILS
	
	public boolean isOwner() {
		return this.privilege.equals(EventUserPrivilegeType.OWNER);
	}
	
	public boolean isUser() {
		return this.privilege.equals(EventUserPrivilegeType.USER);
	}
	
	public boolean isWaiting() {
		return status.equals(EventUserStatusType.WAITING_APPROVAL);
	}
	
	public boolean isAccepted() {
		return status.equals(EventUserStatusType.ACCEPTED);
	}
	
	public boolean isRejected() {
		return status.equals(EventUserStatusType.REJECTED);
	}
	
	public boolean isDeleted() {
		return status.equals(EventUserStatusType.DELETED);
	}
	
	public boolean isInvited() {
		return status.equals(EventUserStatusType.INVITED);
	}
	
	@Override
	public String toString() {
		return String.format("EventUser [%s, %s]", event, user);
	}
	
}
