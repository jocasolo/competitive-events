package es.jocasolo.competitiveeventsapi.mappers;

import es.jocasolo.competitiveeventsapi.dto.event.EventDetailDTO;
import es.jocasolo.competitiveeventsapi.model.Event;

public interface EventMapper {
	
	EventDetailDTO map(Event event);

}
