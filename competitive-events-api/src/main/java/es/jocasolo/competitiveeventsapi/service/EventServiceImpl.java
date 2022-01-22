package es.jocasolo.competitiveeventsapi.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import es.jocasolo.competitiveeventsapi.dao.EventDAO;
import es.jocasolo.competitiveeventsapi.dao.EventUserDAO;
import es.jocasolo.competitiveeventsapi.dao.PunishmentDAO;
import es.jocasolo.competitiveeventsapi.dao.RewardDAO;
import es.jocasolo.competitiveeventsapi.dao.ScoreDAO;
import es.jocasolo.competitiveeventsapi.dao.UserDAO;
import es.jocasolo.competitiveeventsapi.dto.event.EventDTO;
import es.jocasolo.competitiveeventsapi.dto.event.EventPageDTO;
import es.jocasolo.competitiveeventsapi.dto.event.EventPostDTO;
import es.jocasolo.competitiveeventsapi.dto.event.EventPutDTO;
import es.jocasolo.competitiveeventsapi.dto.eventuser.EventUserDTO;
import es.jocasolo.competitiveeventsapi.dto.eventuser.EventUserPostDTO;
import es.jocasolo.competitiveeventsapi.dto.eventuser.EventUserPutDTO;
import es.jocasolo.competitiveeventsapi.enums.ImageType;
import es.jocasolo.competitiveeventsapi.enums.event.EventInscriptionType;
import es.jocasolo.competitiveeventsapi.enums.event.EventStatusType;
import es.jocasolo.competitiveeventsapi.enums.event.EventType;
import es.jocasolo.competitiveeventsapi.enums.event.EventVisibilityType;
import es.jocasolo.competitiveeventsapi.enums.eventuser.EventUserPrivilegeType;
import es.jocasolo.competitiveeventsapi.enums.eventuser.EventUserStatusType;
import es.jocasolo.competitiveeventsapi.enums.score.ScoreSortType;
import es.jocasolo.competitiveeventsapi.enums.score.ScoreValueType;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventInvalidStatusException;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventUserAcceptedException;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventUserRejectedException;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventWrongUpdateException;
import es.jocasolo.competitiveeventsapi.exceptions.image.ImageUploadException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserNotValidException;
import es.jocasolo.competitiveeventsapi.mappers.EventMapper;
import es.jocasolo.competitiveeventsapi.model.Event;
import es.jocasolo.competitiveeventsapi.model.EventUser;
import es.jocasolo.competitiveeventsapi.model.Image;
import es.jocasolo.competitiveeventsapi.model.Punishment;
import es.jocasolo.competitiveeventsapi.model.Reward;
import es.jocasolo.competitiveeventsapi.model.Score;
import es.jocasolo.competitiveeventsapi.model.User;
import es.jocasolo.competitiveeventsapi.model.keys.EventUserKey;
import es.jocasolo.competitiveeventsapi.utils.EventUtils;
import es.jocasolo.competitiveeventsapi.utils.security.AuthenticationFacade;

@Service
public class EventServiceImpl implements EventService {

	@Autowired
	private AuthenticationFacade authentication;

	@Autowired
	private EventDAO eventDao;

	@Autowired
	private UserDAO userDao;
	
	@Autowired
	private RewardDAO rewardDao;
	
	@Autowired
	private PunishmentDAO punishmentDao;
	
	@Autowired
	private ImageService imageService;

	@Autowired
	private EventUserDAO eventUserDao;
	
	@Autowired
	private ScoreDAO scoreDao;

	@Autowired
	private CommonService commonService;
	
	@Autowired
	private EventMapper eventMapper;

	@Override
	@Transactional(readOnly = true)
	public Event findOne(String id) throws EventNotFoundException {
		final Event event = eventDao.findOne(id);
		if (event == null)
			throw new EventNotFoundException();

		return event;
	}

