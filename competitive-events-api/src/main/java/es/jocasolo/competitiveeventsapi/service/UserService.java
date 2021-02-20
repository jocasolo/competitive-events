package es.jocasolo.competitiveeventsapi.service;

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
import es.jocasolo.competitiveeventsapi.model.user.User;

public interface UserService {

	/**
	 * Search for a user by identifier.
	 * 
	 * @param identifier User identifier
	 * @return User corresponding to the id searched.
	 * @throws UserNotFoundException
	 */
	User findOne(String identifier) throws UserNotFoundException;

	/**
	 * Creates a new user.
	 * 
	 * @param userDTO DTO with creation properties
	 * @return The new user.
	 */
	UserDTO create(UserPostDTO userDto) throws UserEmailExistsException, UserIdentifierExistsException;

	/**
	 * Updates an user.
	 * 
	 * @param identifier User identifier
	 * @param user
	 * @throws UserWrongUpdateException
	 * @throws UserInvalidStatusException
	 * @throws UserNotFoundException
	 */
	void update(String identifier, UserPutDTO userDto)
			throws UserInvalidStatusException, UserEmailExistsException, UserIdentifierExistsException, UserWrongPasswordException, UserWrongUpdateException, UserNotFoundException;

	/**
	 * Deletes a user by identifier
	 * 
	 * @param identifier User identifier
	 * @throws UserNotFoundException
	 */
	void delete(String identifier) throws UserNotFoundException;

	/**
	 * Update user password
	 * 
	 * @param identifier User identifier
	 * @param userDTO    User password dto
	 * @throws UserWrongUpdateException
	 * @throws UserNotFoundException
	 * @throws UserWrongPasswordException
	 */
	void updatePassword(String identifier, UserPasswordDTO userDTO) throws UserWrongUpdateException, UserNotFoundException, UserWrongPasswordException;

}
