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

import es.jocasolo.competitiveeventsapi.dto.comment.CommentDTO;
import es.jocasolo.competitiveeventsapi.dto.comment.CommentPostDTO;
import es.jocasolo.competitiveeventsapi.dto.comment.CommentPutDTO;
import es.jocasolo.competitiveeventsapi.exceptions.comment.CommentNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserNotValidException;
import es.jocasolo.competitiveeventsapi.model.Comment;
import es.jocasolo.competitiveeventsapi.model.Event;
import es.jocasolo.competitiveeventsapi.model.User;
import es.jocasolo.competitiveeventsapi.service.CommentService;
import es.jocasolo.competitiveeventsapi.service.CommonService;

@RunWith(MockitoJUnitRunner.class)
class TestCommentController {
	
	@InjectMocks
	private CommentController commentController = new CommentController();
	
	@Mock
	private CommentService commentService;
	
	@Mock
	private CommonService commonService;
	
	private Comment comment = new Comment();
	private CommentDTO commentDto = new CommentDTO();
	
	private static final Integer ID = 1;
			
	
	@BeforeEach
	void init() throws CommentNotFoundException {
		
		MockitoAnnotations.openMocks(this);
		
		Event event = new Event();
		event.setId("event");
		User user = new User();
		user.setId("user");
		
		comment.setId(ID);
		comment.setText("text");
		comment.setEvent(event);
		comment.setUser(user);
		commentDto.setId(comment.getId());
		commentDto.setText(comment.getText());
		
		Mockito.when(commonService.transform(comment, CommentDTO.class)).thenReturn(commentDto);
		
	}
	
	@Test
	void testFindOne() throws CommentNotFoundException {
		Mockito.when(commentService.findOne(ID)).thenReturn(comment);
		CommentDTO result = commentController.findOne(ID);
		assertNotNull(result);
		assertEquals(ID, result.getId());
		assertEquals("text", result.getText());
	}
	
	@Test
	void testCreate() throws EventNotFoundException, UserNotValidException {
		CommentPostDTO dto = new CommentPostDTO();
		Mockito.when(commentService.create(dto)).thenReturn(commentDto);
		
		CommentDTO result = commentController.create(dto);
		assertNotNull(result);
		assertEquals(ID, result.getId());
		assertEquals("text", result.getText());
	}
	
	@Test
	void testUpdate() throws EventNotFoundException, UserNotValidException, CommentNotFoundException {
		CommentPutDTO dto = new CommentPutDTO();
		commentController.update(ID, dto);
	}
	
	@Test
	void testDelete() throws EventNotFoundException, UserNotValidException, CommentNotFoundException {
		commentController.delete(ID);
	}
	
	
}
