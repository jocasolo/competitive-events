package es.jocasolo.competitiveeventsapi.service;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.jocasolo.competitiveeventsapi.dao.EventDAO;
import es.jocasolo.competitiveeventsapi.dto.event.EventDTO;
import es.jocasolo.competitiveeventsapi.dto.event.EventPostDTO;
import es.jocasolo.competitiveeventsapi.enums.event.EventType;
import es.jocasolo.competitiveeventsapi.exceptions.EventNotFoundException;
import es.jocasolo.competitiveeventsapi.model.event.Event;

@Service
public class EventServiceImpl implements EventService {
	
	@Autowired
	private EventDAO eventDao;
	
	@Autowired
	private CommonService commonService;
	
	@Override
	@Transactional(readOnly = true)
	public Event findOne(String id) throws EventNotFoundException {
		final Event event = eventDao.findOne(id);
		if (event == null)
			throw new EventNotFoundException();

		return event;
	}
	
	@Override
	public EventDTO create(EventPostDTO eventDto) {
		Event event = commonService.transform(eventDto, Event.class);
		event.setCreationDate(new Date());
		event.setUuid(UUID.randomUUID().toString());
		event.setType(EventType.SPORTS);
		return commonService.transform(eventDao.save(event), EventDTO.class);
	}

}
