package es.jocasolo.competitiveeventsapi.dto.score;

import java.io.Serializable;

import es.jocasolo.competitiveeventsapi.dto.DTO;
import es.jocasolo.competitiveeventsapi.dto.image.ImageDTO;
import es.jocasolo.competitiveeventsapi.enums.score.ScoreStatusType;
import lombok.Getter;
import lombok.Setter;

public class ScoreDTO extends DTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private Integer id;

	@Getter
	@Setter
	private String value;

	@Getter
	@Setter
	private String date;

	@Getter
	@Setter
	private ScoreStatusType status;

	@Getter
	@Setter
	private ImageDTO image;

	@Override
	public String toString() {
		return String.format("ScoreDTO [id=%s, value=%s]", id, value);
	}

}
