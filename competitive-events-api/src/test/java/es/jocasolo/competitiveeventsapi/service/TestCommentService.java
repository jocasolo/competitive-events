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

import es.jocasolo.competitiveeventsapi.dao.CommentDAO;
import es.jocasolo.competitiveeventsapi.dao.EventUserDAO;
import es.jocasolo.competitiveeventsapi.dto.comment.CommentDTO;
import es.jocasolo.competitiveeventsapi.dto.comment.CommentPostDTO;
import es.jocasolo.competitiveeventsapi.dto.comment.CommentPutDTO;
import es.jocasolo.competitiveeventsapi.exceptions.comment.CommentNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserNotValidException;
import es.jocasolo.competitiveeventsapi.model.Comment;
import es.jocasolo.competitiveeventsapi.model.Event;
import es.jocasolo.competitiveeventsapi.model.EventUser;
import es.jocasolo.competitiveeventsapi.model.User;
import es.jocasolo.competitiveeventsapi.utils.security.AuthenticationFacade;

@RunWith(MockitoJUnitRunner.class)
class TestCommentService {
	
	@InjectMocks
	private CommentService commentService = new CommentServiceImpl();
	
	@Mock
	private CommentDAO commentDao;
	
	@Mock
	private EventUserDAO eventUserDao;

	@Mock
	private CommonService commonService;

	@Mock
	private AuthenticationFacade authentication;
	
	private static final String EVENT1 = "event1";
	private static final String USER1 = "user1";
	
	private Comment comment = new Comment();
	private CommentDTO commentDto = new CommentDTO();
	private Event event = new Event();
	private EventUser eventUser = new EventUser();
	private User user = new User();
	
	@BeforeEach
	void init() {
		
		MockitoAnnotations.openMocks(this);
		
		comment.setId(1);
		comment.setText("text");
		comment.setUser(user);
		comment.setEvent(event);
		commentDto.setId(comment.getId());
		commentDto.setText(comment.getText());
		
		event.setId(EVENT1);
		user.setId(USER1);
		eventUser.setEvent(event);
		eventUser.setUser(user);
		
		Mockito.when(commentDao.findOne(1)).thenReturn(comment);
		Mockito.when(commentDao.save(comment)).thenReturn(comment);
		Mockito.when(eventUserDao.findOneByIds(EVENT1, USER1)).thenReturn(eventUser);
		Mockito.when(authentication.getUser()).thenReturn(user);
		Mockito.when(commonService.transform(comment, CommentDTO.class)).thenReturn(commentDto);
		
	}
	
	@Test
	void testFindOne() throws CommentNotFoundException {
		Comment result = commentService.findOne(1);
		assertNotNull(result);
		assertEquals("event1", result.getEvent().getId());
		assertEquals("user1", result.getUser().getId());
		assertEquals("text", result.getText());
		
		assertThrows(CommentNotFoundException.class, () -> {
			commentService.findOne(-1);
		});
	}
	
	@Test
	void testCreate() throws EventNotFoundException, UserNotValidException {
		CommentPostDTO dto = new CommentPostDTO();
		dto.setEventId(EVENT1);
		commentService.create(dto);
		assertEquals(EVENT1, dto.getEventId());
	}
	
	@Test
	void testUpdate() throws CommentNotFoundException, UserNotValidException {
		CommentPutDTO dto = new CommentPutDTO();
		dto.setEventId(EVENT1);
		dto.setText("text updated");
		commentService.update(1, dto);
		assertEquals("text updated", comment.getText());
	}
	
	@Test
	void testDelete() throws EventNotFoundException, UserNotValidException, CommentNotFoundException {
		commentService.delete(1);
	}
}
