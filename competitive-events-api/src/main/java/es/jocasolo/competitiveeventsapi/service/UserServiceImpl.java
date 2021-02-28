package es.jocasolo.competitiveeventsapi.service;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.jocasolo.competitiveeventsapi.dao.UserDAO;
import es.jocasolo.competitiveeventsapi.dto.user.UserDTO;
import es.jocasolo.competitiveeventsapi.dto.user.UserPasswordDTO;
import es.jocasolo.competitiveeventsapi.dto.user.UserPostDTO;
import es.jocasolo.competitiveeventsapi.dto.user.UserPutDTO;
import es.jocasolo.competitiveeventsapi.enums.user.UserStatusType;
import es.jocasolo.competitiveeventsapi.enums.user.UserType;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserEmailExistsException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserUsenameExistsException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserInvalidStatusException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserWrongPasswordException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserWrongUpdateException;
import es.jocasolo.competitiveeventsapi.model.user.User;
import es.jocasolo.competitiveeventsapi.utils.EventUtils;

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
	public UserDTO create(UserPostDTO userDto) throws UserEmailExistsException, UserUsenameExistsException {
		User user = commonService.transform(userDto, User.class);

		if (emailExist(userDto.getEmail()))
			throw new UserEmailExistsException();

		if (usernameExists(userDto.getUsername()))
			throw new UserUsenameExistsException();

		user.setType(UserType.NORMAL);
		user.setStatus(UserStatusType.ACTIVE);
		user.setRegisterDate(new Date());
		user.setConfirmed(false);
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));

		return commonService.transform(userDao.save(user), UserDTO.class);
	}

	@Override
	public void update(String username, UserPutDTO dto) throws UserWrongUpdateException, UserInvalidStatusException, UserEmailExistsException, UserUsenameExistsException,
			UserWrongPasswordException, UserNotFoundException {

		if (StringUtils.isNotEmpty(dto.getUsername()) && !dto.getUsername().equals(username))
			throw new UserWrongUpdateException();

		User user = userDao.findOne(username);
		if (user == null)
			throw new UserNotFoundException();

		if (StringUtils.isNotEmpty(dto.getEmail()) && emailExist(dto.getEmail()))
			throw new UserEmailExistsException();

		if (!validPassword(user.getPassword(), dto.getPassword()))
			throw new UserWrongPasswordException();

		user.setBirthDate(EventUtils.getValue(dto.getBirthDate(), user.getBirthDate()));
		user.setDescription(EventUtils.getValue(dto.getDescription(), user.getDescription()));
		user.setEmail(EventUtils.getValue(dto.getEmail(), user.getEmail()));
		user.setName(EventUtils.getValue(dto.getName(), user.getName()));
		user.setSurname(EventUtils.getValue(dto.getSurname(),user.getSurname()));
		
		userDao.save(user);
	}
	
	@Override
	public void updatePassword(String username, UserPasswordDTO dto) throws UserWrongUpdateException, UserNotFoundException, UserWrongPasswordException {
		
		if (StringUtils.isNotEmpty(dto.getUsername()) && !dto.getUsername().equals(username))
			throw new UserWrongUpdateException();

		User user = userDao.findOne(username);
		if (user == null)
			throw new UserNotFoundException();

		if (!validPassword(user.getPassword(), dto.getPassword()))
			throw new UserWrongPasswordException();

		if(StringUtils.isNotEmpty(dto.getNewPassword()))
			user.setPassword(passwordEncoder.encode(dto.getNewPassword()));

		userDao.save(user);
		
	}

	@Override
	public void delete(String username) throws UserNotFoundException {

		// TODO validate delete
		User user = userDao.findOne(username);
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
	 * Checks if there is another user with the same username
	 * 
	 * @param username User username
	 * @return True if there is an user tith the same username
	 */
	private boolean usernameExists(String username) {
		final User user = userDao.findOne(username);
		return user != null;
	}

	private boolean validPassword(String encoded, String raw) {
		return passwordEncoder.matches(raw, encoded);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userDao.findOne(username);
	}

}
