package es.jocasolo.competitiveeventsapi.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.multipart.MultipartFile;

import es.jocasolo.competitiveeventsapi.dto.reward.RewardDTO;
import es.jocasolo.competitiveeventsapi.dto.reward.RewardPostDTO;
import es.jocasolo.competitiveeventsapi.dto.reward.RewardPutDTO;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventInvalidStatusException;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.image.ImageUploadException;
import es.jocasolo.competitiveeventsapi.exceptions.reward.RewardNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserNotValidException;
import es.jocasolo.competitiveeventsapi.model.Event;
import es.jocasolo.competitiveeventsapi.model.Reward;
import es.jocasolo.competitiveeventsapi.model.User;
import es.jocasolo.competitiveeventsapi.service.CommonService;
import es.jocasolo.competitiveeventsapi.service.RewardService;

@RunWith(MockitoJUnitRunner.class)
class TestRewardController {
	
	@InjectMocks
	private RewardController rewardController = new RewardController();
	
	@Mock
	private RewardService rewardService;
	
	@Mock
	private CommonService commonService;
	
	private Reward reward = new Reward();
	private RewardDTO rewardDto = new RewardDTO();
	
	private static final Integer ID = 1;
			
	
	@BeforeEach
	void init() throws RewardNotFoundException {
		
		MockitoAnnotations.openMocks(this);
		
		Event event = new Event();
		event.setId("event");
		User user = new User();
		user.setId("user");
		
		reward.setId(ID);
		reward.setDescription("5");
		reward.setEvent(event);
		rewardDto.setId(reward.getId());
		rewardDto.setDescription(reward.getDescription());
		
		Mockito.when(commonService.transform(reward, RewardDTO.class)).thenReturn(rewardDto);
		
	}
	
	@Test
	void testFindOne() throws RewardNotFoundException {
		Mockito.when(rewardService.findOne(ID)).thenReturn(reward);
		RewardDTO result = rewardController.findOne(ID);
		assertNotNull(result);
		assertEquals(ID, result.getId());
		assertEquals("5", result.getDescription());
	}
	
	@Test
	void testCreate() throws EventNotFoundException, UserNotValidException, EventInvalidStatusException {
		RewardPostDTO dto = new RewardPostDTO();
		Mockito.when(rewardService.create(dto)).thenReturn(rewardDto);
		
		RewardDTO result = rewardController.create(dto);
		assertNotNull(result);
		assertEquals(ID, result.getId());
		assertEquals("5", result.getDescription());
	}
	
	@Test
	void testUpdate() throws EventNotFoundException, UserNotValidException, RewardNotFoundException, EventInvalidStatusException {
		RewardPutDTO dto = new RewardPutDTO();
		rewardController.update(ID, dto);
	}
	
	@Test
	void testDelete() throws EventNotFoundException, UserNotValidException, RewardNotFoundException, EventInvalidStatusException {
		rewardController.delete(ID);
	}
	
	@Test
	void testUpdateImage() throws ImageUploadException, RewardNotFoundException, UserNotValidException, EventInvalidStatusException {
		MultipartFile file = Mockito.mock(MultipartFile.class);
		Mockito.when(rewardService.updateImage(ID, file)).thenReturn(rewardDto);
		
		RewardDTO result = rewardController.updateImage(ID, file);
		assertNotNull(result);
		assertEquals(ID, result.getId());
	}
	
}
