package es.jocasolo.competitiveeventsapi.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.multipart.MultipartFile;

import es.jocasolo.competitiveeventsapi.dto.event.EventDTO;
import es.jocasolo.competitiveeventsapi.dto.event.EventDetailDTO;
import es.jocasolo.competitiveeventsapi.dto.event.EventPageDTO;
import es.jocasolo.competitiveeventsapi.dto.event.EventPostDTO;
import es.jocasolo.competitiveeventsapi.dto.event.EventPutDTO;
import es.jocasolo.competitiveeventsapi.dto.eventuser.EventUserDTO;
import es.jocasolo.competitiveeventsapi.dto.eventuser.EventUserPostDTO;
import es.jocasolo.competitiveeventsapi.dto.eventuser.EventUserPutDTO;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventInvalidStatusException;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventNotAvailablePlacesException;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventUserAcceptedException;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventUserRejectedException;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventWrongUpdateException;
import es.jocasolo.competitiveeventsapi.exceptions.image.ImageUploadException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserNotValidException;
import es.jocasolo.competitiveeventsapi.model.Event;
import es.jocasolo.competitiveeventsapi.model.User;
import es.jocasolo.competitiveeventsapi.service.EventService;

@RunWith(MockitoJUnitRunner.class)
class TestEventController {
	
	@InjectMocks
	private EventController eventController = new EventController();
	
	@Mock
	private EventService eventService;
	
	private Event event = new Event();
	private EventDTO eventDto = new EventDTO();
	
	private static final String ID = "event";
			
	
	@BeforeEach
	void init() throws EventNotFoundException {
		
		MockitoAnnotations.openMocks(this);
		
		event = new Event();
		event.setId("event");
		User user = new User();
		user.setId("user");
		
		event.setId(ID);
		event.setTitle("title");
		eventDto.setId(event.getId());
		eventDto.setTitle(event.getTitle());
		
	}
	
	@Test
	void testFindOne() throws EventNotFoundException {
		EventDetailDTO dto = new EventDetailDTO();
		dto.setId(ID);
		
		Mockito.when(eventService.findOne(ID)).thenReturn(dto);
		EventDetailDTO result = eventController.findOne(ID);
		assertNotNull(result);
		assertEquals(ID, result.getId());
	}
	
	@Test
	void testCreate() throws EventNotFoundException, UserNotValidException, EventInvalidStatusException {
		EventPostDTO dto = new EventPostDTO();
		Mockito.when(eventService.create(dto)).thenReturn(eventDto);
		
		EventDTO result = eventController.create(dto);
		assertNotNull(result);
		assertEquals(ID, result.getId());
		assertEquals("title", result.getTitle());
	}
	
	@Test
	void testUpdateImage() throws ImageUploadException, EventNotFoundException, UserNotValidException, EventInvalidStatusException {
		MultipartFile file = Mockito.mock(MultipartFile.class);
		Mockito.when(eventService.updateImage(ID, file)).thenReturn(eventDto);
		
		EventDTO result = eventController.updateImage(ID, file);
		assertNotNull(result);
		assertEquals(ID, result.getId());
	}
	
	@Test
	void testSearch() throws UserNotValidException {
		EventPageDTO page = new EventPageDTO();
		page.setTotal(1L);
		page.setEvents(List.of(eventDto));
		Mockito.when(eventService.search(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(page);
		EventPageDTO result = eventController.search(ID, "title", null, null, null, null, null, 1, 5);
		assertNotNull(result);
	}
	
	@Test
	void testEventUserAddUser() throws UserNotFoundException, EventNotFoundException, EventWrongUpdateException, UserNotValidException, EventUserRejectedException, EventInvalidStatusException, EventUserAcceptedException, EventNotAvailablePlacesException {
		
		EventUserPostDTO dto = new EventUserPostDTO();
		EventUserDTO eventUser = new EventUserDTO();
		eventUser.setEventId(ID);
		eventUser.setUserId("user");
		
		Mockito.when(eventService.addUser(ID, dto)).thenReturn(eventUser);
		
		EventUserDTO result = eventController.addUser(ID, dto);
		assertNotNull(result);
		assertEquals(ID, result.getEventId());
		assertEquals("user", result.getUserId());
	}
	
	@Test
	void testEventUserFindOne() throws UserNotFoundException, EventNotFoundException {
		EventUserDTO eventUser = new EventUserDTO();
		eventUser.setEventId(ID);
		eventUser.setUserId("user");
		Mockito.when(eventService.findEventAndUser(event.getId(), "user")).thenReturn(eventUser);
		
		EventUserDTO result = eventController.findOne(ID, "user");
		assertNotNull(result);
		assertEquals(ID, result.getEventId());
		assertEquals("user", result.getUserId());
	}
	
	@Test
	void testEventUserRemoveUser() throws UserNotFoundException, EventNotFoundException, EventWrongUpdateException {
		EventUserPostDTO dto = new EventUserPostDTO();
		eventController.removeUser(ID, dto);
	}
	
	@Test
	void testEventUserUpdateUser() throws UserNotFoundException, EventNotFoundException, EventWrongUpdateException {
		EventUserPutDTO dto = new EventUserPutDTO();
		eventController.updateUser(ID, dto);
	}
	
	@Test
	void testUpdate() throws EventWrongUpdateException, EventInvalidStatusException, EventNotFoundException {
		EventPutDTO dto = new EventPutDTO();
		eventController.update(ID, dto);
	}
	
	@Test
	void testDelete() throws EventNotFoundException, UserNotValidException, EventNotFoundException, EventInvalidStatusException {
		eventController.delete(ID);
	}
	
	@Test
	void testInit() throws EventInvalidStatusException, EventNotFoundException, UserNotValidException {
		eventController.init(ID);
	}
	
	@Test
	void testFinish() throws EventInvalidStatusException, EventNotFoundException, UserNotValidException {
		eventController.finish(ID);
	}
	
	
	
}
