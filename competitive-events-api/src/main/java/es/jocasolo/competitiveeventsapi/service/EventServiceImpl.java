package es.jocasolo.competitiveeventsapi.service;

import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.jocasolo.competitiveeventsapi.dao.EventDAO;
import es.jocasolo.competitiveeventsapi.dao.EventUserDAO;
import es.jocasolo.competitiveeventsapi.dao.UserDAO;
import es.jocasolo.competitiveeventsapi.dto.event.EventDTO;
import es.jocasolo.competitiveeventsapi.dto.event.EventPageDTO;
import es.jocasolo.competitiveeventsapi.dto.event.EventPostDTO;
import es.jocasolo.competitiveeventsapi.dto.event.EventPutDTO;
import es.jocasolo.competitiveeventsapi.dto.event.EventSummaryDTO;
import es.jocasolo.competitiveeventsapi.dto.eventuser.EventUserDTO;
import es.jocasolo.competitiveeventsapi.dto.eventuser.EventUserPostDTO;
import es.jocasolo.competitiveeventsapi.enums.event.EventInscriptionType;
import es.jocasolo.competitiveeventsapi.enums.event.EventStatusType;
import es.jocasolo.competitiveeventsapi.enums.event.EventType;
import es.jocasolo.competitiveeventsapi.enums.event.EventUserStatusType;
import es.jocasolo.competitiveeventsapi.enums.event.EventVisibilityType;
import es.jocasolo.competitiveeventsapi.enums.user.UserPrivilegeType;
import es.jocasolo.competitiveeventsapi.enums.user.UserType;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventInvalidStatusException;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventWrongUpdateException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserNotValidException;
import es.jocasolo.competitiveeventsapi.model.event.Event;
import es.jocasolo.competitiveeventsapi.model.event.EventUser;
import es.jocasolo.competitiveeventsapi.model.keys.EventUserKey;
import es.jocasolo.competitiveeventsapi.model.user.User;
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
	private EventUserDAO eventUserDao;

	@Autowired
	private CommonService commonService;

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
		event.setStatus(EventStatusType.ACTIVE);
		event.setApprovalNeeded(EventUtils.getValue(dto.getApprovalNeeded(), true));
		eventDao.save(event);

		// Assign event to the actual user
		final User user = authentication.getUser();
		EventUser eventUser = createEventUser(event, user);
		eventUser.setPrivilege(UserPrivilegeType.OWNER);
		eventUserDao.save(eventUser);

		return commonService.transform(event, EventDTO.class);
	}

	@Override
	public void update(String id, EventPutDTO dto) throws EventWrongUpdateException, EventInvalidStatusException {

		if (StringUtils.isNotEmpty(dto.getId()) && !dto.getId().equals(id))
			throw new EventWrongUpdateException();

		Event event = eventDao.findOne(id);
		if (validUpdate(event)) {
			event.setTitle(EventUtils.getValue(dto.getTitle(), event.getTitle()));
			event.setSubtitle(EventUtils.getValue(dto.getSubtitle(), event.getSubtitle()));
			event.setDescription(EventUtils.getValue(dto.getDescription(), event.getDescription()));
			event.setApprovalNeeded(EventUtils.getValue(dto.getAppovalNeeded(), event.getApprovalNeeded()));
			event.setInitDate(EventUtils.getValue(dto.getInitDate(), event.getInitDate()));
			event.setEndDate(EventUtils.getValue(dto.getEndDate(), event.getEndDate()));
			event.setType(EventType.getValue(dto.getType(), event.getType()));
			event.setInscription(EventInscriptionType.getValue(dto.getInscription(), event.getInscription()));
			event.setVisibility(EventVisibilityType.getValue(dto.getVisibility(), event.getVisibility()));
			event.setStatus(EventStatusType.getValue(dto.getStatus(), event.getStatus()));
			event.setMaxPlaces(EventUtils.getValue(dto.getMaxPlaces(), event.getMaxPlaces()));
			eventDao.save(event);

		} else {
			// TODO throw exception
		}

	}

	@Override
	public void delete(String id) throws EventNotFoundException {

		Event event = eventDao.findOne(id);
		if (validRemove(event)) {
			event.setStatus(EventStatusType.DELETED);
			eventDao.save(event);

		} else {
			// TODO throw exception
		}

	}

	@Override
	public EventPageDTO search(String title, EventType type, EventStatusType status, EventInscriptionType inscription, 
			String username, PageRequest pageRequest) throws UserNotValidException {
		
		Page<Event> events = null;
		User user = authentication.getUser();
		
		if(StringUtils.isEmpty(username)) {
			// Public events
			events = eventDao.search(title, EventType.getEnumOrNull(type), EventStatusType.getEnumOrNull(status), 
				EventInscriptionType.getEnumOrNull(inscription), pageRequest);
		} else if(username.equals(user.getUsername())) {
			// User events
			events = eventDao.searchByUser(title, EventType.getEnumOrNull(type), EventStatusType.getEnumOrNull(status), 
					EventInscriptionType.getEnumOrNull(inscription), user, pageRequest);
		} else {
			throw new UserNotValidException();
		}
		
		EventPageDTO dto = new EventPageDTO();
		dto.setEvents(commonService.transform(events.getContent(), EventSummaryDTO.class));
		dto.setTotal(events.getTotalElements());
		dto.setHasNext(events.hasNext());
		dto.setHasPrevious(events.hasPrevious());
		dto.setPages(events.getTotalPages());
		return dto;
	}

	private boolean validRemove(Event event) {
		final User user = authentication.getUser();
		if (user.getEvents().contains(event)) {
			final EventUser eventUser = eventUserDao.findOne(event.getId(), user.getId());
			return user.getType().equals(UserType.SUPERUSER) || eventUser.getPrivilege().equals(UserPrivilegeType.OWNER);
		}

		return false;
	}

	private boolean validUpdate(Event event) {

		final User user = authentication.getUser();
		if (user.getEvents().contains(event)) {
			final EventUser eventUser = eventUserDao.findOne(event.getId(), user.getId());
			return user.getType().equals(UserType.SUPERUSER) || eventUser.getPrivilege().equals(UserPrivilegeType.OWNER)
					|| eventUser.getPrivilege().equals(UserPrivilegeType.ADMIN);
		}

		return false;
	}

	// ************************************
	// EVENT USER
	// ************************************

	@Override
	public EventUserDTO addUser(String eventId, EventUserPostDTO eventUserDto) throws EventWrongUpdateException {

		EventUserStatusType status = null;

		Event event = eventDao.findOne(eventId);
		User targetUser = userDao.findOne(eventUserDto.getUserId());
		if (event == null || targetUser == null)
			throw new EventWrongUpdateException();

		User authenticatedUser = authentication.getUser();
		EventUser newEventUser = createEventUser(event, targetUser);

		// if the type of registration is private
		if (event.getInscription().equals(EventInscriptionType.PRIVATE)) {
			EventUser authenticatedEventUser = eventUserDao.findOne(eventId, authenticatedUser.getId());
			EventUser targetEventUser = eventUserDao.findOne(eventId, targetUser.getId());

			// If the logged-in user is an owner/admin of the event, sets the user waiting
			// to join. If the logged-in user is waiting for join, it status change to
			// accepted
			if (authenticatedEventUser != null
					&& (authenticatedEventUser.getPrivilege().equals(UserPrivilegeType.OWNER) || authenticatedEventUser.getPrivilege().equals(UserPrivilegeType.ADMIN))) {
				status = EventUserStatusType.WAITING;
				newEventUser.setIncorporationDate(null);

			} else if (targetEventUser != null && targetEventUser.getStatus().equals(EventUserStatusType.WAITING) && targetUser.equals(authenticatedUser)) {
				status = EventUserStatusType.ACCEPTED;
			}

		} else {
			// Public inscription
			status = EventUserStatusType.ACCEPTED;
		}

		if (status == null)
			throw new EventWrongUpdateException();

		// Save
		newEventUser.setStatus(status);
		eventUserDao.save(newEventUser);

		// Sets the dto for response
		EventUserDTO dto = new EventUserDTO();
		dto.setEventId(eventId);
		dto.setUserId(eventUserDto.getUserId());
		dto.setIncorporationDate(newEventUser.getIncorporationDate());
		dto.setLastStatusDate(newEventUser.getLastStatusDate());
		dto.setPrivilege(newEventUser.getPrivilege());
		dto.setStatus(status);

		return dto;
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
		eventUser.setPrivilege(UserPrivilegeType.USER);
		eventUser.setStatus(EventUserStatusType.ACCEPTED);

		return eventUser;
	}

}
