package es.jocasolo.competitiveeventsapi.service;

import org.springframework.data.domain.PageRequest;
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
import es.jocasolo.competitiveeventsapi.exceptions.score.ScoreWrongUpdateException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserNotValidException;
import es.jocasolo.competitiveeventsapi.model.Score;

public interface ScoreService {

	/**
	 * @param id
	 * @return
	 * @throws ScoreNotFoundException
	 */
	Score findOne(Integer id) throws ScoreNotFoundException;

	/**
	 * @param userDto
	 * @return
	 * @throws EventNotFoundException 
	 * @throws UserNotValidException 
	 * @throws EventInvalidStatusException 
	 * @throws ScoreWrongTypeException 
	 */
	ScoreDTO create(ScorePostDTO scoreDto) throws EventNotFoundException, UserNotValidException, EventInvalidStatusException, ScoreWrongTypeException;

	/**
	 * @param id
	 * @param scoreDto
	 * @throws ScoreNotFoundException 
	 * @throws UserNotValidException 
	 * @throws EventNotFoundException 
	 * @throws EventInvalidStatusException 
	 * @throws ScoreWrongUpdateException 
	 */
	void update(Integer id, ScorePutDTO scoreDto) throws ScoreNotFoundException, UserNotValidException, EventNotFoundException, EventInvalidStatusException;

	/**
	 * @param id
	 * @throws EventNotFoundException 
	 * @throws UserNotValidException 
	 * @throws ScoreNotFoundException 
	 * @throws EventInvalidStatusException 
	 */
	void delete(Integer id) throws EventNotFoundException, UserNotValidException, ScoreNotFoundException, EventInvalidStatusException;

	/**
	 * @param id
	 * @param file
	 * @return
	 * @throws ImageUploadException 
	 * @throws ScoreNotFoundException 
	 * @throws UserNotValidException 
	 * @throws EventInvalidStatusException 
	 */
	ScoreDTO updateImage(Integer id, MultipartFile file) throws ImageUploadException, ScoreNotFoundException, UserNotValidException, EventInvalidStatusException;

	/**
	 * @param eventId
	 * @param of
	 * @return
	 * @throws EventNotFoundException 
	 */
	ScorePageDTO search(String eventId, PageRequest page) throws EventNotFoundException;
	
}
