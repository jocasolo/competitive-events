package es.jocasolo.competitiveeventsapi.service;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.jocasolo.competitiveeventsapi.dao.UserDAO;
import es.jocasolo.competitiveeventsapi.dto.user.UserDTO;
import es.jocasolo.competitiveeventsapi.dto.user.UserPostDTO;
import es.jocasolo.competitiveeventsapi.dto.user.UserPutDTO;
import es.jocasolo.competitiveeventsapi.enums.user.UserStatusType;
import es.jocasolo.competitiveeventsapi.enums.user.UserType;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserEmailExistsException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserIdentifierExistsException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserInvalidStatusException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserWrongPasswordException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserWrongUpdateException;
import es.jocasolo.competitiveeventsapi.model.user.User;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDAO userDao;

	@Autowired
	private CommonService commonService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	@Transactional(readOnly = true)
	public User findOne(String id) throws UserNotFoundException {
		final User user = userDao.findOne(id);
		if (user == null)
			throw new UserNotFoundException();

		return user;
	}

	@Override
	public UserDTO create(UserPostDTO userDto) throws UserEmailExistsException, UserIdentifierExistsException {
		User user = commonService.transform(userDto, User.class);

		if (emailExist(userDto.getEmail()))
			throw new UserEmailExistsException();

		if (identifierExists(userDto.getIdentifier()))
			throw new UserIdentifierExistsException();

		user.setType(UserType.NORMAL);
		user.setStatus(UserStatusType.ACTIVE);
		user.setRegisterDate(new Date());
		user.setConfirmed(false);
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));

		return commonService.transform(userDao.save(user), UserDTO.class);
	}

	@Override
	public void update(String identifier, UserPutDTO dto) throws UserWrongUpdateException, UserInvalidStatusException, UserEmailExistsException, UserIdentifierExistsException,
			UserWrongPasswordException, UserNotFoundException {

		if (StringUtils.isNotEmpty(dto.getIdentifier()) && !dto.getIdentifier().equals(identifier))
			throw new UserWrongUpdateException();

		User user = userDao.findOne(identifier);
		if (user == null)
			throw new UserNotFoundException();

		if (StringUtils.isNotEmpty(dto.getEmail()) && user.getIdentifier().equals(dto.getIdentifier()) && emailExist(dto.getEmail()))
			throw new UserEmailExistsException();

		if (!validPassword(user.getPassword(), dto.getPassword()))
			throw new UserWrongPasswordException();

		// TODO validate update
		user.setBirthDate(dto.getBirthDate());
		user.setDescription(dto.getDescription());
		user.setEmail(dto.getEmail());
		user.setName(dto.getName());
		user.setSurname(dto.getSurname());
		user.setPassword(passwordEncoder.encode(dto.getNewPassword()));

		userDao.save(user);
	}

	@Override
	public void delete(String identifier) throws UserNotFoundException {

		// TODO validate delete
		User user = userDao.findOne(identifier);
		user.setStatus(UserStatusType.DELETED);
		userDao.save(user);

	}

	/**
	 * Checks if there is another user with the same email
	 * 
	 * @param email User email
	 * @return True if there is an user with the same email
	 */
	private boolean emailExist(String email) {
		final User user = userDao.findOneByEmail(email);
		return user != null;
	}

	/**
	 * Checks if there is another user with the same identifier
	 * 
	 * @param identifier User identifier
	 * @return True if there is an user tith the same identifier
	 */
	private boolean identifierExists(String identifier) {
		final User user = userDao.findOne(identifier);
		return user != null;
	}

	private boolean validPassword(String encoded, String raw) {
		return passwordEncoder.matches(raw, encoded);
	}

}
