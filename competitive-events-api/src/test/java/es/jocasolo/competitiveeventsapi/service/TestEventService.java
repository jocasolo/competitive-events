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
import es.jocasolo.competitiveeventsapi.dto.event.EventDetailDTO;
import es.jocasolo.competitiveeventsapi.dto.event.EventPostDTO;
import es.jocasolo.competitiveeventsapi.dto.event.EventPutDTO;
import es.jocasolo.competitiveeventsapi.enums.event.EventInscriptionType;
import es.jocasolo.competitiveeventsapi.enums.event.EventStatusType;
import es.jocasolo.competitiveeventsapi.enums.event.EventType;
import es.jocasolo.competitiveeventsapi.enums.event.EventVisibilityType;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventInvalidStatusException;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventWrongUpdateException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserNotValidException;
import es.jocasolo.competitiveeventsapi.model.Event;

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
	
	private static final String ID = "ABC";
	private static final String ID2 = "DEF";
	
	private Event mockedEvent = new Event();
	private Event createdEvent = new Event();
	private EventDTO dto = new EventDTO();
	private EventPutDTO putDto = new EventPutDTO();
	private EventPostDTO postDto = new EventPostDTO();
	
	@BeforeEach
	void init() {
		
		MockitoAnnotations.initMocks(this);
		
		mockedEvent.setId(ID);
		
		dto.setId(ID2);
		putDto.setId(ID);
		postDto.setTitle("Created event");
		
		createdEvent.setId(ID+1);
		
		Mockito.when(eventDao.findOne(ID)).thenReturn(mockedEvent);
		Mockito.when(eventDao.save(mockedEvent)).thenReturn(mockedEvent);
		Mockito.when(commonService.transform(postDto, Event.class)).thenReturn(createdEvent);
		Mockito.when(eventDao.save(createdEvent)).thenReturn(createdEvent);
		Mockito.when(commonService.transform(createdEvent, EventDTO.class)).thenReturn(dto);
	}
	
	@Test
	void testFindOne() throws EventNotFoundException {
		EventDetailDTO e = eventService.findOne(ID);
		assertNotNull(e);
		assertEquals(ID, e.getId());
		
		assertThrows(EventNotFoundException.class, () -> {
			eventService.findOne("don't exists");
	    });
	}
	
	@Test
	void testUpdate() throws EventWrongUpdateException, EventInvalidStatusException, EventNotFoundException {
		
		EventPutDTO dto = new EventPutDTO();
		dto.setId(ID);
		dto.setTitle("New title");
		dto.setSubtitle("New subtitle");
		dto.setInitDate(new Date());
		dto.setEndDate(new Date());
		dto.setDescription("New description");
		dto.setType(EventType.ACADEMIC);
		dto.setInscription(EventInscriptionType.PRIVATE);
		dto.setVisibility(EventVisibilityType.PRIVATE);
		dto.setStatus(EventStatusType.ACTIVE);
		dto.setMaxPlaces(1);
		dto.setAppovalNeeded(false);
		
		eventService.update(ID, dto);
		
		assertEquals("New title", mockedEvent.getTitle());
		assertEquals("New description", mockedEvent.getDescription());
		assertEquals("EventPostDTO [title=" + dto.getTitle() + "]", dto.toString());
		
		dto.setId("don't exists");
		assertThrows(EventWrongUpdateException.class, () -> {
			eventService.update(ID, dto);
	    });
	}
	
	@Test
	void testDelete() throws EventNotFoundException, UserNotValidException{
		eventService.delete(ID);
		assertEquals(EventStatusType.DELETED, mockedEvent.getStatus());
	}
	
	@Test
	void testCreate() {
		
		EventDTO eventDto = eventService.create(postDto);
		assertEquals(ID2, eventDto.getId());
		
	}
	
	
}
