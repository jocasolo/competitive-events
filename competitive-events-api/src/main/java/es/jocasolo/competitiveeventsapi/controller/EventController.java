package es.jocasolo.competitiveeventsapi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import es.jocasolo.competitiveeventsapi.dto.event.EventDTO;
import es.jocasolo.competitiveeventsapi.dto.event.EventDetailDTO;
import es.jocasolo.competitiveeventsapi.dto.event.EventPageDTO;
import es.jocasolo.competitiveeventsapi.dto.event.EventPostDTO;
import es.jocasolo.competitiveeventsapi.dto.event.EventPutDTO;
import es.jocasolo.competitiveeventsapi.dto.eventuser.EventUserDTO;
import es.jocasolo.competitiveeventsapi.dto.eventuser.EventUserPostDTO;
import es.jocasolo.competitiveeventsapi.dto.eventuser.EventUserPutDTO;
import es.jocasolo.competitiveeventsapi.enums.event.EventInscriptionType;
import es.jocasolo.competitiveeventsapi.enums.event.EventStatusType;
import es.jocasolo.competitiveeventsapi.enums.event.EventType;
import es.jocasolo.competitiveeventsapi.enums.eventuser.EventUserStatusType;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventInvalidStatusException;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventNotAvailablePlacesException;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventUserAcceptedException;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventUserRejectedException;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventWrongUpdateException;
import es.jocasolo.competitiveeventsapi.exceptions.image.ImageUploadException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserNotValidException;
import es.jocasolo.competitiveeventsapi.service.EventService;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/events")
public class EventController {
	
	private static final Logger log = LoggerFactory.getLogger(EventController.class);
	
	@Autowired
	private EventService eventService;
	
	@GetMapping(value = "/{id}", produces = "application/json;charset=utf8")
	@ApiOperation(value = "Search for an event based on its id.")
	public EventDetailDTO findOne(@PathVariable("id") String id) throws EventNotFoundException {
		log.debug("Looking for the event with id: {}", id);
		return eventService.findOne(id);
	}
	
	@GetMapping(produces = "application/json;charset=utf8")
	@ApiOperation(value = "Find all events that match your search parameters.")
	public EventPageDTO search(
			@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "type", required = false) EventType type,
			@RequestParam(value = "status", required = false) EventStatusType status,
			@RequestParam(value = "eventUserStatus", required = false) EventUserStatusType eventUserStatus,
			@RequestParam(value = "inscription", required = false) EventInscriptionType inscription,
			@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
			@RequestParam(value = "size", required = false, defaultValue = "10") Integer size) throws UserNotValidException {
		log.debug("Looking for events");
		return eventService.search(id, title, type, status, eventUserStatus, inscription, username, PageRequest.of(page, size));
	}
	
	@PostMapping(produces = "application/json;charset=utf8")
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Creates a new event.")
	public EventDTO create(@RequestBody EventPostDTO event) {
		log.debug("Creating the event: {} ", event);
		return eventService.create(event);
	}
	
	@PutMapping(value = "/{id}")
	@ApiOperation(value = "Updates an event by id.")
	public void update(
			@PathVariable("id") String id, 
			@RequestBody EventPutDTO eventDTO) throws EventWrongUpdateException, EventInvalidStatusException, EventNotFoundException {
		log.debug("Updating event: {}", eventDTO);
		eventService.update(id, eventDTO);
	}
	
	@PutMapping(value = "/{id}/finish")
	@ApiOperation(value = "Updates an event by id.")
	public void finish(
			@PathVariable("id") String id) throws EventInvalidStatusException, EventNotFoundException, UserNotValidException {
		log.debug("Finishing event: {}", id);
		eventService.finish(id);
	}
	
	@PutMapping(value = "/{id}/init")
	@ApiOperation(value = "Updates an event by id.")
	public void init(
			@PathVariable("id") String id) throws EventInvalidStatusException, EventNotFoundException, UserNotValidException {
		log.debug("Init event: {}", id);
		eventService.init(id);
	}
	
	@PutMapping(value = "/{id}/image", produces = "application/json;charset=utf8")
	@ApiOperation(value = "Updates an event image.")
	public EventDTO updateImage(
			@PathVariable("id") String id, 
			@RequestParam("file") MultipartFile file) throws ImageUploadException, EventNotFoundException, UserNotValidException {
		log.debug("Updating event image with id: {}", id);
		return eventService.updateImage(id, file);
	}

	@DeleteMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Delete an event by id.")
	public void delete(@PathVariable("id") String id) throws EventNotFoundException, UserNotValidException {
		log.debug("Deleting event with id: {} ", id);
		eventService.delete(id);
	}
	
	// *************************************
	// EVENT USER
	// *************************************
	
	@GetMapping(value = "/{eventId}/users/{userId}", produces = "application/json;charset=utf8")
	@ApiOperation(value = "Search for an event based on its id.")
	public EventUserDTO findOne(@PathVariable("eventId") String eventId, @PathVariable("userId") String userId) throws EventNotFoundException, UserNotFoundException {
		log.debug("Looking for the event with id: {} and user with id: {}", eventId, userId);
		return eventService.findEventAndUser(eventId, userId);
	}
	
	@PostMapping(value = "/{id}/users", produces = "application/json;charset=utf8")
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Adds user to an event.")
	public EventUserDTO addUser(
			@PathVariable("id") String id,
			@RequestBody EventUserPostDTO event) 
					throws EventWrongUpdateException, UserNotValidException, UserNotFoundException, EventUserRejectedException, 
					EventNotFoundException, EventInvalidStatusException, EventUserAcceptedException, EventNotAvailablePlacesException {
		log.debug("Adding user to event: {} ", event);
		return eventService.addUser(id, event);
	}
	
	@DeleteMapping(value = "/{id}/users", produces = "application/json;charset=utf8")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Remove user from an event.")
	public void removeUser(
			@PathVariable("id") String id,
			@RequestBody EventUserPostDTO event) throws EventWrongUpdateException, UserNotFoundException {
		log.debug("Removing user from an event: {} ", event);
		eventService.removeUser(id, event);
	}
	
	@PutMapping(value = "/{id}/users")
	@ApiOperation(value = "Updates an user in an event.")
	public void updateUser(
			@PathVariable("id") String id, 
			@RequestBody EventUserPutDTO eventDTO) throws EventWrongUpdateException, UserNotFoundException {
		log.debug("Updating event: {}", eventDTO);
		eventService.updateUser(id, eventDTO);
	}
	
}
