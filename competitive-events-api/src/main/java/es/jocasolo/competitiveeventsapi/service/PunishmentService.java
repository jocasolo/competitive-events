package es.jocasolo.competitiveeventsapi.service;

import org.springframework.web.multipart.MultipartFile;

import es.jocasolo.competitiveeventsapi.dto.punishment.PunishmentDTO;
import es.jocasolo.competitiveeventsapi.dto.punishment.PunishmentPostDTO;
import es.jocasolo.competitiveeventsapi.dto.punishment.PunishmentPutDTO;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.image.ImageUploadException;
import es.jocasolo.competitiveeventsapi.exceptions.punishment.PunishmentNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.punishment.PunishmentWrongUpdateException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserNotValidException;
import es.jocasolo.competitiveeventsapi.model.event.Punishment;

public interface PunishmentService {

	/**
	 * @param id
	 * @return
	 * @throws PunishmentNotFoundException
	 */
	Punishment findOne(Integer id) throws PunishmentNotFoundException;

	/**
	 * @param userDto
	 * @return
	 * @throws EventNotFoundException 
	 * @throws UserNotValidException 
	 */
	PunishmentDTO create(PunishmentPostDTO punishmentDto) throws EventNotFoundException, UserNotValidException;

	/**
	 * @param id
	 * @param punishmentDto
	 * @throws PunishmentNotFoundException 
	 * @throws UserNotValidException 
	 * @throws PunishmentWrongUpdateException 
	 */
	void update(Integer id, PunishmentPutDTO punishmentDto) throws PunishmentNotFoundException, UserNotValidException, PunishmentWrongUpdateException;

	/**
	 * @param id
	 * @throws EventNotFoundException 
	 * @throws UserNotValidException 
	 * @throws PunishmentNotFoundException 
	 */
	void delete(Integer id) throws EventNotFoundException, UserNotValidException, PunishmentNotFoundException;

	/**
	 * @param id
	 * @param file
	 * @return
	 * @throws ImageUploadException 
	 * @throws PunishmentNotFoundException 
	 * @throws UserNotValidException 
	 */
	Object updateImage(Integer id, MultipartFile file) throws ImageUploadException, PunishmentNotFoundException, UserNotValidException;
	
}
