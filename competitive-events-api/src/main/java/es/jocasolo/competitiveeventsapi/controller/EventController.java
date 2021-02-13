package es.jocasolo.competitiveeventsapi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import es.jocasolo.competitiveeventsapi.dto.event.EventDTO;
import es.jocasolo.competitiveeventsapi.dto.event.EventPostDTO;
import es.jocasolo.competitiveeventsapi.exceptions.EventNotFoundException;
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
	
	@ApiOperation(value = "Search for an event based on its uuid.")
	@RequestMapping(value = "/{uuid}", method = { RequestMethod.GET })
	public EventDTO findOne(@PathVariable("uuid") String uuid) throws EventNotFoundException {
		log.debug(String.format("Looking for the event with uuid: %s", uuid));
		return commonService.transform(eventService.findOne(uuid), EventDTO.class);
	}
	
	@ApiOperation(value = "Creates a new event.")
	@RequestMapping(method = { RequestMethod.POST })
	public EventDTO create(@RequestBody EventPostDTO event) {
		log.debug(String.format("Creating the event: %s", event));
		return eventService.create(event);
	}
	
}
