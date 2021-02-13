package es.jocasolo.competitiveeventsapi.model.event;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import es.jocasolo.competitiveeventsapi.enums.event.EventSortScoreType;
import es.jocasolo.competitiveeventsapi.model.user.User;

@Entity
public class Reward {

	@Id
	@GeneratedValue
	private Integer id;

	@Column(columnDefinition = "TEXT")
	private String description;

	@ManyToOne()
	@JoinColumn(name = "event_id")
	private Event event;

	@ManyToOne()
	private User winner;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private EventSortScoreType sortScore;

	@Column(nullable = false)
	private Integer requiredPosition;

	// GETTERS AND SETTERS

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getWinner() {
		return winner;
	}

	public void setWinner(User winner) {
		this.winner = winner;
	}

	public Integer getRequiredPosition() {
		return requiredPosition;
	}

	public void setRequiredPosition(Integer requiredPosition) {
		this.requiredPosition = requiredPosition;
	}

	public EventSortScoreType getSortScore() {
		return sortScore;
	}

	public void setSortScore(EventSortScoreType sortScore) {
		this.sortScore = sortScore;
	}
	
}