	@Override
	public EventDTO create(EventPostDTO dto) {

		Event event = commonService.transform(dto, Event.class);

		// Event data
		event.setCreationDate(new Date());
		event.setId(RandomStringUtils.randomAlphanumeric(8)); // Random id
		event.setType(EventType.getValue(dto.getType(), EventType.OTHER));
		event.setInscription(EventInscriptionType.getValue(dto.getInscription(), EventInscriptionType.PRIVATE));
		event.setVisibility(EventVisibilityType.getValue(dto.getVisibility(), EventVisibilityType.PRIVATE));
		event.setApprovalNeeded(EventUtils.getValue(dto.getApprovalNeeded(), true));
		event.setScoreType(ScoreValueType.getValue(dto.getScoreType(), ScoreValueType.NUMERIC));
		event.setSortScore(ScoreSortType.getValue(dto.getSortScore(), ScoreSortType.ASC));
		event.setInitDate(EventUtils.getValue(dto.getInitDate(), null));
		event.setEndDate(EventUtils.getValue(dto.getEndDate(), null));
		
		event.setStatus(event.isInDateRange() ? EventStatusType.ACTIVE : EventStatusType.NOT_ACTIVE); // ACTIVE when is in date range
		eventDao.save(event);

		// Assign event to the actual user
		final User user = authentication.getUser();
		EventUser eventUser = createEventUser(event, user);
		eventUser.setPrivilege(EventUserPrivilegeType.OWNER);
		eventUserDao.save(eventUser);

		return commonService.transform(event, EventDTO.class);
	}

	@Override
	public void update(String id, EventPutDTO dto) throws EventWrongUpdateException, EventInvalidStatusException, EventNotFoundException {

		Event event = eventDao.findOne(id);
		if(event == null)
			throw new EventNotFoundException();
		
		if(!event.isInDateRange())
			throw new EventInvalidStatusException();
		
		EventUser eventUser = eventUserDao.findOne(event, authentication.getUser());
		if (eventUser != null && eventUser.isOwner()) {
			event.setTitle(EventUtils.getValue(dto.getTitle(), event.getTitle()));
			event.setSubtitle(EventUtils.getValue(dto.getSubtitle(), event.getSubtitle()));
			event.setDescription(EventUtils.getValue(dto.getDescription(), event.getDescription()));
			event.setApprovalNeeded(EventUtils.getValue(dto.getAppovalNeeded(), event.getApprovalNeeded()));
			event.setInitDate(EventUtils.getValue(dto.getInitDate(), event.getInitDate()));
			event.setEndDate(EventUtils.getValue(dto.getEndDate(), event.getEndDate()));
			event.setStatus(event.isInDateRange() ? EventStatusType.ACTIVE : EventStatusType.NOT_ACTIVE); // ACTIVE when is in date range
			event.setType(EventType.getValue(dto.getType(), event.getType()));
			event.setInscription(EventInscriptionType.getValue(dto.getInscription(), event.getInscription()));
			event.setVisibility(EventVisibilityType.getValue(dto.getVisibility(), event.getVisibility()));
			event.setStatus(EventStatusType.getValue(dto.getStatus(), event.getStatus()));
			event.setMaxPlaces(EventUtils.getValue(dto.getMaxPlaces(), event.getMaxPlaces()));
			event.setScoreType(ScoreValueType.getValue(dto.getScoreType(), event.getScoreType()));
			event.setSortScore(ScoreSortType.getValue(dto.getSortScore(), event.getSortScore()));
			eventDao.save(event);

		} else {
			throw new EventWrongUpdateException();
		}
	}
	
	@Override
	public EventDTO updateImage(String id, MultipartFile multipart) throws EventNotFoundException, UserNotValidException, ImageUploadException {
		
		Event event = eventDao.findOne(id);
		if(event == null)
			throw new EventNotFoundException();
		
		User user = authentication.getUser();
		EventUser eventUser = eventUserDao.findOneByIds(event.getId(), user.getId());
		if(eventUser == null || !eventUser.isOwner())
			throw new UserNotValidException();
		
		Image image = imageService.upload(multipart, ImageType.EVENT);
		event.setImage(image);
		
		return commonService.transform(eventDao.save(event), EventDTO.class);
	}

	@Override
	public void delete(String id) throws EventNotFoundException, UserNotValidException {

		Event event = eventDao.findOne(id);
		if(event == null)
			throw new EventNotFoundException();
		
		EventUser eventUser = eventUserDao.findOne(event, authentication.getUser());
		if (eventUser != null && eventUser.isOwner()) {
			event.setStatus(EventStatusType.DELETED);
			eventDao.save(event);
		} else {
			throw new UserNotValidException();
		}

	}

