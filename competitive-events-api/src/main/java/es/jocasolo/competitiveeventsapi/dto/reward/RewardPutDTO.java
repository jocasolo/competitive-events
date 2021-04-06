package es.jocasolo.competitiveeventsapi.dto.reward;

import java.io.Serializable;

import es.jocasolo.competitiveeventsapi.dto.DTO;

public class RewardPutDTO extends DTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String title;

	private String description;

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
