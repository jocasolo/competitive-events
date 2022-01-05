package es.jocasolo.competitiveeventsapi.dto.score;

import java.io.Serializable;
import java.util.List;

import es.jocasolo.competitiveeventsapi.dto.DTO;
import lombok.Getter;
import lombok.Setter;

public class ScorePageDTO extends DTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private Long total;
	
	@Getter
	@Setter
	private Integer pages;
	
	@Getter
	@Setter
	private Boolean hasNext;
	
	@Getter
	@Setter
	private Boolean hasPrevious;
	
	@Getter
	@Setter
	private List<ScoreDTO> scores;

	@Override
	public String toString() {
		return "ScorePageDTO [total=" + total + "]";
	}

}
