package es.jocasolo.competitiveeventsapi.dto.score;

import java.io.Serializable;

import es.jocasolo.competitiveeventsapi.dto.DTO;
import es.jocasolo.competitiveeventsapi.enums.score.ScoreStatusType;
import lombok.Getter;
import lombok.Setter;

public class ScorePutDTO extends DTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private String value;

	@Getter
	@Setter
	private ScoreStatusType status;

	@Getter
	@Setter
	private String eventId;

	@Override
	public String toString() {
		return String.format("ScorePutDTO [value=%s]", value);
	}

}
