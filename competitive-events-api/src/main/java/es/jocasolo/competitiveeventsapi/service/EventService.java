package es.jocasolo.competitiveeventsapi.service;

import org.springframework.data.domain.PageRequest;

import es.jocasolo.competitiveeventsapi.dto.event.EventDTO;
import es.jocasolo.competitiveeventsapi.dto.event.EventPageDTO;
import es.jocasolo.competitiveeventsapi.dto.event.EventPostDTO;
import es.jocasolo.competitiveeventsapi.dto.event.EventPutDTO;
import es.jocasolo.competitiveeventsapi.dto.eventuser.EventUserDTO;
import es.jocasolo.competitiveeventsapi.dto.eventuser.EventUserPostDTO;
import es.jocasolo.competitiveeventsapi.enums.event.EventInscriptionType;
import es.jocasolo.competitiveeventsapi.enums.event.EventStatusType;
import es.jocasolo.competitiveeventsapi.enums.event.EventType;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventInvalidStatusException;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventWrongUpdateException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserNotValidException;
import es.jocasolo.competitiveeventsapi.model.event.Event;

public interface EventService {

	/**
	 * Search for a event by id.
	 * 
	 * @param id Event id
	 * @return Event corresponding to the id searched.
	 * @throws EventNotFoundException
	 */
	Event findOne(String id) throws EventNotFoundException;

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
	 * @param id  Event id
	 * @param event
	 * @throws EventWrongUpdateException
	 * @throws EventInvalidStatusException
	 */
	void update(String id, EventPutDTO eventDto) throws EventWrongUpdateException, EventInvalidStatusException;

	/**
	 * Deletes a event by id
	 * 
	 * @param id Event id
	 * @throws EventNotFoundException
	 */
	void delete(String id) throws EventNotFoundException;

	/**
	 * Searches for events based on supplied search parameters.
	 * @param title Event title text
	 * @param type Type of event
	 * @param status Status of the event
	 * @param inscription Inscription type
	 * @param username User name
	 * @param pageRequest Pagination data
	 * @return
	 * @throws UserNotValidException 
	 */
	EventPageDTO search(String title, EventType type, EventStatusType status, EventInscriptionType inscription, String username, PageRequest pageRequest) throws UserNotValidException;
	
	// EVENT USER
	/**
	 * @param eventId
	 * @param eventUserDto
	 * @return
	 * @throws EventWrongUpdateException
	 */
	EventUserDTO addUser(String eventId, EventUserPostDTO eventUserDto) throws EventWrongUpdateException;

}
