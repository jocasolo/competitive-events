package es.jocasolo.competitiveeventsapi.service;

import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.jocasolo.competitiveeventsapi.dao.EventDAO;
import es.jocasolo.competitiveeventsapi.dto.event.EventDTO;
import es.jocasolo.competitiveeventsapi.dto.event.EventPageDTO;
import es.jocasolo.competitiveeventsapi.dto.event.EventPostDTO;
import es.jocasolo.competitiveeventsapi.dto.event.EventPutDTO;
import es.jocasolo.competitiveeventsapi.enums.event.EventInscriptionType;
import es.jocasolo.competitiveeventsapi.enums.event.EventStatusType;
import es.jocasolo.competitiveeventsapi.enums.event.EventType;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventInvalidStatusException;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventWrongUpdateException;
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
		event.setType(EventType.SPORTS); // TODO
		return commonService.transform(eventDao.save(event), EventDTO.class);
	}

	@Override
	public void update(String uuid, EventPutDTO dto) throws EventWrongUpdateException, EventInvalidStatusException {

		if (StringUtils.isNotEmpty(dto.getUuid()) && !dto.getUuid().equals(uuid))
			throw new EventWrongUpdateException();

		// TODO validate update
		Event event = eventDao.findOne(uuid);
		event.setTitle(dto.getTitle());
		event.setSubtitle(dto.getSubtitle());
		event.setDescription(dto.getDescription());
		event.setAppovalNeeded(dto.getAppovalNeeded());
		event.setInitDate(dto.getInitDate());
		event.setEndDate(dto.getEndDate());
		event.setType(dto.getType());
		event.setInscription(dto.getInscription());
		event.setVisibility(dto.getVisibility());
		event.setStatus(dto.getStatus());
		event.setMaxPlaces(dto.getMaxPlaces());
		eventDao.save(event);
	}

	@Override
	public void delete(String uuid) throws EventNotFoundException {

		// TODO validate delete
		Event event = eventDao.findOne(uuid);
		event.setStatus(EventStatusType.DELETED);
		eventDao.save(event);

	}

	@Override
	public EventPageDTO search(String title, Date initDate, Date endDate, EventType type, EventInscriptionType inscription, Pageable pageable) {
		final Page<Event> events = eventDao.search(title, pageable);
		EventPageDTO dto = new EventPageDTO();
		dto.setEvents(commonService.transform(events.getContent(), EventDTO.class));
		dto.setTotal(events.getTotalElements());
		dto.setHasNext(events.hasNext());
		dto.setHasPrevious(events.hasPrevious());
		dto.setPages(events.getTotalPages());
		return dto;
	}

}
