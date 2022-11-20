package es.jocasolo.competitiveeventsapi.mappers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import es.jocasolo.competitiveeventsapi.dto.event.EventDTO;
import es.jocasolo.competitiveeventsapi.dto.event.EventDetailDTO;
import es.jocasolo.competitiveeventsapi.dto.image.ImageDTO;
import es.jocasolo.competitiveeventsapi.dto.user.UserLiteWithEventDTO;
import es.jocasolo.competitiveeventsapi.enums.eventuser.EventUserStatusType;
import es.jocasolo.competitiveeventsapi.model.Event;
import es.jocasolo.competitiveeventsapi.model.Image;
import es.jocasolo.competitiveeventsapi.model.User;
import es.jocasolo.competitiveeventsapi.service.CommonService;

@RunWith(MockitoJUnitRunner.class)
class TestEventMapper {
	
	@InjectMocks
	private EventMapper eventMapper = new EventMapperImpl();
	
	@Mock
	private CommonService commonService;
	
	@Mock
	private UserMapper userMapper;
	
	private Event event = new Event();
	
	@BeforeEach
	void init() {
		
		MockitoAnnotations.openMocks(this);
		
		// Image
		Image image = new Image();
		image.setId(1);
		ImageDTO imageDto = new ImageDTO();
		imageDto.setId(image.getId());
		
		// event
		event.setId("test1");
		event.setApprovalNeeded(false);
		event.setTitle("title");
		event.setImage(image);
		
		// Users
		User user1 = new User();
		User user2 = new User();
		user1.setId("user1");
		user2.setId("user2");
		
		UserLiteWithEventDTO ue1 = new UserLiteWithEventDTO();
		ue1.setId("user1");
		ue1.setStatus(EventUserStatusType.ACCEPTED);
		
		UserLiteWithEventDTO ue2 = new UserLiteWithEventDTO();
		ue2.setId("user2");
		ue2.setStatus(EventUserStatusType.REJECTED);
		
		event.setUsers(Set.of(user1, user2));
		
		Mockito.when(commonService.transform(image, ImageDTO.class)).thenReturn(imageDto);
		Mockito.when(userMapper.map(event.getUsers(), event)).thenReturn(Set.of(ue1, ue2));
	}
	
	@Test
	void testMap() {
		EventDTO result = eventMapper.map(event);
		
		assertNotNull(result);
		assertEquals("test1", result.getId());
		assertEquals("title", result.getTitle());
		assertFalse(result.getApprovalNeeded());
		assertNotNull(result.getImage());
		assertEquals(1, result.getImage().getId());
		
		event.setImage(null);
		result = eventMapper.map(event);
		assertNotNull(result);
		assertNull(result.getImage());
	}

	@Test
	void testMapDetail() {
		EventDetailDTO result = eventMapper.mapDetail(event);
		
		assertNotNull(result);
		assertEquals("test1", result.getId());
		assertEquals("title", result.getTitle());
		assertFalse(result.getApprovalNeeded());
		assertNotNull(result.getImage());
		assertEquals(1, result.getImage().getId());
		
		event.setImage(null);
		result = eventMapper.mapDetail(event);
		assertNotNull(result);
		assertNull(result.getImage());
		assertEquals(1, result.getNumParticipants());
		
	}
	
	@Test
	void testMapList() {
		List<EventDTO> result = eventMapper.map(List.of(event));
		assertFalse(result.isEmpty());
		assertEquals(1, result.size());
		assertEquals("title", result.get(0).getTitle());
	}
}
