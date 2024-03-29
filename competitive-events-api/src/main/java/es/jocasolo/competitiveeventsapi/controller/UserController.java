package es.jocasolo.competitiveeventsapi.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import es.jocasolo.competitiveeventsapi.dto.user.UserCompleteDTO;
import es.jocasolo.competitiveeventsapi.dto.user.UserDTO;
import es.jocasolo.competitiveeventsapi.dto.user.UserPasswordDTO;
import es.jocasolo.competitiveeventsapi.dto.user.UserPostDTO;
import es.jocasolo.competitiveeventsapi.dto.user.UserPutDTO;
import es.jocasolo.competitiveeventsapi.enums.SearchTerm;
import es.jocasolo.competitiveeventsapi.exceptions.image.ImageNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.image.ImageUploadException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserEmailExistsException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserInvalidStatusException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserNotValidException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserPhoneExistsException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserUsenameExistsException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserWrongPasswordException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserWrongUpdateException;
import es.jocasolo.competitiveeventsapi.service.CommonService;
import es.jocasolo.competitiveeventsapi.service.UserService;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/users")
public class UserController {
	
	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CommonService commonService;
	
	@GetMapping(value = "/{id}", produces = "application/json;charset=utf8")
	@ApiOperation(value = "Search for an user based on its id.")
	public UserCompleteDTO findOne(
			@PathVariable("id") String id,
			@RequestParam(value="searchTerm", required=false, defaultValue="ID") SearchTerm searchTerm) throws UserNotFoundException {
		log.debug("Looking for the user with id: {}", id);
		return userService.findOne(id, searchTerm);
	}
	
	@RequestMapping(method = RequestMethod.HEAD, value = "/{id}", produces = "application/json;charset=utf8")
	@ApiOperation(value = "Search for an user based on its id.")
	public ResponseEntity<String> exists(
			@PathVariable("id") String id, 
			@RequestParam(value="searchTerm", required=false, defaultValue="ID") SearchTerm searchTerm) throws UserNotFoundException {
		log.debug("Looking for the user with id: {}", id);
		final UserCompleteDTO user = userService.findOne(id, searchTerm);
		if(user != null)		
			return new ResponseEntity<>(HttpStatus.OK);
		else
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping(value = "/confirm/{key}", produces = "application/json;charset=utf8")
	@ApiOperation(value = "Confirm an user registration.")
	public UserDTO confirm(@PathVariable("key") String key) throws UserNotFoundException {
		log.debug("Confirming user with key: {}", key);
		return commonService.transform(userService.confirm(key), UserDTO.class);
	}
	
	@PostMapping(produces = "application/json;charset=utf8")
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Creates a new user.")
	public UserDTO create(@Valid @RequestBody UserPostDTO user) throws UserEmailExistsException, UserUsenameExistsException, UserPhoneExistsException {
		log.debug("Creating the user: {} ", user);
		return userService.create(user);
	}
	
	@PutMapping(value = "/{id}", produces = "application/json;charset=utf8")
	@ApiOperation(value = "Updates an user by id.")
	public void update(
			@PathVariable("id") String id, 
			@Valid @RequestBody UserPutDTO userDTO)
			throws UserInvalidStatusException, UserEmailExistsException, UserUsenameExistsException, UserWrongPasswordException,
			UserNotFoundException, UserNotValidException {
		log.debug("Updating user with id: {}", id);
		userService.update(id, userDTO);
	}
	
	@PutMapping(value = "/{id}/password")
	@ApiOperation(value = "Updates an user password by id.")
	public void updatePassword(
			@PathVariable("id") String id, 
			@Valid @RequestBody UserPasswordDTO userDTO)
			throws UserWrongUpdateException, UserWrongPasswordException,
			UserNotFoundException {
		log.debug("Updating user password with id: {}", id);
		userService.updatePassword(id, userDTO);
	}

	@DeleteMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Delete an user by id.")
	public void delete(@PathVariable("id") String id) throws UserNotFoundException, UserNotValidException {
		log.debug("Deleting user with id: {} ", id);
		userService.delete(id);
	}
	
	@PutMapping(value = "/{id}/avatar", produces = "application/json;charset=utf8")
	@ApiOperation(value = "Updates an user avatar.")
	public UserDTO updateAvatar(
			@PathVariable("id") String id, 
			@RequestParam("file") MultipartFile file)
			throws UserNotFoundException, UserNotValidException, ImageUploadException {
		log.debug("Updating user avatar with id: {}", id);
		return commonService.transform(userService.updateAvatar(id, file), UserDTO.class);
	}
	
	@DeleteMapping(value = "/{id}/avatar")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Delete an user avatar.")
	public void deleteAvatar(@PathVariable("id") String id) 
			throws UserNotFoundException, UserNotValidException, ImageNotFoundException {
		log.debug("Deleting user avatar with id: {} ", id);
		userService.deleteAvatar(id);
	}
	
}
