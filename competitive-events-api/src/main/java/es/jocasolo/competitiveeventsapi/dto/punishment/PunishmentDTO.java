package es.jocasolo.competitiveeventsapi.dto.punishment;

import java.io.Serializable;

import es.jocasolo.competitiveeventsapi.dto.DTO;
import es.jocasolo.competitiveeventsapi.dto.image.ImageDTO;
import es.jocasolo.competitiveeventsapi.dto.user.UserDTO;
import es.jocasolo.competitiveeventsapi.enums.score.ScoreSortType;
import lombok.Getter;
import lombok.Setter;

public class PunishmentDTO extends DTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private String id;

	@Getter
	@Setter
	private String title;

	@Getter
	@Setter
	private String description;

	@Getter
	@Setter
	private UserDTO looser;

	@Getter
	@Setter
	private ScoreSortType sortScore;

	@Getter
	@Setter
	private Integer requiredPosition;

	@Getter
	@Setter
	private ImageDTO image;

	@Override
	public String toString() {
		return String.format("RewardDTO [id=%s, title=%s]", id, title);
	}

}
