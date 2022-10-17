package es.jocasolo.competitiveeventsapi.mappers;

import java.util.Set;

import es.jocasolo.competitiveeventsapi.dto.user.UserLiteWithEventDTO;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserNotFoundException;
import es.jocasolo.competitiveeventsapi.model.Event;
import es.jocasolo.competitiveeventsapi.model.User;

public interface UserMapper {
	
	UserLiteWithEventDTO map(User user, Event event) throws UserNotFoundException, EventNotFoundException;
	
	Set<UserLiteWithEventDTO> map(Set<User> users, Event event);
	
}
