package es.jocasolo.competitiveeventsapi.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.multipart.MultipartFile;

import es.jocasolo.competitiveeventsapi.dao.EventUserDAO;
import es.jocasolo.competitiveeventsapi.dao.RewardDAO;
import es.jocasolo.competitiveeventsapi.dto.reward.RewardDTO;
import es.jocasolo.competitiveeventsapi.dto.reward.RewardPostDTO;
import es.jocasolo.competitiveeventsapi.dto.reward.RewardPutDTO;
import es.jocasolo.competitiveeventsapi.enums.ImageType;
import es.jocasolo.competitiveeventsapi.enums.eventuser.EventUserPrivilegeType;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.image.ImageUploadException;
import es.jocasolo.competitiveeventsapi.exceptions.reward.RewardNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserNotValidException;
import es.jocasolo.competitiveeventsapi.model.Event;
import es.jocasolo.competitiveeventsapi.model.EventUser;
import es.jocasolo.competitiveeventsapi.model.Image;
import es.jocasolo.competitiveeventsapi.model.Reward;
import es.jocasolo.competitiveeventsapi.model.User;
import es.jocasolo.competitiveeventsapi.utils.security.AuthenticationFacade;

@RunWith(MockitoJUnitRunner.class)
class TestRewardService {
	
	@InjectMocks
	private RewardService rewardService = new RewardServiceImpl();
	
	@Mock
	private RewardDAO rewardDao;
	
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
	
	private Reward reward = new Reward();
	private RewardDTO rewardDto = new RewardDTO();
	private Event event = new Event();
	private EventUser eventUser = new EventUser();
	private User user = new User();
	private MultipartFile file;
	
	@BeforeEach
	void init() throws ImageUploadException {
		
		MockitoAnnotations.openMocks(this);
		
		reward.setId(1);
		reward.setRequiredPosition(1);
		reward.setEvent(event);
		rewardDto.setId(reward.getId());
		rewardDto.setRequiredPosition(reward.getRequiredPosition());
		
		event.setId(EVENT1);
		user.setId(USER1);
		eventUser.setEvent(event);
		eventUser.setUser(user);
		eventUser.setPrivilege(EventUserPrivilegeType.OWNER);
		
		Image image = new Image();
		image.setId(1);
		
		file = Mockito.mock(MultipartFile.class);
		Mockito.when(rewardDao.findOne(1)).thenReturn(reward);
		Mockito.when(rewardDao.save(reward)).thenReturn(reward);
		Mockito.when(eventUserDao.findOneByIds(EVENT1, USER1)).thenReturn(eventUser);
		Mockito.when(authentication.getUser()).thenReturn(user);
		Mockito.when(commonService.transform(reward, RewardDTO.class)).thenReturn(rewardDto);
		Mockito.when(imageService.upload(file, ImageType.REWARD)).thenReturn(image);
	}
	
	@Test
	void testFindOne() throws RewardNotFoundException {
		Reward result = rewardService.findOne(1);
		assertNotNull(result);
		assertEquals("event1", result.getEvent().getId());
		assertEquals(1, result.getRequiredPosition());
		
		assertThrows(RewardNotFoundException.class, () -> {
			rewardService.findOne(-1);
		});
	}
	
	@Test
	void testCreate() throws EventNotFoundException, UserNotValidException {
		RewardPostDTO dto = new RewardPostDTO();
		dto.setEventId(EVENT1);
		rewardService.create(dto);
		assertEquals(EVENT1, dto.getEventId());
	}
	
	@Test
	void testUpdate() throws UserNotValidException, RewardNotFoundException {
		RewardPutDTO dto = new RewardPutDTO();
		dto.setDescription("new description");
		rewardService.update(1, dto);
		assertEquals("new description", reward.getDescription());
	}
	
	@Test
	void testDelete() throws EventNotFoundException, UserNotValidException, RewardNotFoundException{
		rewardService.delete(1);
	}
	
	@Test
	void testUpdateImage() throws ImageUploadException, RewardNotFoundException, UserNotValidException {
		RewardDTO result = rewardService.updateImage(1, file);
		assertNotNull(result);
		assertNotNull(reward.getImage());
	}
}
