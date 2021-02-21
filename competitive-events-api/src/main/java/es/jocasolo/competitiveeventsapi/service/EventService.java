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
	 * Search for a event by code.
	 * 
	 * @param code Event code
	 * @return Event corresponding to the id searched.
	 * @throws EventNotFoundException
	 */
	Event findOne(String code) throws EventNotFoundException;

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
	 * @param code  Event code
	 * @param event
	 * @throws EventWrongUpdateException
	 * @throws EventInvalidStatusException
	 */
	void update(String code, EventPutDTO eventDto) throws EventWrongUpdateException, EventInvalidStatusException;

	/**
	 * Deletes a event by code
	 * 
	 * @param code Event code
	 * @throws EventNotFoundException
	 */
	void delete(String code) throws EventNotFoundException;

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
