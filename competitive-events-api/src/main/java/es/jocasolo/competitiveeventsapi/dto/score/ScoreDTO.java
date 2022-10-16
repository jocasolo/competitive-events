package es.jocasolo.competitiveeventsapi.dto.score;

import java.io.Serializable;
import java.util.Date;

import es.jocasolo.competitiveeventsapi.dto.DTO;
import es.jocasolo.competitiveeventsapi.dto.image.ImageDTO;
import es.jocasolo.competitiveeventsapi.dto.user.UserLiteDTO;
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
	private Date date;

	@Getter
	@Setter
	private ScoreStatusType status;

	@Getter
	@Setter
	private ImageDTO image;
	
	@Getter
	@Setter
	private UserLiteDTO user;

	@Override
	public String toString() {
		return String.format("ScoreDTO [id=%s, value=%s]", id, value);
	}

}
