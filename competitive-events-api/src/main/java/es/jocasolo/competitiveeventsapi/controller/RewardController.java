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

import es.jocasolo.competitiveeventsapi.dto.reward.RewardDTO;
import es.jocasolo.competitiveeventsapi.dto.reward.RewardPostDTO;
import es.jocasolo.competitiveeventsapi.dto.reward.RewardPutDTO;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.image.ImageUploadException;
import es.jocasolo.competitiveeventsapi.exceptions.reward.RewardNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserNotValidException;
import es.jocasolo.competitiveeventsapi.service.CommonService;
import es.jocasolo.competitiveeventsapi.service.RewardService;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/rewards")
public class RewardController {
	
	private static final Logger log = LoggerFactory.getLogger(RewardController.class);
	
	@Autowired
	private RewardService rewardService;
	
	@Autowired
	private CommonService commonService;
	
	@GetMapping(value = "/{id}", produces = "application/json;charset=utf8")
	@ApiOperation(value = "Search for an reward based on its id.")
	public RewardDTO findOne(@PathVariable("id") Integer id) throws RewardNotFoundException {
		log.debug("Looking for the reward with id: {}", id);
		return commonService.transform(rewardService.findOne(id), RewardDTO.class);
	}
	
	@PostMapping(produces = "application/json;charset=utf8")
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Creates a new reward.")
	public RewardDTO create(@Valid @RequestBody RewardPostDTO rewardDto) throws EventNotFoundException, UserNotValidException {
		log.debug("Creating the reward: {} ", rewardDto);
		return rewardService.create(rewardDto);
	}
	
	@PutMapping(value = "/{id}/image", produces = "application/json;charset=utf8")
	@ApiOperation(value = "Updates an reward image.")
	public RewardDTO updateImage(
			@PathVariable("id") Integer id, 
			@RequestParam("file") MultipartFile file) throws ImageUploadException, RewardNotFoundException, UserNotValidException {
		log.debug("Updating reward image with id: {}", id);
		return rewardService.updateImage(id, file);
	}
	
	@PutMapping(value = "/{id}", produces = "application/json;charset=utf8")
	@ApiOperation(value = "Updates an reward by id.")
	public void update(
			@PathVariable("id") Integer id, 
			@Valid @RequestBody RewardPutDTO rewardDto) throws RewardNotFoundException, UserNotValidException {
		log.debug("Updating reward with id: {}", id);
		rewardService.update(id, rewardDto);
	}
	
	@DeleteMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Delete an reward by id.")
	public void delete(@PathVariable("id") Integer id) throws EventNotFoundException, UserNotValidException, RewardNotFoundException {
		log.debug("Deleting reward with id: {} ", id);
		rewardService.delete(id);
	}
	
}
