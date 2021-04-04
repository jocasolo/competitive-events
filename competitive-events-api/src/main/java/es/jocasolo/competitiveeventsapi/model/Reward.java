package es.jocasolo.competitiveeventsapi.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import es.jocasolo.competitiveeventsapi.enums.score.ScoreSortType;

@Entity
public class Reward implements Serializable {

	private static final long serialVersionUID = 1L;

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
	private ScoreSortType sortScore;

	@Column(nullable = false)
	private Integer requiredPosition;

	@ManyToOne()
	@JoinColumn(name = "image")
	private Image image;

	private String title;

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

	public ScoreSortType getSortScore() {
		return sortScore;
	}

	public void setSortScore(ScoreSortType sortScore) {
		this.sortScore = sortScore;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	@Override
	public String toString() {
		return String.format("Reward [id=%s]", id);
	}

}
