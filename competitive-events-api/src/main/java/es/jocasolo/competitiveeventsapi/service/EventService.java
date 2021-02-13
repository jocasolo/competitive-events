package es.jocasolo.competitiveeventsapi.service;

import es.jocasolo.competitiveeventsapi.dto.event.EventDTO;
import es.jocasolo.competitiveeventsapi.dto.event.EventPostDTO;
import es.jocasolo.competitiveeventsapi.exceptions.EventNotFoundException;
import es.jocasolo.competitiveeventsapi.model.event.Event;

public interface EventService {

	Event findOne(String id) throws EventNotFoundException;
	
	/**
	 * Creates a new event.
	 * @param eventDTO DTO with creation properties
	 * @return The new event.
	 */
	EventDTO create(EventPostDTO eventDto);
	
}
