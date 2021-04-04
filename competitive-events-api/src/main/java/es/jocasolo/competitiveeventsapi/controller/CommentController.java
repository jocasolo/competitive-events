package es.jocasolo.competitiveeventsapi.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import es.jocasolo.competitiveeventsapi.dto.comment.CommentDTO;
import es.jocasolo.competitiveeventsapi.dto.comment.CommentPostDTO;
import es.jocasolo.competitiveeventsapi.dto.comment.CommentPutDTO;
import es.jocasolo.competitiveeventsapi.exceptions.comment.CommentNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserNotValidException;
import es.jocasolo.competitiveeventsapi.service.CommentService;
import es.jocasolo.competitiveeventsapi.service.CommonService;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/comments")
public class CommentController {
	
	private static final Logger log = LoggerFactory.getLogger(CommentController.class);
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private CommonService commonService;
	
	@GetMapping(value = "/{id}", produces = "application/json;charset=utf8")
	@ApiOperation(value = "Search for an comment based on its id.")
	public CommentDTO findOne(@PathVariable("id") Integer id) throws CommentNotFoundException {
		log.debug("Looking for the comment with id: {}", id);
		return commonService.transform(commentService.findOne(id), CommentDTO.class);
	}
	
	@PostMapping(produces = "application/json;charset=utf8")
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Creates a new comment.")
	public CommentDTO create(@Valid @RequestBody CommentPostDTO commentDto) throws EventNotFoundException, UserNotValidException {
		log.debug("Creating the comment: {} ", commentDto);
		return commentService.create(commentDto);
	}
	
	@PutMapping(value = "/{id}", produces = "application/json;charset=utf8")
	@ApiOperation(value = "Updates an comment by id.")
	public void update(
			@PathVariable("id") Integer id, 
			@Valid @RequestBody CommentPutDTO commentDto) throws CommentNotFoundException, UserNotValidException {
		log.debug("Updating comment with id: {}", id);
		commentService.update(id, commentDto);
	}
	
	@DeleteMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Delete an comment by id.")
	public void delete(@PathVariable("id") Integer id) throws EventNotFoundException, UserNotValidException, CommentNotFoundException {
		log.debug("Deleting comment with id: {} ", id);
		commentService.delete(id);
	}
	
}
