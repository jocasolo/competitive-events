package es.jocasolo.competitiveeventsapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import es.jocasolo.competitiveeventsapi.dao.PunishmentDAO;
import es.jocasolo.competitiveeventsapi.dao.RewardDAO;
import es.jocasolo.competitiveeventsapi.dao.ScoreDAO;
import es.jocasolo.competitiveeventsapi.dao.UserDAO;
import es.jocasolo.competitiveeventsapi.dto.event.EventDTO;
import es.jocasolo.competitiveeventsapi.dto.event.EventDetailDTO;
import es.jocasolo.competitiveeventsapi.dto.event.EventPageDTO;
import es.jocasolo.competitiveeventsapi.dto.event.EventPostDTO;
import es.jocasolo.competitiveeventsapi.dto.event.EventPutDTO;
import es.jocasolo.competitiveeventsapi.dto.eventuser.EventUserDTO;
import es.jocasolo.competitiveeventsapi.dto.eventuser.EventUserPostDTO;
import es.jocasolo.competitiveeventsapi.dto.eventuser.EventUserPutDTO;
import es.jocasolo.competitiveeventsapi.dto.image.ImageDTO;
import es.jocasolo.competitiveeventsapi.enums.event.EventInscriptionType;
import es.jocasolo.competitiveeventsapi.enums.event.EventStatusType;
import es.jocasolo.competitiveeventsapi.enums.event.EventType;
import es.jocasolo.competitiveeventsapi.enums.event.EventVisibilityType;
import es.jocasolo.competitiveeventsapi.enums.eventuser.EventUserPrivilegeType;
import es.jocasolo.competitiveeventsapi.enums.eventuser.EventUserStatusType;
import es.jocasolo.competitiveeventsapi.enums.score.ScoreSortType;
import es.jocasolo.competitiveeventsapi.enums.score.ScoreValueType;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventInvalidStatusException;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventNotAvailablePlacesException;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventUserAcceptedException;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventUserRejectedException;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventWrongUpdateException;
import es.jocasolo.competitiveeventsapi.exceptions.image.ImageUploadException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserNotValidException;
import es.jocasolo.competitiveeventsapi.mappers.EventMapper;
import es.jocasolo.competitiveeventsapi.model.Event;
import es.jocasolo.competitiveeventsapi.model.EventUser;
import es.jocasolo.competitiveeventsapi.model.Punishment;
import es.jocasolo.competitiveeventsapi.model.Reward;
import es.jocasolo.competitiveeventsapi.model.Score;
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
	private ScoreDAO scoreDao;
	
	@Mock
	private RewardDAO rewardDao;
	
	@Mock
	private UserDAO userDao;
	
	@Mock
	private PunishmentDAO punishmentDao;
	
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
	
	private static final String EVENT1 = "ABC";
	private static final String EVENT2 = "DEF";
	private static final String USER1 = "test1";
	private static final String USER2 = "test2";
	private static final String DONT_EXISTS = "don't exists";
	
	private User user = new User();
	private User user2 = new User();
	private Event event = new Event();
	private EventUser eventUser = new EventUser();
	private Event createdEvent = new Event();
	private EventDTO eventDto = new EventDTO();
	private EventDetailDTO eventDetailDto = new EventDetailDTO();
	private EventPutDTO eventPutDto = new EventPutDTO();
	private EventPostDTO eventPostDto = new EventPostDTO();
	private EventUserDTO eventUserDto = new EventUserDTO();
	
	@BeforeEach
	void init() {
		
		MockitoAnnotations.openMocks(this);
		
		user.setId(USER1);
		user2.setId(USER2);
		
		event.setId(EVENT1);
		event.setStatus(EventStatusType.ACTIVE);
		event.setScoreType(ScoreValueType.NUMERIC);
		event.setSortScore(ScoreSortType.ASC);
		initEventScores();
		
		eventUser.setPrivilege(EventUserPrivilegeType.OWNER);
		eventUser.setEvent(event);
		eventUser.setUser(user);
		
		eventDto.setId(EVENT2);
		eventDetailDto.setId(EVENT1);
		eventPutDto.setId(EVENT1);
		eventPostDto.setTitle("Created event");
		eventUserDto.setEventId(EVENT1);
		eventUserDto.setUserId(USER1);
		
		createdEvent.setId(EVENT1+1);
		
		Mockito.when(userDao.findOne(USER1)).thenReturn(user);
		
		Mockito.when(eventDao.findOne(EVENT1)).thenReturn(event);
		Mockito.when(eventDao.save(event)).thenReturn(event);
		Mockito.when(eventDao.save(createdEvent)).thenReturn(createdEvent);
		
		Mockito.when(commonService.transform(eventPostDto, Event.class)).thenReturn(createdEvent);
		Mockito.when(commonService.transform(createdEvent, EventDTO.class)).thenReturn(eventDto);
		Mockito.when(commonService.transform(event, EventDTO.class)).thenReturn(eventDto);
		Mockito.when(commonService.transform(eventUser, EventUserDTO.class)).thenReturn(eventUserDto);
		
		Mockito.when(eventMapper.mapDetail(event)).thenReturn(eventDetailDto);
		Mockito.when(authenticationFacade.getUser()).thenReturn(user);
		
		Mockito.when(eventUserDao.save(Mockito.any())).thenReturn(eventUser);
		Mockito.when(eventUserDao.findOne(event, user)).thenReturn(eventUser);
		Mockito.when(eventUserDao.findOneByIds(Mockito.any(), Mockito.any())).thenReturn(eventUser);
		
		Mockito.when(userDao.findOneByPhone("123456789")).thenReturn(user);
		Mockito.when(userDao.findOneByEmail("test@test.com")).thenReturn(user);
		
	}
	
	@Test
	void testFindOne() throws EventNotFoundException {
		EventDetailDTO e = eventService.findOne(EVENT1);
		assertNotNull(e);
		assertEquals(EVENT1, e.getId());
		
		assertThrows(EventNotFoundException.class, () -> {
			eventService.findOne(DONT_EXISTS);
	    });
	}
	
	@Test
	void testUpdate() throws EventWrongUpdateException, EventInvalidStatusException, EventNotFoundException {
		
		EventPutDTO dto = new EventPutDTO();
		dto.setId(EVENT1);
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
		dto.setApprovalNeeded(false);
		
		eventService.update(EVENT1, dto);
		
		assertEquals("New title", event.getTitle());
		assertEquals("New description", event.getDescription());
		assertEquals("EventPostDTO [title=" + dto.getTitle() + "]", dto.toString());
		
		assertThrows(EventNotFoundException.class, () -> {
			eventService.update(DONT_EXISTS, dto);
	    });
		
	}
	
	@Test
	void testDelete() throws EventNotFoundException, UserNotValidException{
		eventService.delete(EVENT1);
		assertEquals(EventStatusType.DELETED, event.getStatus());
		
		assertThrows(EventNotFoundException.class, () -> {
			eventService.delete(DONT_EXISTS);
		});
		
		eventUser.setPrivilege(EventUserPrivilegeType.USER);
		assertThrows(UserNotValidException.class, () -> {
			eventService.delete(EVENT1);
		});
	}
	
	@Test
	void testCreate() {
		EventDTO eventDto = eventService.create(eventPostDto);
		assertEquals(EVENT2, eventDto.getId());
	}
	
	
	@Test
	void testUpdateImage()throws EventNotFoundException, UserNotValidException, ImageUploadException {
		
		ImageDTO image = new ImageDTO();
		image.setId(1);
		image.setName("image");
		eventDto.setImage(image);
		MultipartFile file = Mockito.mock(MultipartFile.class);
		
		EventDTO result = eventService.updateImage(EVENT1, file);
		
		assertNotNull(result.getImage());
		assertEquals(1, result.getImage().getId());
		assertEquals("image", result.getImage().getName());
		
		assertThrows(EventNotFoundException.class, () -> {
			eventService.updateImage(DONT_EXISTS, file);
		});
		
		eventUser.setPrivilege(EventUserPrivilegeType.USER);
		assertThrows(UserNotValidException.class, () -> {
			eventService.updateImage(EVENT1, file);
		});
	}
	
	@Test
	void testSearch() throws UserNotValidException {
		
		@SuppressWarnings("unchecked")
		Page<Event> page = Mockito.mock(Page.class);
		
		Mockito.when(eventDao.search(EVENT1, null, null, null, null, null)).thenReturn(page);
		Mockito.when(eventDao.searchByUser("title", "ACADEMIC", "ACTIVE", "ACCEPTED", "PRIVATE", user, null)).thenReturn(page);
		Mockito.when(page.getContent()).thenReturn(List.of(event));
		Mockito.when(eventMapper.map(page.getContent())).thenReturn(List.of(eventDto));
		
		EventPageDTO result = eventService.search(EVENT1, null, null, null, null, null, null, null);
		assertNotNull(result.getEvents());
		assertEquals(1, result.getEvents().size());
		
		result = eventService.search(EVENT1, "title", EventType.ACADEMIC, EventStatusType.ACTIVE, 
				EventUserStatusType.ACCEPTED, EventInscriptionType.PRIVATE, USER1, null);
		assertNotNull(result.getEvents());
		assertEquals(1, result.getEvents().size());
		
	}
	
	@Test
	void testInit() throws EventNotFoundException, UserNotValidException, EventInvalidStatusException {
		event.setStatus(EventStatusType.NOT_ACTIVE);
		eventService.init(EVENT1);
		
		assertEquals(EventStatusType.ACTIVE, event.getStatus());
		
		assertThrows(EventInvalidStatusException.class, () -> {
			eventService.init(EVENT1);
		});
		
		eventUser.setPrivilege(EventUserPrivilegeType.USER);
		assertThrows(UserNotValidException.class, () -> {
			eventService.init(EVENT1);
		});
		
		Mockito.when(eventUserDao.findOneByIds(DONT_EXISTS, user.getId())).thenReturn(null);
		assertThrows(EventNotFoundException.class, () -> {
			eventService.init(DONT_EXISTS);
		});
		
	}
	
	@Test
	void testFinish() throws EventNotFoundException, UserNotValidException, EventInvalidStatusException {
		eventService.finish(EVENT1);
		
		assertEquals(EventStatusType.FINISHED, event.getStatus());
		
		assertThrows(EventInvalidStatusException.class, () -> {
			eventService.finish(EVENT1);
		});
		
		eventUser.setPrivilege(EventUserPrivilegeType.USER);
		assertThrows(UserNotValidException.class, () -> {
			eventService.finish(EVENT1);
		});
		
		Mockito.when(eventUserDao.findOneByIds(DONT_EXISTS, user.getId())).thenReturn(null);
		assertThrows(EventNotFoundException.class, () -> {
			eventService.finish(DONT_EXISTS);
		});
		
	}
	
	@Test
	void testAsignRewards() throws EventNotFoundException, UserNotValidException, EventInvalidStatusException {
		
		eventService.finish(EVENT1);
		
		Set<Reward> rewards = event.getRewards();
		Set<Punishment> punishments = event.getPunishments();
		
		assertEquals(1, rewards.size());
		assertEquals(1, punishments.size());
		assertEquals(USER2, rewards.iterator().next().getWinner().getId());
		assertEquals(USER1, punishments.iterator().next().getLooser().getId());
		
		event.setStatus(EventStatusType.ACTIVE);
		event.setSortScore(ScoreSortType.DESC);
		
		eventService.finish(EVENT1);
		
	}
	
	@Test
	void textFindEventAndUser() throws UserNotFoundException, EventNotFoundException {
		
		Mockito.when(eventUserDao.findOneAllStatus(event, user)).thenReturn(eventUser);
		EventUserDTO eu = eventService.findEventAndUser(EVENT1, USER1);
		
		assertEquals(USER1, eu.getUserId());
		assertEquals(EVENT1, eu.getEventId());
		
		assertThrows(EventNotFoundException.class, () -> {
			eventService.findEventAndUser(DONT_EXISTS, USER1);
		});
		
		assertThrows(UserNotFoundException.class, () -> {
			eventService.findEventAndUser(EVENT1, DONT_EXISTS);
		});
		
		Mockito.when(eventUserDao.findOneAllStatus(event, user)).thenReturn(null);
		assertThrows(EventNotFoundException.class, () -> {
			eventService.findEventAndUser(EVENT1, USER1);
		});
		
	}
	
	@Test
	void testAddUser() throws EventWrongUpdateException, UserNotValidException, UserNotFoundException, EventUserRejectedException, EventNotFoundException, EventInvalidStatusException, EventUserAcceptedException, EventNotAvailablePlacesException {
		
		EventUserPostDTO dto = new EventUserPostDTO();
		dto.setUsername(USER1);
		
		// Search by user ID
		EventUserDTO result = eventService.addUser(EVENT1, dto);
		
		assertEquals(EVENT1, result.getEventId());
		assertEquals(USER1, result.getUserId());
		assertEquals(EventUserStatusType.ACCEPTED, result.getStatus());
		assertEquals(EventUserPrivilegeType.USER, result.getPrivilege());
	}
	
	@Test
	void testAddUserByPhone() throws EventWrongUpdateException, UserNotValidException, UserNotFoundException, EventUserRejectedException, EventNotFoundException, EventInvalidStatusException, EventUserAcceptedException, EventNotAvailablePlacesException {
	
		// Search by user phone
		EventUserPostDTO dto = new EventUserPostDTO();
		dto.setPhone("123456789");
		EventUserDTO result = eventService.addUser(EVENT1, dto);
		
		assertEquals(EVENT1, result.getEventId());
	}
	
	@Test
	void testAddUserMaxPlaces() throws EventWrongUpdateException, UserNotValidException, UserNotFoundException, EventUserRejectedException, EventNotFoundException, EventInvalidStatusException, EventUserAcceptedException, EventNotAvailablePlacesException {
		
		EventUserPostDTO dto = new EventUserPostDTO();
		
		// Max places
		event.setMaxPlaces(1);
		eventDetailDto.setNumParticipants(1);
		dto.setUsername(USER1);
		assertThrows(EventNotAvailablePlacesException.class, () -> {
			eventService.addUser(EVENT1, dto);
		});
	}
	
	@Test
	void testAddUserReject() throws EventWrongUpdateException, UserNotValidException, UserNotFoundException, EventUserRejectedException, EventNotFoundException, EventInvalidStatusException, EventUserAcceptedException, EventNotAvailablePlacesException {
		
		EventUserPostDTO dto = new EventUserPostDTO();
		
		dto.setUsername(USER1);
		dto.setReject(true);
		eventUser.setStatus(EventUserStatusType.WAITING_APPROVAL);
		EventUserDTO result = eventService.addUser(EVENT1, dto);
		
		assertEquals(EventUserStatusType.REJECTED, result.getStatus());
		
		eventUser.setStatus(EventUserStatusType.DELETED);
		assertThrows(EventWrongUpdateException.class, () -> {
			eventService.addUser(EVENT1, dto);
		});
	}
	
	@Test
	void testAddUserToPublic() throws EventWrongUpdateException, UserNotValidException, UserNotFoundException, EventUserRejectedException, EventNotFoundException, EventInvalidStatusException, EventUserAcceptedException, EventNotAvailablePlacesException {
		EventUserPostDTO dto = new EventUserPostDTO();
		dto.setUsername(USER1);
		eventUser.setStatus(EventUserStatusType.WAITING_APPROVAL);
		event.setInscription(EventInscriptionType.PRIVATE);
		Mockito.when(eventUserDao.findOneAllStatus(event, user)).thenReturn(eventUser);
		EventUserDTO result = eventService.addUser(EVENT1, dto);
		
		assertNotNull(result);
		assertEquals(EVENT1, result.getEventId());
		assertEquals(USER1, result.getUserId());
		assertEquals(EventUserStatusType.INVITED, result.getStatus());
		
		event.setInscription(EventInscriptionType.PUBLIC);
		result = eventService.addUser(EVENT1, dto);
	}
		
	@Test
	void testAddUserExceptions() throws EventWrongUpdateException, UserNotValidException, UserNotFoundException, EventUserRejectedException, EventNotFoundException, EventInvalidStatusException, EventUserAcceptedException, EventNotAvailablePlacesException {
	
		EventUserPostDTO dto = new EventUserPostDTO();
		
		dto.setUsername(DONT_EXISTS);
		dto.setEmail(DONT_EXISTS);
		dto.setPhone(DONT_EXISTS);
		assertThrows(UserNotFoundException.class, () -> {
			eventService.addUser(EVENT1, dto);
		});
		
		dto.setUsername(USER1);
		assertThrows(EventNotFoundException.class, () -> {
			eventService.addUser(DONT_EXISTS, dto);
		});
		
		event.setStatus(EventStatusType.DELETED);
		assertThrows(EventInvalidStatusException.class, () -> {
			eventService.addUser(EVENT1, dto);
		});
		
	}
	
	@Test
	void testRemoveUser() throws UserNotFoundException, EventWrongUpdateException {
		
		EventUserPostDTO dto = new EventUserPostDTO();
		dto.setUsername(USER1);
		eventService.removeUser(EVENT1, dto);
		
		assertEquals(EventUserStatusType.DELETED, eventUser.getStatus());
		assertThrows(EventWrongUpdateException.class, () -> {
			eventService.removeUser(DONT_EXISTS, dto);
		});
		
		Mockito.when(eventUserDao.findOne(event, user)).thenReturn(null);
		assertThrows(EventWrongUpdateException.class, () -> {
			eventService.removeUser(EVENT1, dto);
		});
		
		dto.setUsername(DONT_EXISTS);
		assertThrows(UserNotFoundException.class, () -> {
			eventService.removeUser(EVENT1, dto);
		});
		
		Mockito.when(eventUserDao.findOne(event, user)).thenReturn(eventUser);
		Mockito.when(authenticationFacade.getUser()).thenReturn(user2);
		eventUser.setPrivilege(EventUserPrivilegeType.OWNER);
		Mockito.when(eventUserDao.findOne(event, user2)).thenReturn(eventUser);
		dto.setUsername(USER1);
		eventService.removeUser(EVENT1, dto);
		
		eventUser.setPrivilege(EventUserPrivilegeType.USER);
		assertThrows(EventWrongUpdateException.class, () -> {
			eventService.removeUser(EVENT1, dto);
		});
		
	}
	
	@Test
	void testUpdateUser() throws UserNotFoundException, EventWrongUpdateException {
		EventUserPutDTO dto = new EventUserPutDTO();
		dto.setPrivilege(EventUserPrivilegeType.OWNER);
		dto.setStatus(EventUserStatusType.INVITED);
		dto.setUsername(USER1);
		eventService.updateUser(EVENT1, dto);
		assertNotNull(dto);
		
		assertThrows(EventWrongUpdateException.class, () -> {
			eventService.updateUser(DONT_EXISTS, dto);
		});
		
	}
	
	// INIT FIELDS
	private void initEventScores() {
		
		// Rewards
		Set<Reward> rewards = new HashSet<>();
		Reward r = new Reward();
		r.setRequiredPosition(1);
		r.setId(1);
		r.setTitle("Reward title");
		rewards.add(r);
		event.setRewards(rewards);
		
		// Punishments
		Set<Punishment> punishments = new HashSet<>();
		Punishment p = new Punishment();
		p.setRequiredPosition(1);
		p.setId(1);
		p.setTitle("Punishment title");
		punishments.add(p);
		event.setPunishments(punishments);
		
		// Scores
		List<Score> scores = new ArrayList<>();
		List<Score> scoresReverse = new ArrayList<>();
		
		Score s1 = new Score();
		s1.setValue("5");
		s1.setUser(user);
		Score s2 = new Score();
		s2.setValue("10");
		s2.setUser(user2);
		
		scores.add(s1);
		scores.add(s2);
		scoresReverse.add(s2);
		scoresReverse.add(s1);
		
		event.setScores(scores);
		
		Mockito.when(scoreDao.findAllSortedAsc(Mockito.any())).thenReturn(scores);
		Mockito.when(scoreDao.findAllSortedDesc(Mockito.any())).thenReturn(scoresReverse);
		Mockito.when(rewardDao.save(Mockito.any())).thenReturn(r);
		Mockito.when(punishmentDao.save(Mockito.any())).thenReturn(p);
		
	}
}
