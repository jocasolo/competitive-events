package es.jocasolo.competitiveeventsapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Date;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import es.jocasolo.competitiveeventsapi.dao.EventDAO;
import es.jocasolo.competitiveeventsapi.dao.EventUserDAO;
import es.jocasolo.competitiveeventsapi.dto.event.EventDTO;
import es.jocasolo.competitiveeventsapi.dto.event.EventDetailDTO;
import es.jocasolo.competitiveeventsapi.dto.event.EventPageDTO;
import es.jocasolo.competitiveeventsapi.dto.event.EventPostDTO;
import es.jocasolo.competitiveeventsapi.dto.event.EventPutDTO;
import es.jocasolo.competitiveeventsapi.dto.image.ImageDTO;
import es.jocasolo.competitiveeventsapi.enums.event.EventInscriptionType;
import es.jocasolo.competitiveeventsapi.enums.event.EventStatusType;
import es.jocasolo.competitiveeventsapi.enums.event.EventType;
import es.jocasolo.competitiveeventsapi.enums.event.EventVisibilityType;
import es.jocasolo.competitiveeventsapi.enums.eventuser.EventUserPrivilegeType;
import es.jocasolo.competitiveeventsapi.enums.eventuser.EventUserStatusType;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventInvalidStatusException;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventWrongUpdateException;
import es.jocasolo.competitiveeventsapi.exceptions.image.ImageUploadException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserNotValidException;
import es.jocasolo.competitiveeventsapi.mappers.EventMapper;
import es.jocasolo.competitiveeventsapi.model.Event;
import es.jocasolo.competitiveeventsapi.model.EventUser;
import es.jocasolo.competitiveeventsapi.model.User;
import es.jocasolo.competitiveeventsapi.utils.security.AuthenticationFacade;

@RunWith(MockitoJUnitRunner.class)
class TestEventService {
	
	@InjectMocks
	private EventService eventService = new EventServiceImpl();
	
	@Mock
	private EventDAO eventDao;
	
	@Mock
	private EventUserDAO eventUserDao;
	
	@Mock
	private DozerBeanMapper dozer;
	
	@Mock
	private CommonService commonService;
	
	@Mock
	private ImageService imageService;
	
	@Mock
	private EventMapper eventMapper;
	
	@Mock
	private AuthenticationFacade authenticationFacade = new AuthenticationFacade();
	
	private static final String ID = "ABC";
	private static final String ID2 = "DEF";
	
	private User user = new User();
	private Event event = new Event();
	private EventUser eventUser = new EventUser();
	private Event createdEvent = new Event();
	private EventDTO eventDto = new EventDTO();
	private EventDetailDTO eventDetailDto = new EventDetailDTO();
	private EventPutDTO eventPutDto = new EventPutDTO();
	private EventPostDTO eventPostDto = new EventPostDTO();
	
	@BeforeEach
	void init() {
		
		MockitoAnnotations.openMocks(this);
		
		user.setId("test1");
		event.setId(ID);
		eventUser.setPrivilege(EventUserPrivilegeType.OWNER);
		
		eventDto.setId(ID2);
		eventDetailDto.setId(ID);
		eventPutDto.setId(ID);
		eventPostDto.setTitle("Created event");
		
		createdEvent.setId(ID+1);
		
		Mockito.when(eventDao.findOne(ID)).thenReturn(event);
		Mockito.when(eventDao.save(event)).thenReturn(event);
		Mockito.when(eventDao.save(createdEvent)).thenReturn(createdEvent);
		
		Mockito.when(commonService.transform(eventPostDto, Event.class)).thenReturn(createdEvent);
		Mockito.when(commonService.transform(createdEvent, EventDTO.class)).thenReturn(eventDto);
		Mockito.when(commonService.transform(event, EventDTO.class)).thenReturn(eventDto);
		
		Mockito.when(eventMapper.mapDetail(event)).thenReturn(eventDetailDto);
		Mockito.when(authenticationFacade.getUser()).thenReturn(user);
		
		Mockito.when(eventUserDao.save(Mockito.any())).thenReturn(eventUser);
		Mockito.when(eventUserDao.findOne(Mockito.any(), Mockito.any())).thenReturn(eventUser);
		Mockito.when(eventUserDao.findOneByIds(Mockito.any(), Mockito.any())).thenReturn(eventUser);
		
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
		
		assertEquals("New title", event.getTitle());
		assertEquals("New description", event.getDescription());
		assertEquals("EventPostDTO [title=" + dto.getTitle() + "]", dto.toString());
		
		assertThrows(EventNotFoundException.class, () -> {
			eventService.update("don't exists", dto);
	    });
		
	}
	
	@Test
	void testDelete() throws EventNotFoundException, UserNotValidException{
		eventService.delete(ID);
		assertEquals(EventStatusType.DELETED, event.getStatus());
		
		assertThrows(EventNotFoundException.class, () -> {
			eventService.delete("don't exists");
		});
		
		eventUser.setPrivilege(EventUserPrivilegeType.USER);
		assertThrows(UserNotValidException.class, () -> {
			eventService.delete(ID);
		});
	}
	
	@Test
	void testCreate() {
		EventDTO eventDto = eventService.create(eventPostDto);
		assertEquals(ID2, eventDto.getId());
	}
	
	
	@Test
	void testUpdateImage()throws EventNotFoundException, UserNotValidException, ImageUploadException {
		
		ImageDTO image = new ImageDTO();
		image.setId(1);
		image.setName("image");
		eventDto.setImage(image);
		MultipartFile file = Mockito.mock(MultipartFile.class);
		
		EventDTO result = eventService.updateImage(ID, file);
		
		assertNotNull(result.getImage());
		assertEquals(1, result.getImage().getId());
		assertEquals("image", result.getImage().getName());
		
		assertThrows(EventNotFoundException.class, () -> {
			eventService.updateImage("don't exists", file);
		});
		
		eventUser.setPrivilege(EventUserPrivilegeType.USER);
		assertThrows(UserNotValidException.class, () -> {
			eventService.updateImage(ID, file);
		});
	}
	
	@Test
	void testSearch() throws UserNotValidException {
		
		Page<Event> page = Mockito.mock(Page.class);
		
		Mockito.when(eventDao.search(ID, null, null, null, null, null)).thenReturn(page);
		Mockito.when(eventDao.searchByUser("title", "ACADEMIC", "ACTIVE", "ACCEPTED", "PRIVATE", user, null)).thenReturn(page);
		Mockito.when(page.getContent()).thenReturn(List.of(event));
		Mockito.when(eventMapper.map(page.getContent())).thenReturn(List.of(eventDto));
		
		EventPageDTO result = eventService.search(ID, null, null, null, null, null, null, null);
		assertNotNull(result.getEvents());
		assertEquals(1, result.getEvents().size());
		
		result = eventService.search(ID, "title", EventType.ACADEMIC, EventStatusType.ACTIVE, 
				EventUserStatusType.ACCEPTED, EventInscriptionType.PRIVATE, "test1", null);
		assertNotNull(result.getEvents());
		assertEquals(1, result.getEvents().size());
		
	}
}
