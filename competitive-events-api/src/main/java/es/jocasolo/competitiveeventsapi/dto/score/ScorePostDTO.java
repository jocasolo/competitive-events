package es.jocasolo.competitiveeventsapi.dto.score;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import es.jocasolo.competitiveeventsapi.dto.DTO;
import lombok.Getter;
import lombok.Setter;

public class ScorePostDTO extends DTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private String value;

	@Getter
	@Setter
	@NotBlank
	private String eventId;

	@Override
	public String toString() {
		return "ScorePostDTO [value=" + value + "]";
	}

}
