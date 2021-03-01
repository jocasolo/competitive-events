package es.jocasolo.competitiveeventsapi.dto.event;

import java.io.Serializable;
import java.util.Date;

import es.jocasolo.competitiveeventsapi.dto.DTO;
import es.jocasolo.competitiveeventsapi.enums.event.EventInscriptionType;
import es.jocasolo.competitiveeventsapi.enums.event.EventStatusType;
import es.jocasolo.competitiveeventsapi.enums.event.EventType;
import es.jocasolo.competitiveeventsapi.enums.event.EventVisibilityType;

public class EventPutDTO extends DTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	private String title;

	private String subtitle;

	private String description;

	private Date initDate;

	private Date endDate;

	private EventType type;

	private EventInscriptionType inscription;

	private EventVisibilityType visibility;

	private EventStatusType status;

	private Boolean appovalNeeded;

	private Integer maxPlaces;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public Date getInitDate() {
		return initDate;
	}

	public void setInitDate(Date initDate) {
		this.initDate = initDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public EventInscriptionType getInscription() {
		return inscription;
	}

	public void setInscription(EventInscriptionType inscription) {
		this.inscription = inscription;
	}

	public EventVisibilityType getVisibility() {
		return visibility;
	}

	public void setVisibility(EventVisibilityType visibility) {
		this.visibility = visibility;
	}

	public EventStatusType getStatus() {
		return status;
	}

	public void setStatus(EventStatusType status) {
		this.status = status;
	}

	public Boolean getAppovalNeeded() {
		return appovalNeeded;
	}

	public void setAppovalNeeded(Boolean appovalNeeded) {
		this.appovalNeeded = appovalNeeded;
	}

	public Integer getMaxPlaces() {
		return maxPlaces;
	}

	public void setMaxPlaces(Integer maxPlaces) {
		this.maxPlaces = maxPlaces;
	}

	@Override
	public String toString() {
		return "EventPostDTO [title=" + title + "]";
	}

}
