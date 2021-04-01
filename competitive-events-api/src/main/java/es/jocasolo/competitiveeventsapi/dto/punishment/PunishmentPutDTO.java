package es.jocasolo.competitiveeventsapi.dto.punishment;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import es.jocasolo.competitiveeventsapi.dto.DTO;
import es.jocasolo.competitiveeventsapi.enums.event.EventSortScoreType;

public class PunishmentPutDTO extends DTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotBlank
	private Integer id;

	private String title;

	private String description;

	private EventSortScoreType sortScore;

	private Integer requiredPosition;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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

	public EventSortScoreType getSortScore() {
		return sortScore;
	}

	public void setSortScore(EventSortScoreType sortScore) {
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
		return "RewardPutDTO [title=" + title + "]";
	}

}
