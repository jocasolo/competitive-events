package es.jocasolo.competitiveeventsapi.model.event;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import es.jocasolo.competitiveeventsapi.enums.event.EventInscriptionType;
import es.jocasolo.competitiveeventsapi.enums.event.EventStatusType;
import es.jocasolo.competitiveeventsapi.enums.event.EventType;
import es.jocasolo.competitiveeventsapi.enums.event.EventVisibilityType;
import es.jocasolo.competitiveeventsapi.model.user.User;

@Entity
public class Event implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Integer id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date initDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date creationDate;

	@Enumerated(EnumType.STRING)
	private EventType type;

	@Enumerated(EnumType.STRING)
	private EventInscriptionType inscription;

	@Enumerated(EnumType.STRING)
	private EventVisibilityType visibility;

	@Enumerated(EnumType.STRING)
	private EventStatusType status;

	@Column(columnDefinition = "TEXT")
	private String description;

	@OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
	private List<Reward> rewards;

	@OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
	private List<Punishment> punishments;

	@ManyToMany(mappedBy = "events")
	private Set<User> users;

	@Column(unique = true)
	private String uuid;

	private String title;

	private String subtitle;

	private Boolean appovalNeeded; // needs appoval to join

	private Integer maxPlaces;

	// GETTERS AND SETTERS

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getAppovalNeeded() {
		return appovalNeeded;
	}

	public void setAppovalNeeded(Boolean appovalNeeded) {
		this.appovalNeeded = appovalNeeded;
	}

	public EventStatusType getStatus() {
		return status;
	}

	public void setStatus(EventStatusType status) {
		this.status = status;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public Integer getMaxPlaces() {
		return maxPlaces;
	}

	public void setMaxPlaces(Integer maxPlaces) {
		this.maxPlaces = maxPlaces;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public List<Reward> getRewards() {
		return rewards;
	}

	public void setRewards(List<Reward> rewards) {
		this.rewards = rewards;
	}

	public List<Punishment> getPunishments() {
		return punishments;
	}

	public void setPunishments(List<Punishment> punishments) {
		this.punishments = punishments;
	}

}
