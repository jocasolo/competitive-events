package es.jocasolo.competitiveeventsapi.mappers;

import es.jocasolo.competitiveeventsapi.dto.user.UserCompleteDTO;
import es.jocasolo.competitiveeventsapi.model.User;

public interface UserMapper {
	
	UserCompleteDTO map(User source);
	
}
