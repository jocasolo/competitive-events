package es.jocasolo.competitiveeventsapi.mappers;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import es.jocasolo.competitiveeventsapi.dto.eventuser.EventUserDTO;
import es.jocasolo.competitiveeventsapi.dto.image.ImageDTO;
import es.jocasolo.competitiveeventsapi.dto.user.UserLiteWithEventDTO;
import es.jocasolo.competitiveeventsapi.enums.eventuser.EventUserStatusType;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserNotFoundException;
import es.jocasolo.competitiveeventsapi.model.Event;
import es.jocasolo.competitiveeventsapi.model.Image;
import es.jocasolo.competitiveeventsapi.model.User;
import es.jocasolo.competitiveeventsapi.service.CommonService;
import es.jocasolo.competitiveeventsapi.service.EventService;

@RunWith(MockitoJUnitRunner.class)
class TestUserMapper {
	
	@InjectMocks
	private UserMapper userMapper = new UserMapperImpl();
	
	@Mock
	private CommonService commonService;
	
	@Mock
	private EventService eventService;
	
	private User user = new User();
	Event event = new Event();
	
	@BeforeEach
	void init() throws UserNotFoundException, EventNotFoundException {
		
		MockitoAnnotations.openMocks(this);
		
		// Image
		Image avatar = new Image();
		avatar.setId(1);
		ImageDTO avatarDto = new ImageDTO();
		avatarDto.setId(1);
		
		// User
		user.setId("user1");
		user.setAvatar(avatar);
		
		// Event
		event.setId("event1");
		EventUserDTO eu = new EventUserDTO();
		eu.setEventId(event.getId());
		eu.setUserId(user.getId());
		eu.setStatus(EventUserStatusType.ACCEPTED);
		
		Mockito.when(commonService.transform(user.getAvatar(), ImageDTO.class)).thenReturn(avatarDto);
		Mockito.when(eventService.findEventAndUser(event.getId(), user.getId())).thenReturn(eu);
	}
	
	@Test
	void testMap() throws UserNotFoundException, EventNotFoundException {
		UserLiteWithEventDTO result = userMapper.map(user, event);
		assertNotNull(result);
		assertEquals("user1", result.getId());
		assertNotNull(result.getAvatar());
		assertEquals(1, result.getAvatar().getId());
		assertEquals(EventUserStatusType.ACCEPTED, result.getStatus());
	}
	
	@Test
	void testMapList() {
		Set<UserLiteWithEventDTO> result = userMapper.map(Set.of(user), event);
		assertNotNull(result);
		assertEquals(1, result.size());
	}
	
	
	
}
