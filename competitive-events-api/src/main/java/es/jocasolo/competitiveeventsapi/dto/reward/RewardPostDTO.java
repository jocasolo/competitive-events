package es.jocasolo.competitiveeventsapi.dto.reward;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import es.jocasolo.competitiveeventsapi.dto.DTO;
import es.jocasolo.competitiveeventsapi.enums.score.ScoreSortType;

public class RewardPostDTO extends DTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotBlank
	private String title;
	
	private String description;
	
	@NotBlank
	private String eventId;
	
	@NotNull
	private ScoreSortType sortScore;
	
	@NotNull
	private Integer requiredPosition;

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

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public ScoreSortType getSortScore() {
		return sortScore;
	}

	public void setSortScore(ScoreSortType sortScore) {
		this.sortScore = sortScore;
	}

	public Integer getRequiredPosition() {
		return requiredPosition;
	}

	public void setRequiredPosition(Integer requiredPosition) {
		this.requiredPosition = requiredPosition;
	}

	@Override
	public String toString() {
		return "RewardPostDTO [title=" + title + "]";
	}

}
