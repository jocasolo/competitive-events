package es.jocasolo.competitiveeventsapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Date;

import org.dozer.DozerBeanMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import es.jocasolo.competitiveeventsapi.dao.EventDAO;
import es.jocasolo.competitiveeventsapi.dto.event.EventDTO;
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

@RunWith(MockitoJUnitRunner.class)
class TestEventService {
	
	@InjectMocks
	private EventService eventService = new EventServiceImpl();
	
	@Mock
	private EventDAO eventDao;
	
	@Mock
	private DozerBeanMapper dozer;
	
	@Mock
	private CommonService commonService = new CommonServiceImpl();
	
	private static final String UUID = "c8c71353-bb9a-4fe0-b8bc-df9fba82108b";
	private static final String UUID2 = "557ccb52-3c11-4b1a-922a-86e5cfbc85c8";
	
	private Event mockedEvent = new Event();
	private Event createdEvent = new Event();
	private EventDTO dto = new EventDTO();
	private EventPutDTO putDto = new EventPutDTO();
	private EventPostDTO postDto = new EventPostDTO();
	
	@BeforeEach
	void init() {
		
		MockitoAnnotations.initMocks(this);
		
		mockedEvent.setId(1);
		mockedEvent.setUuid(UUID);
		
		dto.setUuid(UUID2);
		putDto.setUuid(UUID);
		postDto.setTitle("Created event");
		
		createdEvent.setId(2);
		
		Mockito.when(eventDao.findOne(UUID)).thenReturn(mockedEvent);
		Mockito.when(eventDao.save(mockedEvent)).thenReturn(mockedEvent);
		Mockito.when(commonService.transform(postDto, Event.class)).thenReturn(createdEvent);
		Mockito.when(eventDao.save(createdEvent)).thenReturn(createdEvent);
		Mockito.when(commonService.transform(createdEvent, EventDTO.class)).thenReturn(dto);
	}
	
	@Test
	void testFindOne() throws EventNotFoundException {
		Event e = eventService.findOne(UUID);
		assertNotNull(e);
		assertEquals(1, e.getId());
		
		assertThrows(EventNotFoundException.class, () -> {
			eventService.findOne("don't exists");
	    });
	}
	
	@Test
	void testUpdate() throws EventWrongUpdateException, EventInvalidStatusException {
		
		EventPutDTO dto = new EventPutDTO();
		dto.setUuid(UUID);
		dto.setTitle("New title");
		dto.setSubtitle("New subtitle");
		dto.setInitDate(new Date());
		dto.setEndDate(new Date());
		dto.setDescription("New description");
		dto.setType(EventType.ACADEMIC);
		dto.setInscription(EventInscriptionType.INVITATION);
		dto.setVisibility(EventVisibilityType.PRIVATE);
		dto.setStatus(EventStatusType.ACTIVE);
		dto.setMaxPlaces(1);
		dto.setAppovalNeeded(false);
		
		eventService.update(UUID, dto);
		
		assertEquals("New title", mockedEvent.getTitle());
		assertEquals("New description", mockedEvent.getDescription());
		assertEquals("EventPostDTO [title=" + dto.getTitle() + "]", dto.toString());
		
		dto.setUuid("don't exists");
		assertThrows(EventWrongUpdateException.class, () -> {
			eventService.update(UUID, dto);
	    });
	}
	
	@Test
	void testDelete() throws EventNotFoundException{
		eventService.delete(UUID);
		assertEquals(EventStatusType.DELETED, mockedEvent.getStatus());
	}
	
	@Test
	void testCreate() {
		
		EventDTO eventDto = eventService.create(postDto);
		assertEquals(UUID2, eventDto.getUuid());
		
	}
	
	
}
