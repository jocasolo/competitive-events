package es.jocasolo.competitiveeventsapi.dto.punishment;

import java.io.Serializable;

import es.jocasolo.competitiveeventsapi.dto.DTO;
import es.jocasolo.competitiveeventsapi.enums.score.ScoreSortType;
import lombok.Getter;
import lombok.Setter;

public class PunishmentPutDTO extends DTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private String title;

	@Getter
	@Setter
	private String description;

	@Getter
	@Setter
	private ScoreSortType sortScore;

	@Getter
	@Setter
	private Integer requiredPosition;

	@Override
	public String toString() {
		return "RewardPutDTO [title=" + title + "]";
	}

}