	@Override
	public EventPageDTO search(String title, EventType type, EventStatusType status, EventInscriptionType inscription, 
			String username, PageRequest pageRequest) throws UserNotValidException {
		
		Page<Event> events = null;
		User user = authentication.getUser();
		
		if(user == null) {
			// Public events
			events = eventDao.search(title, EventType.getEnumOrNull(type), EventStatusType.getEnumOrNull(status), 
				EventInscriptionType.getEnumOrNull(inscription), pageRequest);
		} else {
			// User events
			events = eventDao.searchByUser(title, EventType.getEnumOrNull(type), EventStatusType.getEnumOrNull(status), 
					EventInscriptionType.getEnumOrNull(inscription), user, pageRequest);
		}
		
		EventPageDTO dto = new EventPageDTO();
		dto.setEvents(eventMapper.map(events.getContent()));
		dto.setTotal(events.getTotalElements());
		dto.setHasNext(events.hasNext());
		dto.setHasPrevious(events.hasPrevious());
		dto.setPages(events.getTotalPages());
		
		return dto;
	}
	
	@Override
	public void init(String id) throws EventNotFoundException, UserNotValidException, EventInvalidStatusException {
		
		EventUser eventUser = eventUserDao.findOneByIds(id, authentication.getUser().getId());
		if(eventUser == null)
			throw new EventNotFoundException();
		
		if(!eventUser.isOwner())
			throw new UserNotValidException();
		
		Event event = eventUser.getEvent();
		if(!event.getStatus().equals(EventStatusType.NOT_ACTIVE))
			throw new EventInvalidStatusException();
		
		initEvent(eventUser.getEvent());
		
	}
	
	@Override
	public void initEvent(Event event) {
		event.setStatus(EventStatusType.ACTIVE);
		eventDao.save(event);
	}
	
	@Override
	public void finish(String id) throws EventNotFoundException, UserNotValidException, EventInvalidStatusException {
		
		EventUser eventUser = eventUserDao.findOneByIds(id, authentication.getUser().getId());
		if(eventUser == null)
			throw new EventNotFoundException();
		
		if(!eventUser.isOwner())
			throw new UserNotValidException();
		
		Event event = eventUser.getEvent();
		if(!event.getStatus().equals(EventStatusType.ACTIVE) && !event.getStatus().equals(EventStatusType.NOT_ACTIVE))
			throw new EventInvalidStatusException();
		
		finishEvent(eventUser.getEvent());
	}
	
	@Override
	public void finishEvent(Event event) {
		
		event.setStatus(EventStatusType.FINISHED);
		
		// Assign rewards to users
		asignRewards(event);
		
		// Assign punishments to users
		assingPunishments(event);
		
		eventDao.save(event);
	}

	private void asignRewards(Event event) {
		if(CollectionUtils.isNotEmpty(event.getRewards())) {
			
			List<Score> scores = null;
			if(event.getSortScore().equals(ScoreSortType.ASC))
				scores = scoreDao.findAllSortedAsc(event);
			else
				scores = scoreDao.findAllSortedDesc(event);
			
			for(Reward reward : event.getRewards()) {
				if(reward.getWinner() == null && reward.getRequiredPosition() < scores.size()) {
					final User winner = scores.get(reward.getRequiredPosition()).getUser();
					reward.setWinner(winner);
					rewardDao.save(reward);
				}
			}
		}
	}
	
	private void assingPunishments(Event event) {
		if(CollectionUtils.isNotEmpty(event.getPunishments())) {
			List<Score> scores = null;
			if(event.getSortScore().equals(ScoreSortType.ASC))
				scores = scoreDao.findAllSortedDesc(event);
			else
				scores = scoreDao.findAllSortedAsc(event);
			
			for(Punishment punishment : event.getPunishments()) {
				if(punishment.getLooser() == null && punishment.getRequiredPosition() < scores.size()) {
					final User looser = scores.get(punishment.getRequiredPosition()).getUser();
					punishment.setLooser(looser);
					punishmentDao.save(punishment);
				}
			}
		}
	}

	// ************************************
	// EVENT USER
	// ************************************

