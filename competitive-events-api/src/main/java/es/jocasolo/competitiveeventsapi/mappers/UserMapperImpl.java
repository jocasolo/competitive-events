package es.jocasolo.competitiveeventsapi.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.jocasolo.competitiveeventsapi.dto.image.ImageDTO;
import es.jocasolo.competitiveeventsapi.dto.user.UserCompleteDTO;
import es.jocasolo.competitiveeventsapi.model.User;
import es.jocasolo.competitiveeventsapi.service.CommonService;

@Service
public class UserMapperImpl implements UserMapper {
	
	@Autowired
	private CommonService commonService;

	@Override
	public UserCompleteDTO map(User source) {
		UserCompleteDTO user = new UserCompleteDTO();
		
		user.setBirthDate(source.getBirthDate());
		user.setDescription(source.getDescription());
		user.setEmail(source.getEmail());
		user.setId(source.getId());
		user.setName(source.getName());
		user.setRegisterDate(source.getRegisterDate());
		user.setSurname(source.getSurname());
		user.setAvatar(commonService.transform(source.getAvatar(), ImageDTO.class));
		
		return user;
	}
	
	

}
