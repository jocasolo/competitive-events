package es.jocasolo.competitiveeventsapi.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
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
import es.jocasolo.competitiveeventsapi.service.CommonService;
import es.jocasolo.competitiveeventsapi.service.ScoreService;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/scores")
public class ScoreController {
	
	private static final Logger log = LoggerFactory.getLogger(ScoreController.class);
	
	@Autowired
	private ScoreService scoreService;
	
	@Autowired
	private CommonService commonService;
	
	@GetMapping(value = "/{id}", produces = "application/json;charset=utf8")
	@ApiOperation(value = "Search for an score based on its id.")
	public ScoreDTO findOne(@PathVariable("id") Integer id) throws ScoreNotFoundException {
		log.debug("Looking for the score with id: {}", id);
		return commonService.transform(scoreService.findOne(id), ScoreDTO.class);
	}
	
	@GetMapping(produces = "application/json;charset=utf8")
	@ApiOperation(value = "Find all scores that match your search parameters.")
	public ScorePageDTO search(
			@RequestParam(value = "eventId", required = false) String eventId,
			@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
			@RequestParam(value = "size", required = false, defaultValue = "10") Integer size) throws EventNotFoundException {
		log.debug("Looking for events");
		return scoreService.search(eventId, PageRequest.of(page, size));
	}
	
	@PostMapping(produces = "application/json;charset=utf8")
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Creates a new score.")
	public ScoreDTO create(@Valid @RequestBody ScorePostDTO scoreDto) 
			throws EventNotFoundException, UserNotValidException, EventInvalidStatusException, ScoreWrongTypeException {
		log.debug("Creating the score: {} ", scoreDto);
		return scoreService.create(scoreDto);
	}
	
	@PutMapping(value = "/{id}/image", produces = "application/json;charset=utf8")
	@ApiOperation(value = "Updates an score image.")
	public ScoreDTO updateImage(
			@PathVariable("id") Integer id, 
			@RequestParam("file") MultipartFile file) throws ImageUploadException, ScoreNotFoundException, UserNotValidException, EventInvalidStatusException {
		log.debug("Updating score image with id: {}", id);
		return commonService.transform(scoreService.updateImage(id, file), ScoreDTO.class);
	}
	
	@PutMapping(value = "/{id}", produces = "application/json;charset=utf8")
	@ApiOperation(value = "Updates an score by id.")
	public void update(
			@PathVariable("id") Integer id, 
			@Valid @RequestBody ScorePutDTO scoreDto) throws ScoreNotFoundException, UserNotValidException, EventNotFoundException, EventInvalidStatusException {
		log.debug("Updating score with id: {}", id);
		scoreService.update(id, scoreDto);
	}
	
	@DeleteMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Delete an score by id.")
	public void delete(@PathVariable("id") Integer id) throws EventNotFoundException, UserNotValidException, ScoreNotFoundException, EventInvalidStatusException {
		log.debug("Deleting score with id: {} ", id);
		scoreService.delete(id);
	}
	
}
