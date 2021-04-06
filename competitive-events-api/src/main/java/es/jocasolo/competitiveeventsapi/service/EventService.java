package es.jocasolo.competitiveeventsapi.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import es.jocasolo.competitiveeventsapi.dto.event.EventDTO;
import es.jocasolo.competitiveeventsapi.dto.event.EventPageDTO;
import es.jocasolo.competitiveeventsapi.dto.event.EventPostDTO;
import es.jocasolo.competitiveeventsapi.dto.event.EventPutDTO;
import es.jocasolo.competitiveeventsapi.dto.eventuser.EventUserDTO;
import es.jocasolo.competitiveeventsapi.dto.eventuser.EventUserPostDTO;
import es.jocasolo.competitiveeventsapi.dto.eventuser.EventUserPutDTO;
import es.jocasolo.competitiveeventsapi.enums.event.EventInscriptionType;
import es.jocasolo.competitiveeventsapi.enums.event.EventStatusType;
import es.jocasolo.competitiveeventsapi.enums.event.EventType;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventInvalidStatusException;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventUserRejectedException;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventWrongUpdateException;
import es.jocasolo.competitiveeventsapi.exceptions.image.ImageUploadException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserNotValidException;
import es.jocasolo.competitiveeventsapi.model.Event;

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
	 * @throws EventNotFoundException 
	 */
	void update(String id, EventPutDTO eventDto) throws EventWrongUpdateException, EventInvalidStatusException, EventNotFoundException;
	
	/**
	 * @param id
	 * @param file
	 * @return
	 * @throws EventNotFoundException 
	 * @throws UserNotValidException 
	 * @throws ImageUploadException 
	 */
	EventDTO updateImage(String id, MultipartFile file) throws EventNotFoundException, UserNotValidException, ImageUploadException;

	/**
	 * Deletes a event by id
	 * 
	 * @param id Event id
	 * @throws EventNotFoundException
	 * @throws UserNotValidException 
	 */
	void delete(String id) throws EventNotFoundException, UserNotValidException;

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
	
	/**
	 * Finish an event, called via rest
	 * @param e Valid event object
	 * @throws EventNotFoundException 
	 * @throws UserNotValidException 
	 * @throws EventInvalidStatusException 
	 */
	void finish(String id) throws EventNotFoundException, UserNotValidException, EventInvalidStatusException;
	
	/**
	 * Finish an event, called via scheduler
	 * @param e Valid event object
	 */
	void finishEvent(Event event);
	
	/**
	 * @param id
	 */
	void init(String id) throws EventNotFoundException, UserNotValidException, EventInvalidStatusException;
	
	/**
	 * @param event
	 */
	void initEvent(Event event);
	
	// EVENT USER
	/**
	 * @param eventId
	 * @param eventUserDto
	 * @return
	 * @throws EventWrongUpdateException
	 * @throws UserNotValidException 
	 * @throws UserNotFoundException 
	 * @throws EventUserRejectedException 
	 * @throws EventNotFoundException 
	 * @throws EventInvalidStatusException 
	 */
	EventUserDTO addUser(String eventId, EventUserPostDTO eventUserDto) 
			throws EventWrongUpdateException, UserNotValidException, UserNotFoundException, EventUserRejectedException, EventNotFoundException, EventInvalidStatusException;

	/**
	 * Removes an user from an event
	 * @param id Event id
	 * @param event Post dto
	 * @return
	 * @throws UserNotFoundException 
	 * @throws EventWrongUpdateException 
	 */
	void removeUser(String id, EventUserPostDTO eventUserDto) throws UserNotFoundException, EventWrongUpdateException;

	/**
	 * Updates info of user in an event
	 * @param id Event id
	 * @param eventDTO Put dto
	 * @throws UserNotFoundException 
	 * @throws EventWrongUpdateException 
	 */
	void updateUser(String id, EventUserPutDTO eventDTO) throws UserNotFoundException, EventWrongUpdateException;

}
