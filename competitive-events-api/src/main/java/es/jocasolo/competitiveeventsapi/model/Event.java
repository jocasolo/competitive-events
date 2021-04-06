package es.jocasolo.competitiveeventsapi.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import es.jocasolo.competitiveeventsapi.enums.event.EventInscriptionType;
import es.jocasolo.competitiveeventsapi.enums.event.EventStatusType;
import es.jocasolo.competitiveeventsapi.enums.event.EventType;
import es.jocasolo.competitiveeventsapi.enums.event.EventVisibilityType;
import es.jocasolo.competitiveeventsapi.enums.score.ScoreSortType;
import es.jocasolo.competitiveeventsapi.enums.score.ScoreValueType;

@Entity
public class Event implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String id;

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

	@OneToMany(mappedBy = "event")
	private Set<Reward> rewards = new HashSet<>();

	@OneToMany(mappedBy = "event")
	private Set<Punishment> punishments = new HashSet<>();

	@ManyToMany(mappedBy = "events")
	private Set<User> users = new HashSet<>();

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "image_id")
	private Image image;

	@OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Score> scores;

	@OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> comments;

	@ManyToMany
	@JoinTable(name = "image_event", joinColumns = @JoinColumn(name = "event_id"), inverseJoinColumns = @JoinColumn(name = "image_id"))
	private Set<Image> images = new HashSet<>();

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ScoreSortType sortScore;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ScoreValueType scoreType;

	private String title;

	private String subtitle;

	private Boolean approvalNeeded; // needs approval to join

	private Integer maxPlaces;

	/**
	 * Check if the event is within the range of start and end dates
	 * 
	 * @return True if the current date is between the start and end of the event
	 */
	public boolean isInDateRange() {
		Date now = new Date();

		if (initDate == null && endDate == null)
			return true;

		if (initDate == null && now.before(endDate))
			return true;

		if (endDate == null && now.after(initDate))
			return true;

		if (now.after(initDate) && now.before(endDate))
			return true;

		return false;
	}

	// GETTERS AND SETTERS

	public Boolean getApprovalNeeded() {
		return approvalNeeded;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setApprovalNeeded(Boolean approvalNeeded) {
		this.approvalNeeded = approvalNeeded;
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

	public Set<Reward> getRewards() {
		return rewards;
	}

	public void setRewards(Set<Reward> rewards) {
		this.rewards = rewards;
	}

	public Set<Punishment> getPunishments() {
		return punishments;
	}

	public void setPunishments(Set<Punishment> punishments) {
		this.punishments = punishments;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public Set<Image> getImages() {
		return images;
	}

	public void setImages(Set<Image> images) {
		this.images = images;
	}

	public List<Score> getScores() {
		return scores;
	}

	public void setScores(List<Score> scores) {
		this.scores = scores;
	}

	public ScoreValueType getScoreType() {
		return scoreType;
	}

	public void setScoreType(ScoreValueType scoreType) {
		this.scoreType = scoreType;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public ScoreSortType getSortScore() {
		return sortScore;
	}

	public void setSortScore(ScoreSortType sortScore) {
		this.sortScore = sortScore;
	}

	@Override
	public String toString() {
		return String.format("Event [id=%s]", id);
	}

}
