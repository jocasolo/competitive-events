package es.jocasolo.competitiveeventsapi.dto.reward;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import es.jocasolo.competitiveeventsapi.dto.DTO;
import lombok.Getter;
import lombok.Setter;

public class RewardPostDTO extends DTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	@NotBlank
	private String title;
	
	@Getter
	@Setter
	private String description;
	
	@Getter
	@Setter
	@NotBlank
	private String eventId;
	
	@Getter
	@Setter
	@NotNull
	private Integer requiredPosition;

	@Override
	public String toString() {
		return "RewardPostDTO [title=" + title + "]";
	}

}
