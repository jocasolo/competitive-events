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

import es.jocasolo.competitiveeventsapi.dto.user.UserDTO;
import es.jocasolo.competitiveeventsapi.dto.user.UserPasswordDTO;
import es.jocasolo.competitiveeventsapi.dto.user.UserPostDTO;
import es.jocasolo.competitiveeventsapi.dto.user.UserPutDTO;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserEmailExistsException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserUsenameExistsException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserInvalidStatusException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserNotFoundException;
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
	public UserDTO findOne(@PathVariable("id") String id) throws UserNotFoundException {
		log.debug("Looking for the user with id: {}", id);
		return commonService.transform(userService.findOne(id), UserDTO.class);
	}
	
	@PostMapping(produces = "application/json;charset=utf8")
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Creates a new user.")
	public UserDTO create(@Valid @RequestBody UserPostDTO user) throws UserEmailExistsException, UserUsenameExistsException {
		log.debug("Creating the user: {} ", user);
		return userService.create(user);
	}
	
	@PutMapping(value = "/{id}", produces = "application/json;charset=utf8")
	@ApiOperation(value = "Updates an user by id.")
	public void update(
			@PathVariable("id") String id, 
			@Valid @RequestBody UserPutDTO userDTO)
			throws UserWrongUpdateException, UserInvalidStatusException, UserEmailExistsException, UserUsenameExistsException, UserWrongPasswordException,
			UserNotFoundException {
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
	public void delete(@PathVariable("id") String id) throws UserNotFoundException {
		log.debug("Deleting user with id: {} ", id);
		userService.delete(id);
	}
	
}
