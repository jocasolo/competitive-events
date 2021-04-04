package es.jocasolo.competitiveeventsapi.service;

import org.springframework.web.multipart.MultipartFile;

import es.jocasolo.competitiveeventsapi.dto.reward.RewardDTO;
import es.jocasolo.competitiveeventsapi.dto.reward.RewardPostDTO;
import es.jocasolo.competitiveeventsapi.dto.reward.RewardPutDTO;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.image.ImageUploadException;
import es.jocasolo.competitiveeventsapi.exceptions.reward.RewardNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.reward.RewardWrongUpdateException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserNotValidException;
import es.jocasolo.competitiveeventsapi.model.Reward;

public interface RewardService {

	/**
	 * @param id
	 * @return
	 * @throws RewardNotFoundException
	 */
	Reward findOne(Integer id) throws RewardNotFoundException;

	/**
	 * @param userDto
	 * @return
	 * @throws EventNotFoundException 
	 * @throws UserNotValidException 
	 */
	RewardDTO create(RewardPostDTO rewardDto) throws EventNotFoundException, UserNotValidException;

	/**
	 * @param id
	 * @param rewardDto
	 * @throws RewardNotFoundException 
	 * @throws UserNotValidException 
	 * @throws RewardWrongUpdateException 
	 */
	void update(Integer id, RewardPutDTO rewardDto) throws RewardNotFoundException, UserNotValidException;

	/**
	 * @param id
	 * @throws EventNotFoundException 
	 * @throws UserNotValidException 
	 * @throws RewardNotFoundException 
	 */
	void delete(Integer id) throws EventNotFoundException, UserNotValidException, RewardNotFoundException;

	/**
	 * @param id
	 * @param file
	 * @return
	 * @throws ImageUploadException 
	 * @throws RewardNotFoundException 
	 * @throws UserNotValidException 
	 */
	RewardDTO updateImage(Integer id, MultipartFile file) throws ImageUploadException, RewardNotFoundException, UserNotValidException;
	
}
