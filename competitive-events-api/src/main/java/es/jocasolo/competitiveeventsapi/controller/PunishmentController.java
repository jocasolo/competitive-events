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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import es.jocasolo.competitiveeventsapi.dto.punishment.PunishmentDTO;
import es.jocasolo.competitiveeventsapi.dto.punishment.PunishmentPostDTO;
import es.jocasolo.competitiveeventsapi.dto.punishment.PunishmentPutDTO;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.image.ImageUploadException;
import es.jocasolo.competitiveeventsapi.exceptions.punishment.PunishmentNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserNotValidException;
import es.jocasolo.competitiveeventsapi.service.CommonService;
import es.jocasolo.competitiveeventsapi.service.PunishmentService;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/punishments")
public class PunishmentController {
	
	private static final Logger log = LoggerFactory.getLogger(PunishmentController.class);
	
	@Autowired
	private PunishmentService punishmentService;
	
	@Autowired
	private CommonService commonService;
	
	@GetMapping(value = "/{id}", produces = "application/json;charset=utf8")
	@ApiOperation(value = "Search for an punishment based on its id.")
	public PunishmentDTO findOne(@PathVariable("id") Integer id) throws PunishmentNotFoundException {
		log.debug("Looking for the punishment with id: {}", id);
		return commonService.transform(punishmentService.findOne(id), PunishmentDTO.class);
	}
	
	@PostMapping(produces = "application/json;charset=utf8")
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Creates a new punishment.")
	public PunishmentDTO create(@Valid @RequestBody PunishmentPostDTO punishmentDto) throws EventNotFoundException, UserNotValidException {
		log.debug("Creating the punishment: {} ", punishmentDto);
		return punishmentService.create(punishmentDto);
	}
	
	@PutMapping(value = "/{id}/image", produces = "application/json;charset=utf8")
	@ApiOperation(value = "Updates an punishment image.")
	public PunishmentDTO updateImage(
			@PathVariable("id") Integer id, 
			@RequestParam("file") MultipartFile file) throws ImageUploadException, PunishmentNotFoundException, UserNotValidException {
		log.debug("Updating punishment image with id: {}", id);
		return punishmentService.updateImage(id, file);
	}
	
	@PutMapping(value = "/{id}", produces = "application/json;charset=utf8")
	@ApiOperation(value = "Updates an punishment by id.")
	public void update(
			@PathVariable("id") Integer id, 
			@Valid @RequestBody PunishmentPutDTO punishmentDto) throws PunishmentNotFoundException, UserNotValidException {
		log.debug("Updating punishment with id: {}", id);
		punishmentService.update(id, punishmentDto);
	}
	
	@DeleteMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Delete an punishment by id.")
	public void delete(@PathVariable("id") Integer id) throws EventNotFoundException, UserNotValidException, PunishmentNotFoundException {
		log.debug("Deleting punishment with id: {} ", id);
		punishmentService.delete(id);
	}
	
}
