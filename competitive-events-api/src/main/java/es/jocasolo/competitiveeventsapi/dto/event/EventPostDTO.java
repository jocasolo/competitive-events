package es.jocasolo.competitiveeventsapi.dto.event;

import java.io.Serializable;
import java.util.Date;

import es.jocasolo.competitiveeventsapi.dto.DTO;
import es.jocasolo.competitiveeventsapi.enums.event.EventInscriptionType;
import es.jocasolo.competitiveeventsapi.enums.event.EventType;
import es.jocasolo.competitiveeventsapi.enums.event.EventVisibilityType;
import es.jocasolo.competitiveeventsapi.enums.score.ScoreSortType;
import es.jocasolo.competitiveeventsapi.enums.score.ScoreValueType;
import lombok.Getter;
import lombok.Setter;

public class EventPostDTO extends DTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private String title;

	@Getter
	@Setter
	private String subtitle;

	@Getter
	@Setter
	private String description;

	@Getter
	@Setter
	private EventType type;

	@Getter
	@Setter
	private EventInscriptionType inscription;

	@Getter
	@Setter
	private EventVisibilityType visibility;

	@Getter
	@Setter
	private Boolean approvalNeeded;

	@Getter
	@Setter
	private Date initDate;

	@Getter
	@Setter
	private Date endDate;

	@Getter
	@Setter
	private Integer maxPlaces;

	@Getter
	@Setter
	private ScoreValueType scoreType;
	
	@Getter
	@Setter
	private ScoreSortType sortScore;

	@Override
	public String toString() {
		return "EventPostDTO [title=" + title + "]";
	}

}
