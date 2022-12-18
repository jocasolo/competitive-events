package es.jocasolo.competitiveeventsapi.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import es.jocasolo.competitiveeventsapi.constants.CommonConstants;
import es.jocasolo.competitiveeventsapi.dao.UserDAO;
import es.jocasolo.competitiveeventsapi.dto.user.UserCompleteDTO;
import es.jocasolo.competitiveeventsapi.dto.user.UserDTO;
import es.jocasolo.competitiveeventsapi.dto.user.UserPasswordDTO;
import es.jocasolo.competitiveeventsapi.dto.user.UserPostDTO;
import es.jocasolo.competitiveeventsapi.dto.user.UserPutDTO;
import es.jocasolo.competitiveeventsapi.enums.ImageType;
import es.jocasolo.competitiveeventsapi.enums.SearchTerm;
import es.jocasolo.competitiveeventsapi.enums.user.UserStatusType;
import es.jocasolo.competitiveeventsapi.enums.user.UserType;
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
import es.jocasolo.competitiveeventsapi.model.Image;
import es.jocasolo.competitiveeventsapi.model.User;
import es.jocasolo.competitiveeventsapi.utils.EventUtils;
import es.jocasolo.competitiveeventsapi.utils.security.AuthenticationFacade;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserDAO userDao;

	@Autowired
	private CommonService commonService;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
	private AuthenticationFacade authentication;

	@Override
	@Transactional(readOnly = true)
	public UserCompleteDTO findOne(String id, SearchTerm searchTerm) throws UserNotFoundException {
		
		User user = null;
		switch (searchTerm) {
			case PHONE:
				user = userDao.findOneByPhone(id.replaceAll("[^\\d.]", ""));
				break;
			case EMAIL:
				user = userDao.findOneByEmail(id);
				break;
			case ID:
			default:
				user = userDao.findOne(id);
		}
		
		if (user == null)
			throw new UserNotFoundException();
		
		if(!user.equals(authentication.getUser())) {
			user.setEmail(null);
		}
		
		return commonService.transform(user, UserCompleteDTO.class);
	}
	
	@Override
	@Transactional(readOnly = true)
	public UserCompleteDTO findOneByPhone(String phone) throws UserNotFoundException {
		final User user = userDao.findOneByPhone(phone);
		if (user == null)
			throw new UserNotFoundException();
		
		return commonService.transform(user, UserCompleteDTO.class);
	}
	
	@Override
	public UserDTO create(UserPostDTO userDto) throws UserEmailExistsException, UserUsenameExistsException, UserPhoneExistsException {
		User user = commonService.transform(userDto, User.class);

		if (emailExist(userDto.getEmail()))
			throw new UserEmailExistsException();

		if (usernameExists(userDto.getId()))
			throw new UserUsenameExistsException();
		
		if(StringUtils.isNotEmpty(userDto.getPhone()) && phoneExists(userDto.getPhone())) {
			throw new UserPhoneExistsException();
		}
		
		user.setType(UserType.NORMAL);
		user.setStatus(UserStatusType.NOT_CONFIRMED);
		final String key = UUID.randomUUID().toString();
		user.setConfirmKey(key);
		user.setRegisterDate(new Date());
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		
		sendConfirmationEmail(userDto, key);

		return commonService.transform(userDao.save(user), UserDTO.class);
	}
	
	private void sendConfirmationEmail(UserPostDTO userDto, String key) {
		Map<String,Object> parameters = new HashMap<>();
		parameters.put("key", key);
		emailService.sendSimpleMessage(userDto.getEmail(), CommonConstants.EMAIL_CONFIRMATION_SUBJECT, CommonConstants.EMAIL_CONFIRMATION_TEMPLATE, parameters);
	}

	@Override
	public void update(String id, UserPutDTO dto) throws UserInvalidStatusException, UserEmailExistsException, UserUsenameExistsException,
			UserWrongPasswordException, UserNotFoundException, UserNotValidException {
		
		User user = userDao.findOne(id);
		if (user == null)
			throw new UserNotFoundException();
		
		if(!authentication.getUser().equals(user))
			throw new UserNotValidException();

		if (StringUtils.isNotEmpty(dto.getEmail()) && emailExist(dto.getEmail()))
			throw new UserEmailExistsException();

		user.setBirthDate(EventUtils.getValue(dto.getBirthDate(), user.getBirthDate()));
		user.setDescription(EventUtils.getValue(dto.getDescription(), user.getDescription()));
		user.setEmail(EventUtils.getValue(dto.getEmail(), user.getEmail()));
		user.setName(EventUtils.getValue(dto.getName(), user.getName()));
		user.setSurname(EventUtils.getValue(dto.getSurname(),user.getSurname()));
		
		userDao.save(user);
	}
	
	@Override
	public void updatePassword(String id, UserPasswordDTO dto) throws UserWrongUpdateException, UserNotFoundException, UserWrongPasswordException {
		
		if (StringUtils.isNotEmpty(dto.getId()) && !dto.getId().equals(id))
			throw new UserWrongUpdateException();

		User user = userDao.findOne(id);
		if (user == null)
			throw new UserNotFoundException();

		if (!validPassword(user.getPassword(), dto.getPassword()))
			throw new UserWrongPasswordException();

		if(StringUtils.isNotEmpty(dto.getNewPassword()))
			user.setPassword(passwordEncoder.encode(dto.getNewPassword()));

		userDao.save(user);
		
	}

	@Override
	public void delete(String username) throws UserNotFoundException, UserNotValidException {

		if(!validUpdateDelete(username))
			throw new UserNotValidException();
		
		User user = userDao.findOne(username);
		
		if(user == null)
			throw new UserNotFoundException();
		
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
	 * Checks if there is another user with the same phone
	 * @param phone User phone number
	 * @return True if there is an user with the same phone
	 */
	private boolean phoneExists(String phone) {
		final User user = userDao.findOneByPhone(phone);
		return user != null;
	}

	/**
	 * Checks if there is another user with the same id
	 * 
	 * @param id User id
	 * @return True if there is an user tith the same id
	 */
	private boolean usernameExists(String id) {
		final User user = userDao.findOne(id);
		return user != null;
	}

	private boolean validPassword(String encoded, String raw) {
		return passwordEncoder.matches(raw, encoded);
	}
	
	private boolean validUpdateDelete(String username) {
		return authentication.getUser().getId().equals(username);
	}

	@Override
	public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
		final User user = userDao.findOne(id);
		if(user == null)
			throw new UsernameNotFoundException("Username or password not valid");
		
		return userDao.findOne(id);
	}

	@Override
	public UserDTO confirm(String key) throws UserNotFoundException {
		List<User> users = userDao.findByConfirmKey(key);
		if(users.isEmpty())
			throw new UserNotFoundException();
		
		User user = users.get(0);
		user.setStatus(UserStatusType.ACTIVE);
		return commonService.transform(userDao.save(user), UserDTO.class);
	}

	@Override
	public UserDTO updateAvatar(String username, MultipartFile multipart) throws UserNotFoundException, UserNotValidException, ImageUploadException {
		
		User user = userDao.findOne(username);
		
		if(!validUpdateDelete(username))
			throw new UserNotValidException();
		
		if (user == null)
			throw new UserNotFoundException();
		
		Image avatar = imageService.upload(multipart, ImageType.AVATAR);
		user.setAvatar(avatar);
		
		return commonService.transform(userDao.save(user), UserDTO.class);
		
	}

	@Override
	public void deleteAvatar(String username) throws UserNotFoundException, UserNotValidException, ImageNotFoundException {
		
		User user = userDao.findOne(username);
		
		if (user == null)
			throw new UserNotFoundException();
		
		final String avatarId = user.getAvatar().getStorageId();
		user.setAvatar(null);
		userDao.save(user);
		
		imageService.delete(avatarId);
		
	}

}