	@Override
	public EventUserDTO addUser(String eventId, EventUserPostDTO eventUserDto) 
			throws EventWrongUpdateException, UserNotValidException, UserNotFoundException, EventUserRejectedException, EventNotFoundException, EventInvalidStatusException, EventUserAcceptedException {

		EventUserStatusType status = null;

		User targetUser = userDao.findOne(eventUserDto.getUsername());
		if (targetUser == null)
			throw new UserNotFoundException();
		
		Event event = eventDao.findOne(eventId);
		if(event == null)
			throw new EventNotFoundException();
		
		if(!event.isInDateRange())
			throw new EventInvalidStatusException();

		User authenticatedUser = authentication.getUser();
		EventUser newEventUser = createEventUser(event, targetUser);

		// add or reject user
		if(Boolean.FALSE.equals(eventUserDto.getReject()))
			status = addUserToEvent(event, targetUser, authenticatedUser, newEventUser);
		else
			status = rejectUserToEvent(event, targetUser, authenticatedUser);

		if (status == null)
			throw new EventWrongUpdateException();

		// Save
		newEventUser.setStatus(status);
		eventUserDao.save(newEventUser);

		// Sets the dto for response
		EventUserDTO dto = new EventUserDTO();
		dto.setEventId(eventId);
		dto.setUserId(eventUserDto.getUsername());
		dto.setIncorporationDate(newEventUser.getIncorporationDate());
		dto.setLastStatusDate(newEventUser.getLastStatusDate());
		dto.setPrivilege(newEventUser.getPrivilege());
		dto.setStatus(status);

		return dto;
	}

	/**
	 * @param event
	 * @param targetUser
	 * @param authenticatedUser
	 * @param newEventUser
	 * @return
	 * @throws EventWrongUpdateException
	 * @throws EventUserRejectedException
	 * @throws EventUserAcceptedException 
	 */
	private EventUserStatusType addUserToEvent(Event event, User targetUser, User authenticatedUser, EventUser newEventUser) 
			throws EventWrongUpdateException, EventUserRejectedException, EventUserAcceptedException {
		
		// TODO test private and public
		
		EventUserStatusType status = null;
		EventUser authenticatedEventUser = eventUserDao.findOneAllStatus(event, authenticatedUser);
		EventUser targetEventUser = eventUserDao.findOneAllStatus(event, targetUser);
		
		// if the type of registration is private
		if (event.getInscription().equals(EventInscriptionType.PRIVATE)) {
			
			// Private event
			status = addUserToPrivateEvent(targetUser, authenticatedUser, newEventUser, status, authenticatedEventUser, targetEventUser);

		} else {
			
			// Public inscription
			status = addUserToPublicEvent(targetUser, authenticatedUser, authenticatedEventUser, targetEventUser);
		}
		
		return status;
	}

	private EventUserStatusType addUserToPublicEvent(User targetUser, User authenticatedUser, EventUser authenticatedEventUser, EventUser targetEventUser) {
		EventUserStatusType status;
		if(targetUser.equals(authenticatedUser) && Boolean.TRUE.equals(targetEventUser.getEvent().getApprovalNeeded())) {
			
			// User request approval to join in a public event with approval needed
			status = EventUserStatusType.WAITING_APPROVAL;
			
		} else if((targetUser.equals(authenticatedUser) && !Boolean.TRUE.equals(targetEventUser.getEvent().getApprovalNeeded())) 
				|| authenticatedEventUser.isOwner() && targetEventUser.isWaiting()) {
			
			// User joins event without approval needed
			status = EventUserStatusType.ACCEPTED;
			
		} else {
			
			// Owner invited one user
			status = EventUserStatusType.INVITED;
		}
		return status;
	}

	private EventUserStatusType addUserToPrivateEvent(User targetUser, User authenticatedUser, EventUser newEventUser, EventUserStatusType status, EventUser authenticatedEventUser,
			EventUser targetEventUser) throws EventWrongUpdateException, EventUserAcceptedException, EventUserRejectedException {
		
		if(targetEventUser == null || authenticatedEventUser == null)
			throw new EventWrongUpdateException();
		
		// If is already accepted
		if(targetEventUser.isAccepted())
			throw new EventUserAcceptedException();
		
		// If is rejected or deleted
		if(targetEventUser.isRejected() || targetEventUser.isDeleted())
			throw new EventUserRejectedException();

		// If the logged-in user is an owner of the event, sets the user waiting
		// to join. If the logged-in user is waiting for join, it status change to
		// accepted
		if (authenticatedEventUser.isOwner()) {
			
			status = EventUserStatusType.INVITED;
			newEventUser.setIncorporationDate(null);

		} else if (authenticatedEventUser.isOwner() && targetEventUser.isWaiting()) {
			
			// Owner accepts an user waiting for approval
			status = EventUserStatusType.ACCEPTED;

		} else if (targetEventUser.isInvited() && targetUser.equals(authenticatedUser)) {
			
			// User accept an invitation to join
			status = EventUserStatusType.ACCEPTED;
			
		} else if (targetUser.equals(authenticatedUser)){
			
			// User request approval to join
			status = EventUserStatusType.WAITING_APPROVAL;
			newEventUser.setIncorporationDate(null);
		}
		return status;
	}
	
