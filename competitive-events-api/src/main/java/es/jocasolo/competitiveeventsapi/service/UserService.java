package es.jocasolo.competitiveeventsapi.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import es.jocasolo.competitiveeventsapi.dto.user.UserDTO;
import es.jocasolo.competitiveeventsapi.dto.user.UserPasswordDTO;
import es.jocasolo.competitiveeventsapi.dto.user.UserPostDTO;
import es.jocasolo.competitiveeventsapi.dto.user.UserPutDTO;
import es.jocasolo.competitiveeventsapi.exceptions.image.ImageNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.image.ImageUploadException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserEmailExistsException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserUsenameExistsException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserInvalidStatusException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserNotValidException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserWrongPasswordException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserWrongUpdateException;
import es.jocasolo.competitiveeventsapi.model.User;

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
	 * @param username User id
	 * @throws UserNotFoundException
	 * @throws UserNotValidException 
	 */
	void delete(String username) throws UserNotFoundException, UserNotValidException;

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
	
	/**
	 * User email confirmation.
	 * @param key Generated key for confirmation
	 * @return
	 * @throws UserNotFoundException 
	 */
	UserDTO confirm(String key) throws UserNotFoundException;
	
	/**
	 * Updates user's avatar.
	 * @param username User identifier
	 * @param multipart File multipart
	 * @return User updated
	 * @throws UserNotFoundException 
	 * @throws UserNotValidException 
	 * @throws ImageUploadException 
	 */
	UserDTO updateAvatar(String username, MultipartFile multipart) throws UserNotFoundException, UserNotValidException, ImageUploadException;
	
	
	/**
	 * Delete user avatar.
	 * @param username Username
	 * @throws UserNotFoundException
	 * @throws UserNotValidException
	 * @throws ImageNotFoundException 
	 */
	void deleteAvatar(String username) throws UserNotFoundException, UserNotValidException, ImageNotFoundException;

}
