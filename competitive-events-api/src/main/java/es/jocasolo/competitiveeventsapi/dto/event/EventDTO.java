package es.jocasolo.competitiveeventsapi.dto.event;

import java.io.Serializable;
import java.util.Date;

import es.jocasolo.competitiveeventsapi.dto.DTO;
import es.jocasolo.competitiveeventsapi.dto.image.ImageDTO;
import es.jocasolo.competitiveeventsapi.enums.event.EventInscriptionType;
import es.jocasolo.competitiveeventsapi.enums.event.EventType;
import es.jocasolo.competitiveeventsapi.enums.event.EventVisibilityType;
import es.jocasolo.competitiveeventsapi.enums.score.ScoreValueType;
import lombok.Getter;
import lombok.Setter;

public class EventDTO extends DTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private String id;

	@Getter
	@Setter
	private String title;

	@Getter
	@Setter
	private String subtitle;

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
	private Integer numParticipants;

	@Getter
	@Setter
	private ImageDTO image;

	@Getter
	@Setter
	private ScoreValueType scoreType;	

	@Override
	public String toString() {
		return "EventDTO [id=" + id + "]";
	}

}
