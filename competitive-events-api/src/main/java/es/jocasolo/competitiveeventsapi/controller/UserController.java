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
import es.jocasolo.competitiveeventsapi.exceptions.user.UserIdentifierExistsException;
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
	
	@GetMapping(value = "/{identifier}")
	@ApiOperation(value = "Search for an user based on its identifier.")
	public UserDTO findOne(@PathVariable("identifier") String identifier) throws UserNotFoundException {
		log.debug("Looking for the user with identifier: {}", identifier);
		return commonService.transform(userService.findOne(identifier), UserDTO.class);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Creates a new user.")
	public UserDTO create(@Valid @RequestBody UserPostDTO user) throws UserEmailExistsException, UserIdentifierExistsException {
		log.debug("Creating the user: {} ", user);
		return userService.create(user);
	}
	
	@PutMapping(value = "/{identifier}")
	@ApiOperation(value = "Updates an user by identifier.")
	public void update(
			@PathVariable("identifier") String identifier, 
			@Valid @RequestBody UserPutDTO userDTO)
			throws UserWrongUpdateException, UserInvalidStatusException, UserEmailExistsException, UserIdentifierExistsException, UserWrongPasswordException,
			UserNotFoundException {
		log.debug("Updating user with id: {}", identifier);
		userService.update(identifier, userDTO);
	}
	
	@PutMapping(value = "/{identifier}/password")
	@ApiOperation(value = "Updates an user password by identifier.")
	public void updatePassword(
			@PathVariable("identifier") String identifier, 
			@Valid @RequestBody UserPasswordDTO userDTO)
			throws UserWrongUpdateException, UserWrongPasswordException,
			UserNotFoundException {
		log.debug("Updating user password with id: {}", identifier);
		userService.updatePassword(identifier, userDTO);
	}

	@DeleteMapping(value = "/{identifier}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Delete an user by identifier.")
	public void delete(@PathVariable("identifier") String identifier) throws UserNotFoundException {
		log.debug("Deleting user with identifier: {} ", identifier);
		userService.delete(identifier);
	}
	
}
