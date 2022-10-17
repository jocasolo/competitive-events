package es.jocasolo.competitiveeventsapi.mappers;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.jocasolo.competitiveeventsapi.dto.eventuser.EventUserDTO;
import es.jocasolo.competitiveeventsapi.dto.image.ImageDTO;
import es.jocasolo.competitiveeventsapi.dto.user.UserLiteWithEventDTO;
import es.jocasolo.competitiveeventsapi.exceptions.event.EventNotFoundException;
import es.jocasolo.competitiveeventsapi.exceptions.user.UserNotFoundException;
import es.jocasolo.competitiveeventsapi.model.Event;
import es.jocasolo.competitiveeventsapi.model.User;
import es.jocasolo.competitiveeventsapi.service.CommonService;
import es.jocasolo.competitiveeventsapi.service.EventService;

@Service
public class UserMapperImpl implements UserMapper {
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private EventService eventService;
	

	@Override
	public UserLiteWithEventDTO map(User source, Event event) {

		UserLiteWithEventDTO user = new UserLiteWithEventDTO();
		
		user.setId(source.getId());
		user.setName(source.getName());
		user.setSurname(source.getSurname());
		if(source.getAvatar() != null) {
			user.setAvatar(commonService.transform(source.getAvatar(), ImageDTO.class));
		}
		
		// Set data associated to user
		try {
			EventUserDTO eventUser = eventService.findEventAndUser(event.getId(), source.getId());
			user.setIncorporationDate(eventUser.getIncorporationDate());
			user.setLastStatusDate(eventUser.getLastStatusDate());
			
			user.setPrivilege(eventUser.getPrivilege());
			user.setStatus(eventUser.getStatus());
			
		} catch (EventNotFoundException | UserNotFoundException e) {
			e.printStackTrace();
		}
		
		return user;
	}


	@Override
	public Set<UserLiteWithEventDTO> map(Set<User> users, Event event) {
		
		Set<UserLiteWithEventDTO> result = new HashSet<>();
		
		for(User user : users) {
			final UserLiteWithEventDTO userEventDTO = map(user, event);
			result.add(userEventDTO);
		}
		
		return result;
	}


}
