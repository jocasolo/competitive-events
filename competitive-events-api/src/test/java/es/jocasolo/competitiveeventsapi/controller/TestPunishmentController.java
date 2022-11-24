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

import es.jocasolo.competitiveeventsapi.dto.punishment.PunishmentDTO;
import es.jocasolo.competitiveeventsapi.dto.punishment.PunishmentPostDTO;
import es.jocasolo.competitiveeventsapi.dto.punishment.PunishmentPutDTO;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventInvalidStatusException;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.image.ImageUploadException;
import es.jocasolo.competitiveeventsapi.exceptions.punishment.PunishmentNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserNotValidException;
import es.jocasolo.competitiveeventsapi.model.Event;
import es.jocasolo.competitiveeventsapi.model.Punishment;
import es.jocasolo.competitiveeventsapi.model.User;
import es.jocasolo.competitiveeventsapi.service.CommonService;
import es.jocasolo.competitiveeventsapi.service.PunishmentService;

@RunWith(MockitoJUnitRunner.class)
class TestPunishmentController {
	
	@InjectMocks
	private PunishmentController punishmentController = new PunishmentController();
	
	@Mock
	private PunishmentService punishmentService;
	
	@Mock
	private CommonService commonService;
	
	private Punishment punishment = new Punishment();
	private PunishmentDTO punishmentDto = new PunishmentDTO();
	
	private static final Integer ID = 1;
			
	
	@BeforeEach
	void init() throws PunishmentNotFoundException {
		
		MockitoAnnotations.openMocks(this);
		
		Event event = new Event();
		event.setId("event");
		User user = new User();
		user.setId("user");
		
		punishment.setId(ID);
		punishment.setDescription("5");
		punishment.setEvent(event);
		punishmentDto.setId(punishment.getId());
		punishmentDto.setDescription(punishment.getDescription());
		
		Mockito.when(commonService.transform(punishment, PunishmentDTO.class)).thenReturn(punishmentDto);
		
	}
	
	@Test
	void testFindOne() throws PunishmentNotFoundException {
		Mockito.when(punishmentService.findOne(ID)).thenReturn(punishment);
		PunishmentDTO result = punishmentController.findOne(ID);
		assertNotNull(result);
		assertEquals(ID, result.getId());
		assertEquals("5", result.getDescription());
	}
	
	@Test
	void testCreate() throws EventNotFoundException, UserNotValidException, EventInvalidStatusException {
		PunishmentPostDTO dto = new PunishmentPostDTO();
		Mockito.when(punishmentService.create(dto)).thenReturn(punishmentDto);
		
		PunishmentDTO result = punishmentController.create(dto);
		assertNotNull(result);
		assertEquals(ID, result.getId());
		assertEquals("5", result.getDescription());
	}
	
	@Test
	void testUpdate() throws EventNotFoundException, UserNotValidException, PunishmentNotFoundException, EventInvalidStatusException {
		PunishmentPutDTO dto = new PunishmentPutDTO();
		punishmentController.update(ID, dto);
	}
	
	@Test
	void testDelete() throws EventNotFoundException, UserNotValidException, PunishmentNotFoundException, EventInvalidStatusException {
		punishmentController.delete(ID);
	}
	
	@Test
	void testUpdateImage() throws ImageUploadException, PunishmentNotFoundException, UserNotValidException, EventInvalidStatusException {
		MultipartFile file = Mockito.mock(MultipartFile.class);
		Mockito.when(punishmentService.updateImage(ID, file)).thenReturn(punishmentDto);
		
		PunishmentDTO result = punishmentController.updateImage(ID, file);
		assertNotNull(result);
		assertEquals(ID, result.getId());
	}
	
}
