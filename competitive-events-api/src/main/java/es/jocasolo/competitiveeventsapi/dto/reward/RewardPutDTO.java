package es.jocasolo.competitiveeventsapi.dto.reward;

import java.io.Serializable;

import es.jocasolo.competitiveeventsapi.dto.DTO;
import es.jocasolo.competitiveeventsapi.enums.score.ScoreSortType;
import lombok.Getter;
import lombok.Setter;

public class RewardPutDTO extends DTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private String title;

	@Getter
	@Setter
	private String description;

	@Getter
	@Setter
	private Integer requiredPosition;
	
	@Getter
	@Setter
	private ScoreSortType sortScore;

	@Override
	public String toString() {
		return "RewardPutDTO [title=" + title + "]";
	}

}
