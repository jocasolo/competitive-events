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
import es.jocasolo.competitiveeventsapi.dao.PunishmentDAO;
import es.jocasolo.competitiveeventsapi.dto.punishment.PunishmentDTO;
import es.jocasolo.competitiveeventsapi.dto.punishment.PunishmentPostDTO;
import es.jocasolo.competitiveeventsapi.dto.punishment.PunishmentPutDTO;
import es.jocasolo.competitiveeventsapi.enums.ImageType;
import es.jocasolo.competitiveeventsapi.enums.eventuser.EventUserPrivilegeType;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.image.ImageUploadException;
import es.jocasolo.competitiveeventsapi.exceptions.punishment.PunishmentNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserNotValidException;
import es.jocasolo.competitiveeventsapi.model.Event;
import es.jocasolo.competitiveeventsapi.model.EventUser;
import es.jocasolo.competitiveeventsapi.model.Image;
import es.jocasolo.competitiveeventsapi.model.Punishment;
import es.jocasolo.competitiveeventsapi.model.User;
import es.jocasolo.competitiveeventsapi.utils.security.AuthenticationFacade;

@RunWith(MockitoJUnitRunner.class)
class TestPunishmentService {
	
	@InjectMocks
	private PunishmentService punishmentService = new PunishmentServiceImpl();
	
	@Mock
	private PunishmentDAO punishmentDao;
	
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
	
	private Punishment punishment = new Punishment();
	private PunishmentDTO punishmentDto = new PunishmentDTO();
	private Event event = new Event();
	private EventUser eventUser = new EventUser();
	private User user = new User();
	private MultipartFile file;
	
	@BeforeEach
	void init() throws ImageUploadException {
		
		MockitoAnnotations.openMocks(this);
		
		punishment.setId(1);
		punishment.setRequiredPosition(1);
		punishment.setEvent(event);
		punishmentDto.setId(punishment.getId());
		punishmentDto.setRequiredPosition(punishment.getRequiredPosition());
		
		event.setId(EVENT1);
		user.setId(USER1);
		eventUser.setEvent(event);
		eventUser.setUser(user);
		eventUser.setPrivilege(EventUserPrivilegeType.OWNER);
		
		Image image = new Image();
		image.setId(1);
		
		file = Mockito.mock(MultipartFile.class);
		Mockito.when(punishmentDao.findOne(1)).thenReturn(punishment);
		Mockito.when(punishmentDao.save(punishment)).thenReturn(punishment);
		Mockito.when(eventUserDao.findOneByIds(EVENT1, USER1)).thenReturn(eventUser);
		Mockito.when(authentication.getUser()).thenReturn(user);
		Mockito.when(commonService.transform(punishment, PunishmentDTO.class)).thenReturn(punishmentDto);
		Mockito.when(imageService.upload(file, ImageType.PUNISHMENT)).thenReturn(image);
	}
	
	@Test
	void testFindOne() throws PunishmentNotFoundException {
		Punishment result = punishmentService.findOne(1);
		assertNotNull(result);
		assertEquals("event1", result.getEvent().getId());
		assertEquals(1, result.getRequiredPosition());
		
		assertThrows(PunishmentNotFoundException.class, () -> {
			punishmentService.findOne(-1);
		});
	}
	
	@Test
	void testCreate() throws EventNotFoundException, UserNotValidException {
		PunishmentPostDTO dto = new PunishmentPostDTO();
		dto.setEventId(EVENT1);
		punishmentService.create(dto);
		assertEquals(EVENT1, dto.getEventId());
	}
	
	@Test
	void testUpdate() throws UserNotValidException, PunishmentNotFoundException {
		PunishmentPutDTO dto = new PunishmentPutDTO();
		dto.setDescription("new description");
		punishmentService.update(1, dto);
		assertEquals("new description", punishment.getDescription());
	}
	
	@Test
	void testDelete() throws EventNotFoundException, UserNotValidException, PunishmentNotFoundException{
		punishmentService.delete(1);
	}
	
	@Test
	void testUpdateImage() throws ImageUploadException, PunishmentNotFoundException, UserNotValidException {
		PunishmentDTO result = punishmentService.updateImage(1, file);
		assertNotNull(result);
		assertNotNull(punishment.getImage());
	}
}
