package es.jocasolo.competitiveeventsapi.service;

import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;
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
import es.jocasolo.competitiveeventsapi.enums.event.EventVisibilityType;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventInvalidStatusException;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventWrongUpdateException;
import es.jocasolo.competitiveeventsapi.model.event.Event;
import es.jocasolo.competitiveeventsapi.utils.EventUtils;

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
	public EventDTO create(EventPostDTO dto) {
		
		Event event = commonService.transform(dto, Event.class);
		event.setCreationDate(new Date());
		event.setCode(RandomStringUtils.randomAlphanumeric(8)); // Random code
		event.setType(EventType.getValue(dto.getType(), EventType.OTHER));
		event.setInscription(EventInscriptionType.getValue(dto.getInscription(), EventInscriptionType.INVITATION));
		event.setVisibility(EventVisibilityType.getValue(dto.getVisibility(), EventVisibilityType.PRIVATE));
		event.setStatus(EventStatusType.ACTIVE);
		event.setApprovalNeeded(EventUtils.getValue(dto.getApprovalNeeded(), true));
		
		return commonService.transform(eventDao.save(event), EventDTO.class);
	}

	@Override
	public void update(String code, EventPutDTO dto) throws EventWrongUpdateException, EventInvalidStatusException {

		if (StringUtils.isNotEmpty(dto.getCode()) && !dto.getCode().equals(code))
			throw new EventWrongUpdateException();

		// TODO validate update
		Event event = eventDao.findOne(code);
		event.setTitle(EventUtils.getValue(dto.getTitle(), event.getTitle()));
		event.setSubtitle(EventUtils.getValue(dto.getSubtitle(), event.getSubtitle()));
		event.setDescription(EventUtils.getValue(dto.getDescription(), event.getDescription()));
		event.setApprovalNeeded(EventUtils.getValue(dto.getAppovalNeeded(), event.getApprovalNeeded()));
		event.setInitDate(EventUtils.getValue(dto.getInitDate(), event.getInitDate()));
		event.setEndDate(EventUtils.getValue(dto.getEndDate(), event.getEndDate()));
		event.setType(EventType.getValue(dto.getType(), event.getType()));
		event.setInscription(EventInscriptionType.getValue(dto.getInscription(), event.getInscription()));
		event.setVisibility(EventVisibilityType.getValue(dto.getVisibility(), event.getVisibility()));
		event.setStatus(EventStatusType.getValue(dto.getStatus(), event.getStatus()));
		event.setMaxPlaces(EventUtils.getValue(dto.getMaxPlaces(), event.getMaxPlaces()));
		eventDao.save(event);
	}

	@Override
	public void delete(String code) throws EventNotFoundException {

		// TODO validate delete
		Event event = eventDao.findOne(code);
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
