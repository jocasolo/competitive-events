package es.jocasolo.competitiveeventsapi.service;

import java.util.Date;

import org.springframework.data.domain.Pageable;

import es.jocasolo.competitiveeventsapi.dto.event.EventDTO;
import es.jocasolo.competitiveeventsapi.dto.event.EventPageDTO;
import es.jocasolo.competitiveeventsapi.dto.event.EventPostDTO;
import es.jocasolo.competitiveeventsapi.dto.event.EventPutDTO;
import es.jocasolo.competitiveeventsapi.enums.event.EventInscriptionType;
import es.jocasolo.competitiveeventsapi.enums.event.EventType;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventInvalidStatusException;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventWrongUpdateException;
import es.jocasolo.competitiveeventsapi.model.event.Event;

public interface EventService {

	/**
	 * Search for a event by uuid.
	 * 
	 * @param uuid Event uuid
	 * @return Event corresponding to the id searched.
	 * @throws EventNotFoundException
	 */
	Event findOne(String uuid) throws EventNotFoundException;

	/**
	 * Creates a new event.
	 * 
	 * @param eventDTO DTO with creation properties
	 * @return The new event.
	 */
	EventDTO create(EventPostDTO eventDto);

	/**
	 * Updates an event.
	 * 
	 * @param uuid  Event uuid
	 * @param event
	 * @throws EventWrongUpdateException
	 * @throws EventInvalidStatusException
	 */
	void update(String uuid, EventPutDTO eventDto) throws EventWrongUpdateException, EventInvalidStatusException;

	/**
	 * Deletes a event by uuid
	 * 
	 * @param uuid Event uuid
	 * @throws EventNotFoundException
	 */
	void delete(String uuid) throws EventNotFoundException;

	/**
	 * @param title
	 * @param initDate
	 * @param endDate
	 * @param type
	 * @param inscription
	 * @param pageRequest
	 * @return
	 */
	EventPageDTO search(String title, Date initDate, Date endDate, EventType type, EventInscriptionType inscription, Pageable pageRequest);

}
