package es.jocasolo.competitiveeventsapi.service;

import org.springframework.security.core.userdetails.UserDetailsService;

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
import es.jocasolo.competitiveeventsapi.model.user.User;

public interface UserService extends UserDetailsService {

	/**
	 * Search for a user by id.
	 * 
	 * @param id User id
	 * @return User corresponding to the id searched.
	 * @throws UserNotFoundException
	 */
	User findOne(String id) throws UserNotFoundException;

	/**
	 * Creates a new user.
	 * 
	 * @param userDTO DTO with creation properties
	 * @return The new user.
	 */
	UserDTO create(UserPostDTO userDto) throws UserEmailExistsException, UserUsenameExistsException;

	/**
	 * Updates an user.
	 * 
	 * @param id User id
	 * @param user
	 * @throws UserWrongUpdateException
	 * @throws UserInvalidStatusException
	 * @throws UserNotFoundException
	 */
	void update(String id, UserPutDTO userDto)
			throws UserInvalidStatusException, UserEmailExistsException, UserUsenameExistsException, UserWrongPasswordException, UserWrongUpdateException, UserNotFoundException;

	/**
	 * Deletes a user by id
	 * 
	 * @param id User id
	 * @throws UserNotFoundException
	 */
	void delete(String id) throws UserNotFoundException;

	/**
	 * Update user password
	 * 
	 * @param id User id
	 * @param userDTO    User password dto
	 * @throws UserWrongUpdateException
	 * @throws UserNotFoundException
	 * @throws UserWrongPasswordException
	 */
	void updatePassword(String id, UserPasswordDTO userDTO) throws UserWrongUpdateException, UserNotFoundException, UserWrongPasswordException;

}
