package es.jocasolo.competitiveeventsapi.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import es.jocasolo.competitiveeventsapi.dao.UserDAO;
import es.jocasolo.competitiveeventsapi.dto.user.UserCompleteDTO;
import es.jocasolo.competitiveeventsapi.dto.user.UserDTO;
import es.jocasolo.competitiveeventsapi.dto.user.UserPasswordDTO;
import es.jocasolo.competitiveeventsapi.dto.user.UserPostDTO;
import es.jocasolo.competitiveeventsapi.dto.user.UserPutDTO;
import es.jocasolo.competitiveeventsapi.enums.SearchTerm;
import es.jocasolo.competitiveeventsapi.enums.eventuser.EventUserPrivilegeType;
import es.jocasolo.competitiveeventsapi.enums.score.ScoreSortType;
import es.jocasolo.competitiveeventsapi.enums.score.ScoreValueType;
import es.jocasolo.competitiveeventsapi.enums.user.UserStatusType;
import es.jocasolo.competitiveeventsapi.exceptions.image.ImageNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.image.ImageUploadException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserEmailExistsException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserInvalidStatusException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserNotValidException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserPhoneExistsException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserUsenameExistsException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserWrongPasswordException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserWrongUpdateException;
import es.jocasolo.competitiveeventsapi.model.Event;
import es.jocasolo.competitiveeventsapi.model.EventUser;
import es.jocasolo.competitiveeventsapi.model.Image;
import es.jocasolo.competitiveeventsapi.model.User;
import es.jocasolo.competitiveeventsapi.utils.security.AuthenticationFacade;

@RunWith(MockitoJUnitRunner.class)
class TestUserService {
	
	@InjectMocks
	private UserService userService = new UserServiceImpl();
	
	@Mock
	private UserDAO userDao;
	
	@Mock
	private CommonService commonService;

	@Mock
	private PasswordEncoder passwordEncoder;
	
	@Mock
	private EmailService emailService;
	
	@Mock
	private ImageService imageService;
	
	@Mock
	private AuthenticationFacade authentication;
	
	private static final String EVENT1 = "event1";
	private static final String USER1 = "user1";
	private static final String PHONE = "123456789";
	private static final String EMAIL = "test@test.com";
	private static final String PASSWORD = "Test123456";
	
	private Event event = new Event();
	private EventUser eventUser = new EventUser();
	private User user = new User();
	
	@BeforeEach
	void init() throws ImageUploadException {
		
		MockitoAnnotations.openMocks(this);
		
		user.setId(USER1);
		user.setName("name");
		user.setPassword(PASSWORD);
		user.setConfirmKey("12345");
		UserCompleteDTO userComplete = new UserCompleteDTO();
		userComplete.setId(user.getId());
		userComplete.setName(user.getName());
		UserDTO userDto = userComplete;
		
		event.setId(EVENT1);
		event.setScoreType(ScoreValueType.NUMERIC);
		event.setSortScore(ScoreSortType.ASC);
		eventUser.setEvent(event);
		eventUser.setUser(user);
		eventUser.setPrivilege(EventUserPrivilegeType.OWNER);
		
		Mockito.when(authentication.getUser()).thenReturn(user);
		Mockito.when(commonService.transform(user, UserCompleteDTO.class)).thenReturn(userComplete);
		Mockito.when(commonService.transform(user, UserDTO.class)).thenReturn(userDto);
		Mockito.when(userDao.findOneByPhone(PHONE)).thenReturn(user);
		Mockito.when(userDao.findOneByEmail(EMAIL)).thenReturn(user);
		Mockito.when(userDao.findOne(USER1)).thenReturn(user);
		Mockito.when(userDao.save(Mockito.any())).thenReturn(user);
	}
	
