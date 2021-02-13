package es.jocasolo.competitiveeventsapi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.jocasolo.competitiveeventsapi.dto.event.EventDTO;
import es.jocasolo.competitiveeventsapi.dto.event.EventPostDTO;
import es.jocasolo.competitiveeventsapi.dto.event.EventPutDTO;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventInvalidStatusException;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventWrongUpdateException;
import es.jocasolo.competitiveeventsapi.service.CommonService;
import es.jocasolo.competitiveeventsapi.service.EventService;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/event")
public class EventController {
	
	private static final Logger log = LoggerFactory.getLogger(EventController.class);
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private CommonService commonService;
	
	@GetMapping(value = "/{uuid}")
	@ApiOperation(value = "Search for an event based on its uuid.")
	public EventDTO findOne(@PathVariable("uuid") String uuid) throws EventNotFoundException {
		log.debug("Looking for the event with uuid: {}", uuid);
		return commonService.transform(eventService.findOne(uuid), EventDTO.class);
	}
	
	@PostMapping
	@ApiOperation(value = "Creates a new event.")
	public EventDTO create(@RequestBody EventPostDTO event) {
		log.debug("Creating the event: {} ", event);
		return eventService.create(event);
	}
	
	@PutMapping(value = "/{uuid}")
	@ApiOperation(value = "Updates an event by uuid.")
	public void update(
			@PathVariable("uuid") String uuid, 
			@RequestBody EventPutDTO eventDTO) throws EventWrongUpdateException, EventInvalidStatusException {
		log.debug("Modificando el libro: {}", eventDTO);
		eventService.update(uuid, eventDTO);
	}

	@DeleteMapping(value = "/{uuid}")
	@ApiOperation(value = "Delete an event by uuid.")
	public void delete(@PathVariable("id") String uuid) throws EventNotFoundException {
		log.debug("Deleting event with uuid: {} ", uuid);
		eventService.delete(uuid);
	}
	
}
