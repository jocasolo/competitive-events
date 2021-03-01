package es.jocasolo.competitiveeventsapi.controller;

import java.security.Principal;
import java.util.Date;

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

import es.jocasolo.competitiveeventsapi.dto.event.EventDTO;
import es.jocasolo.competitiveeventsapi.dto.event.EventPageDTO;
import es.jocasolo.competitiveeventsapi.dto.event.EventPostDTO;
import es.jocasolo.competitiveeventsapi.dto.event.EventPutDTO;
import es.jocasolo.competitiveeventsapi.dto.event.EventUserDTO;
import es.jocasolo.competitiveeventsapi.dto.event.EventUserPostDTO;
import es.jocasolo.competitiveeventsapi.enums.event.EventInscriptionType;
import es.jocasolo.competitiveeventsapi.enums.event.EventType;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventInvalidStatusException;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventWrongUpdateException;
import es.jocasolo.competitiveeventsapi.service.CommonService;
import es.jocasolo.competitiveeventsapi.service.EventService;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/events")
public class EventController {
	
	private static final Logger log = LoggerFactory.getLogger(EventController.class);
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private CommonService commonService;
	
	@GetMapping(value = "/{id}", produces = "application/json;charset=utf8")
	@ApiOperation(value = "Search for an event based on its id.")
	public EventDTO findOne(@PathVariable("id") String id, Principal principal) throws EventNotFoundException {
		log.debug("Looking for the event with id: {}", id);
		return commonService.transform(eventService.findOne(id), EventDTO.class);
	}
	
	@GetMapping(produces = "application/json;charset=utf8")
	@ApiOperation(value = "Find all events that match your search parameters.")
	public EventPageDTO search(
			@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "subtitle", required = false) String subtitle,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "initDate", required = false) Date initDate,
			@RequestParam(value = "endDate", required = false) Date endDate,
			@RequestParam(value = "type", required = false) EventType type,
			@RequestParam(value = "inscription", required = false) EventInscriptionType inscription,
			@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
			@RequestParam(value = "size", required = false, defaultValue = "10") Integer size, 
			Principal principal) {
		log.debug("Looking for events");
		return eventService.search(title, initDate, endDate, type, inscription, PageRequest.of(page, size));
	}
	
	@PostMapping(produces = "application/json;charset=utf8")
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Creates a new event.")
	public EventDTO create(@RequestBody EventPostDTO event, Principal principal) {
		log.debug("Creating the event: {} ", event);
		return eventService.create(event);
	}
	
	@PutMapping(value = "/{id}")
	@ApiOperation(value = "Updates an event by id.")
	public void update(
			@PathVariable("id") String id, 
			@RequestBody EventPutDTO eventDTO, Principal principal) throws EventWrongUpdateException, EventInvalidStatusException {
		log.debug("Updating event: {}", eventDTO);
		eventService.update(id, eventDTO);
	}

	@DeleteMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Delete an event by id.")
	public void delete(@PathVariable("id") String id, Principal principal) throws EventNotFoundException {
		log.debug("Deleting event with id: {} ", id);
		eventService.delete(id);
	}
	
	// EVENT USER
	@PostMapping(value = "/{id}/users", produces = "application/json;charset=utf8")
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Adds user to an event.")
	public EventUserDTO addUser(
			@PathVariable("id") String id,
			@RequestBody EventUserPostDTO event, Principal principal) throws EventWrongUpdateException {
		log.debug("Adding user to event: {} ", event);
		return eventService.addUser(id, event);
	}
	
}
