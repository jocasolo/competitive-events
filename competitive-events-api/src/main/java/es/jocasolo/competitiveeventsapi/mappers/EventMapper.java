package es.jocasolo.competitiveeventsapi.mappers;

import java.util.List;

import es.jocasolo.competitiveeventsapi.dto.event.EventDTO;
import es.jocasolo.competitiveeventsapi.dto.event.EventDetailDTO;
import es.jocasolo.competitiveeventsapi.model.Event;

public interface EventMapper {
	
	EventDTO map(Event event);
	
	List<EventDTO> map(List<Event> events);

	EventDetailDTO mapDetail(Event event);

}
