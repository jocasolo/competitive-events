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
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import es.jocasolo.competitiveeventsapi.dto.score.ScoreDTO;
import es.jocasolo.competitiveeventsapi.dto.score.ScorePageDTO;
import es.jocasolo.competitiveeventsapi.dto.score.ScorePostDTO;
import es.jocasolo.competitiveeventsapi.dto.score.ScorePutDTO;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventInvalidStatusException;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.image.ImageUploadException;
import es.jocasolo.competitiveeventsapi.exceptions.score.ScoreNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.score.ScoreWrongTypeException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserNotValidException;
import es.jocasolo.competitiveeventsapi.model.Event;
import es.jocasolo.competitiveeventsapi.model.Score;
import es.jocasolo.competitiveeventsapi.model.User;
import es.jocasolo.competitiveeventsapi.service.CommonService;
import es.jocasolo.competitiveeventsapi.service.ScoreService;

@RunWith(MockitoJUnitRunner.class)
class TestScoreController {
	
	@InjectMocks
	private ScoreController scoreController = new ScoreController();
	
	@Mock
	private ScoreService scoreService;
	
	@Mock
	private CommonService commonService;
	
	private Score score = new Score();
	private ScoreDTO scoreDto = new ScoreDTO();
	
	private static final Integer ID = 1;
			
	
	@BeforeEach
	void init() throws ScoreNotFoundException {
		
		MockitoAnnotations.openMocks(this);
		
		Event event = new Event();
		event.setId("event");
		User user = new User();
		user.setId("user");
		
		score.setId(ID);
		score.setValue("5");
		score.setEvent(event);
		score.setUser(user);
		scoreDto.setId(score.getId());
		scoreDto.setValue(score.getValue());
		
		Mockito.when(commonService.transform(score, ScoreDTO.class)).thenReturn(scoreDto);
		
	}
	
	@Test
	void testFindOne() throws ScoreNotFoundException {
		Mockito.when(scoreService.findOne(ID)).thenReturn(score);
		ScoreDTO result = scoreController.findOne(ID);
		assertNotNull(result);
		assertEquals(ID, result.getId());
		assertEquals("5", result.getValue());
	}
	
	@Test
	void testCreate() throws EventNotFoundException, UserNotValidException, EventInvalidStatusException, ScoreWrongTypeException {
		ScorePostDTO dto = new ScorePostDTO();
		Mockito.when(scoreService.create(dto)).thenReturn(scoreDto);
		
		ScoreDTO result = scoreController.create(dto);
		assertNotNull(result);
		assertEquals(ID, result.getId());
		assertEquals("5", result.getValue());
	}
	
	@Test
	void testUpdate() throws EventNotFoundException, UserNotValidException, ScoreNotFoundException, EventInvalidStatusException {
		ScorePutDTO dto = new ScorePutDTO();
		scoreController.update(ID, dto);
	}
	
	@Test
	void testDelete() throws EventNotFoundException, UserNotValidException, ScoreNotFoundException, EventInvalidStatusException {
		scoreController.delete(ID);
	}
	
	@Test
	void testSearch() throws EventNotFoundException {
		ScorePageDTO page = new ScorePageDTO();
		page.setTotal(1L);
		page.setScores(List.of(scoreDto));
		Mockito.when(scoreService.search("ABC", PageRequest.of(1, 5))).thenReturn(page);
		
		ScorePageDTO result = scoreController.search("ABC", 1, 5);
		assertNotNull(result);
		assertEquals(1, result.getTotal());
		assertEquals(ID, result.getScores().get(0).getId());
	}
	
	@Test
	void testUpdateImage() throws ImageUploadException, ScoreNotFoundException, UserNotValidException, EventInvalidStatusException {
		MultipartFile file = Mockito.mock(MultipartFile.class);
		Mockito.when(scoreService.updateImage(ID, file)).thenReturn(scoreDto);
		
		ScoreDTO result = scoreController.updateImage(ID, file);
		assertNotNull(result);
		assertEquals(ID, result.getId());
	}
	
}