	@Test
	void testFindOne() throws UserNotFoundException {
		
		UserCompleteDTO result = userService.findOne(USER1, SearchTerm.ID);
		assertNotNull(result);
		assertEquals(USER1, result.getId());
		assertEquals("name", result.getName());
		
		result = userService.findOne(PHONE, SearchTerm.PHONE);
		assertNotNull(result);
		assertEquals(USER1, result.getId());
		
		result = userService.findOne(EMAIL, SearchTerm.EMAIL);
		assertNotNull(result);
		assertEquals(USER1, result.getId());
		
		assertThrows(UserNotFoundException.class, () -> {
			userService.findOne("don't exists", SearchTerm.ID);
		});
	}
	
	@Test
	void testFindOneByPhone() throws UserNotFoundException {
		UserCompleteDTO result = userService.findOneByPhone(PHONE);
		assertNotNull(result);
		assertEquals(USER1, result.getId());
		assertEquals("name", result.getName());
		
		assertThrows(UserNotFoundException.class, () -> {
			userService.findOneByPhone("don't exists");
		});
	}
	
	@Test
	void testCreate() throws UserEmailExistsException, UserUsenameExistsException, UserPhoneExistsException {
		User newUser = new User();
		newUser.setId("user2");
		newUser.setName("name2");
		UserPostDTO dto = new UserPostDTO();
		dto.setId(newUser.getId());
		dto.setName(newUser.getName());
		Mockito.when(commonService.transform(dto, User.class)).thenReturn(newUser);
		
		UserDTO result = userService.create(dto);
		assertNotNull(result);
	}
	
	@Test
	void testUpdate() throws UserInvalidStatusException, UserEmailExistsException, UserUsenameExistsException, UserWrongPasswordException, UserNotFoundException, UserNotValidException {
		UserPutDTO dto = new UserPutDTO();
		dto.setName("name2");
		
		userService.update(USER1, dto);
		assertEquals("name2", user.getName());
	}
	
	@Test
	void testUpdatePassword() throws UserWrongUpdateException, UserNotFoundException, UserWrongPasswordException {
		UserPasswordDTO dto = new UserPasswordDTO();
		dto.setId(USER1);
		dto.setPassword(PASSWORD);
		dto.setNewPassword("Test123456b");
		
		Mockito.when(passwordEncoder.matches(PASSWORD, PASSWORD)).thenReturn(true);
		Mockito.when(passwordEncoder.encode(dto.getNewPassword())).thenReturn("Test123456b");
		
		userService.updatePassword(USER1, dto);
		assertEquals("Test123456b", user.getPassword());
	}
	
	@Test
	void testDelete() throws UserNotFoundException, UserNotValidException {
		userService.delete(USER1);
		assertEquals(UserStatusType.DELETED, user.getStatus());
	}
	
	@Test
	void testLoadUserByUsername() {
		UserDetails result = userService.loadUserByUsername(USER1);
		assertNotNull(result);
		assertEquals(USER1, result.getUsername());
	}
	
	@Test
	void testConfirm() throws UserNotFoundException {
		Mockito.when(userDao.findByConfirmKey("12345")).thenReturn(List.of(user));
		UserDTO result = userService.confirm("12345");
		assertNotNull(result);
	}
	
	@Test
	void testUpdateAvatar() throws UserNotFoundException, UserNotValidException, ImageUploadException {
		MultipartFile file = Mockito.mock(MultipartFile.class);
		UserDTO result = userService.updateAvatar(USER1, file);
		assertNotNull(result);
		
		assertThrows(UserNotValidException.class, () -> {
			userService.updateAvatar("don't exists", file);
		});
	}
	
	@Test
	void testDeleteAvatar() throws UserNotFoundException, UserNotValidException, ImageNotFoundException {
		Image image = new Image();
		image.setId(1);
		image.setStorageId("storage");
		user.setAvatar(image);
		
		userService.deleteAvatar(USER1);
		assertNull(user.getAvatar());
		
		assertThrows(UserNotFoundException.class, () -> {
			userService.deleteAvatar("don't exists");
		});
	}
}
