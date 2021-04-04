package es.jocasolo.competitiveeventsapi.service;

import es.jocasolo.competitiveeventsapi.dto.comment.CommentDTO;
import es.jocasolo.competitiveeventsapi.dto.comment.CommentPostDTO;
import es.jocasolo.competitiveeventsapi.dto.comment.CommentPutDTO;
import es.jocasolo.competitiveeventsapi.exceptions.comment.CommentNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.comment.CommentWrongUpdateException;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserNotValidException;
import es.jocasolo.competitiveeventsapi.model.Comment;

public interface CommentService {

	/**
	 * @param id
	 * @return
	 * @throws CommentNotFoundException
	 */
	Comment findOne(Integer id) throws CommentNotFoundException;

	/**
	 * @param userDto
	 * @return
	 * @throws EventNotFoundException 
	 * @throws UserNotValidException 
	 */
	CommentDTO create(CommentPostDTO commentDto) throws EventNotFoundException, UserNotValidException;

	/**
	 * @param id
	 * @param commentDto
	 * @throws CommentNotFoundException 
	 * @throws UserNotValidException 
	 * @throws CommentWrongUpdateException 
	 */
	void update(Integer id, CommentPutDTO commentDto) throws CommentNotFoundException, UserNotValidException;

	/**
	 * @param id
	 * @throws EventNotFoundException 
	 * @throws UserNotValidException 
	 * @throws CommentNotFoundException 
	 */
	void delete(Integer id) throws EventNotFoundException, UserNotValidException, CommentNotFoundException;

}
