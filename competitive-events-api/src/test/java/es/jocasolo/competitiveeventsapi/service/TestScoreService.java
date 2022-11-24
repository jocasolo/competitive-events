package es.jocasolo.competitiveeventsapi.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import es.jocasolo.competitiveeventsapi.dao.EventUserDAO;
import es.jocasolo.competitiveeventsapi.dao.ScoreDAO;
import es.jocasolo.competitiveeventsapi.dto.score.ScoreDTO;
import es.jocasolo.competitiveeventsapi.dto.score.ScorePageDTO;
import es.jocasolo.competitiveeventsapi.dto.score.ScorePostDTO;
import es.jocasolo.competitiveeventsapi.dto.score.ScorePutDTO;
import es.jocasolo.competitiveeventsapi.dto.user.UserLiteDTO;
import es.jocasolo.competitiveeventsapi.enums.ImageType;
import es.jocasolo.competitiveeventsapi.enums.eventuser.EventUserPrivilegeType;
import es.jocasolo.competitiveeventsapi.enums.score.ScoreSortType;
import es.jocasolo.competitiveeventsapi.enums.score.ScoreStatusType;
import es.jocasolo.competitiveeventsapi.enums.score.ScoreValueType;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventInvalidStatusException;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.image.ImageUploadException;
import es.jocasolo.competitiveeventsapi.exceptions.score.ScoreNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.score.ScoreWrongTypeException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserNotValidException;
import es.jocasolo.competitiveeventsapi.model.Event;
import es.jocasolo.competitiveeventsapi.model.EventUser;
import es.jocasolo.competitiveeventsapi.model.Image;
import es.jocasolo.competitiveeventsapi.model.Score;
import es.jocasolo.competitiveeventsapi.model.User;
import es.jocasolo.competitiveeventsapi.utils.security.AuthenticationFacade;

@SuppressWarnings("unchecked")
@RunWith(MockitoJUnitRunner.class)
class TestScoreService {
	
	@InjectMocks
	private ScoreService scoreService = new ScoreServiceImpl();
	
	@Mock
	private ScoreDAO scoreDao;
	
	@Mock
	private EventUserDAO eventUserDao;

	@Mock
	private CommonService commonService;

	@Mock
	private ImageService imageService;
	
	@Mock
	private AuthenticationFacade authentication;
	
	private static final String EVENT1 = "event1";
	private static final String USER1 = "user1";
	
	private Score score = new Score();
	private ScoreDTO scoreDto = new ScoreDTO();
	private Event event = new Event();
	private EventUser eventUser = new EventUser();
	private User user = new User();
	private MultipartFile file;
	
	@BeforeEach
	void init() throws ImageUploadException {
		
		MockitoAnnotations.openMocks(this);
		
		score.setId(1);
		score.setValue("5");
		score.setEvent(event);
		score.setUser(user);
		score.setStatus(ScoreStatusType.VALID);
		scoreDto.setId(score.getId());
		scoreDto.setValue(score.getValue());
		scoreDto.setUser(new UserLiteDTO());
		scoreDto.setStatus(score.getStatus());
		
		event.setId(EVENT1);
		event.setScoreType(ScoreValueType.NUMERIC);
		event.setSortScore(ScoreSortType.ASC);
		user.setId(USER1);
		eventUser.setEvent(event);
		eventUser.setUser(user);
		eventUser.setPrivilege(EventUserPrivilegeType.OWNER);
		
		Image image = new Image();
		image.setId(1);
		
		file = Mockito.mock(MultipartFile.class);
		Mockito.when(scoreDao.findOne(1)).thenReturn(score);
		Mockito.when(scoreDao.save(score)).thenReturn(score);
		Mockito.when(eventUserDao.findOneByIds(EVENT1, USER1)).thenReturn(eventUser);
		Mockito.when(authentication.getUser()).thenReturn(user);
		Mockito.when(commonService.transform(score, ScoreDTO.class)).thenReturn(scoreDto);
		Mockito.when(imageService.upload(file, ImageType.PUNISHMENT)).thenReturn(image);
	}
	
	@Test
	void testFindOne() throws ScoreNotFoundException {
		Score result = scoreService.findOne(1);
		assertNotNull(result);
		assertEquals("event1", result.getEvent().getId());
		assertEquals("5", result.getValue());
		
		assertThrows(ScoreNotFoundException.class, () -> {
			scoreService.findOne(-1);
		});
	}
	
	@Test
	void testCreate() throws EventNotFoundException, UserNotValidException, EventInvalidStatusException, ScoreWrongTypeException {
		ScorePostDTO dto = new ScorePostDTO();
		dto.setEventId(EVENT1);
		dto.setValue("5");
		event.setScoreType(ScoreValueType.DECIMAL);
		scoreService.create(dto);
		assertEquals(EVENT1, dto.getEventId());
		
		EventUser eu = new EventUser();
		Mockito.when(eventUserDao.findOneByIds(EVENT1, USER1)).thenReturn(null);
		assertThrows(EventNotFoundException.class, () -> {
			scoreService.create(dto);
		});
		
		Date now = new Date();
		event.setInitDate(new Date(now.getTime()+1000));
		event.setEndDate(now);
		eu.setEvent(event);
		Mockito.when(eventUserDao.findOneByIds(EVENT1, USER1)).thenReturn(eu);
		assertThrows(EventInvalidStatusException.class, () -> {
			scoreService.create(dto);
		});
		
		event.setScoreType(ScoreValueType.TIME);
		event.setEndDate(null);
		event.setInitDate(null);
		dto.setValue("not valid score");
		assertThrows(ScoreWrongTypeException.class, () -> {
			scoreService.create(dto);
		});
		
	}
	
	@Test
	void testUpdate() throws UserNotValidException, ScoreNotFoundException, EventNotFoundException, EventInvalidStatusException {
		ScorePutDTO dto = new ScorePutDTO();
		dto.setValue("6");
		scoreService.update(1, dto);
		assertEquals("6", score.getValue());
	}
	
	@Test
	void testUpdateImage() throws ImageUploadException, ScoreNotFoundException, UserNotValidException, EventInvalidStatusException {
		ScoreDTO result = scoreService.updateImage(1, file);
		assertNotNull(result);
		
		assertThrows(ScoreNotFoundException.class, () -> {
			scoreService.updateImage(2, file);
		});
		
		Date now = new Date();
		event.setInitDate(new Date(now.getTime()+1000));
		event.setEndDate(now);
		assertThrows(EventInvalidStatusException.class, () -> {
			scoreService.updateImage(1, file);
		});
	}
	
	@Test
	void testSearch() throws EventNotFoundException {
		PageRequest request = Mockito.mock(PageRequest.class);
		Page<Score> page = Mockito.mock(Page.class);
		Mockito.when(page.getContent()).thenReturn(List.of(score));
		Mockito.when(scoreDao.search(event, request)).thenReturn(page);
		ScorePageDTO result = scoreService.search(EVENT1, request);
		assertNotNull(result);
		
		assertThrows(EventNotFoundException.class, () -> {
			scoreService.search("don't exists", request);
		});
	}
	
	@Test
	void testDelete() throws EventNotFoundException, UserNotValidException, ScoreNotFoundException, EventInvalidStatusException{
		scoreService.delete(1);
	}
	
	
}