	private EventUserStatusType rejectUserToEvent(Event event, User targetUser, User authenticatedUser) 
			throws EventWrongUpdateException {
		
		EventUserStatusType status = null;
		
		EventUser authenticatedEventUser = eventUserDao.findOne(event, authenticatedUser);
		EventUser targetEventUser = eventUserDao.findOne(event, targetUser);
		
		if(targetEventUser != null && (targetEventUser.getStatus().equals(EventUserStatusType.ACCEPTED) 
				|| targetEventUser.getStatus().equals(EventUserStatusType.DELETED) || targetEventUser.getStatus().equals(EventUserStatusType.REJECTED)))
			throw new EventWrongUpdateException();
		
		if(targetUser.equals(authenticatedUser) || (targetEventUser != null && (authenticatedEventUser.isOwner()))) {
			status = EventUserStatusType.REJECTED;
		}
		
		return status;
	}
	
	@Override
	public void updateUser(String eventId, EventUserPutDTO eventUserDto) throws UserNotFoundException, EventWrongUpdateException {
		
		Event event = eventDao.findOne(eventId);
		User targetUser = userDao.findOne(eventUserDto.getUsername());
		
		if (targetUser == null)
			throw new UserNotFoundException();
		
		if(event == null)
			throw new EventWrongUpdateException();
		
		User authenticatedUser = authentication.getUser();
		EventUser authenticatedEventUser = eventUserDao.findOne(event, authenticatedUser);
		EventUser targetEventUser = eventUserDao.findOne(event, targetUser);
		
		if(authenticatedEventUser == null || targetEventUser == null)
			throw new EventWrongUpdateException();
		
		if(!targetEventUser.isOwner() && authenticatedEventUser.isOwner() && eventUserDto.getPrivilege() != null && !eventUserDto.getPrivilege().equals(EventUserPrivilegeType.OWNER)) {
			targetEventUser.setPrivilege(eventUserDto.getPrivilege());
		}
		
		if(eventUserDto.getStatus() != null && authenticatedEventUser.isOwner()) {
			targetEventUser.setStatus(eventUserDto.getStatus());
			targetEventUser.setLastStatusDate(new Date());
		}
		
		// Save
		eventUserDao.save(targetEventUser);
		
	}
	
	@Override
	public void removeUser(String eventId, EventUserPostDTO eventUserDto) throws UserNotFoundException, EventWrongUpdateException {
		
		Event event = eventDao.findOne(eventId);
		User targetUser = userDao.findOne(eventUserDto.getUsername());
		
		if (targetUser == null)
			throw new UserNotFoundException();
		
		if(event == null)
			throw new EventWrongUpdateException();
		
		EventUser eventUser = eventUserDao.findOne(event, targetUser);
		if(eventUser == null)
			throw new EventWrongUpdateException();
		
		User authenticatedUser = authentication.getUser();
		
		if(authenticatedUser.equals(targetUser)) {
			eventUser.setStatus(EventUserStatusType.DELETED);
		} else {
			final EventUser eventAuthenticated = eventUserDao.findOne(event, authenticatedUser);
			if(eventAuthenticated.isOwner())
				eventUser.setStatus(EventUserStatusType.DELETED);
			else
				throw new EventWrongUpdateException();
		}
		
		// Save
		eventUserDao.save(eventUser);
		
	}

	private EventUser createEventUser(Event event, User user) {
		
		EventUser eventUser = new EventUser();
		EventUserKey key = new EventUserKey();
		key.setEventId(event.getId());
		key.setUserId(user.getId());
		eventUser.setId(key);
		eventUser.setEvent(event);
		eventUser.setUser(user);
		Date date = new Date();
		eventUser.setIncorporationDate(date);
		eventUser.setLastStatusDate(date);
		eventUser.setPrivilege(EventUserPrivilegeType.USER);
		eventUser.setStatus(EventUserStatusType.ACCEPTED);

		return eventUser;
	}

}
