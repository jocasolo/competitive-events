package es.jocasolo.competitiveeventsapi.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import es.jocasolo.competitiveeventsapi.dto.image.ImageDTO;
import es.jocasolo.competitiveeventsapi.dto.user.UserDTO;

@RunWith(MockitoJUnitRunner.class)
class TestUserDTO {

	private ImageDTO image = new ImageDTO();
	private UserDTO user = new UserDTO();

	@BeforeEach
	void init() {

		MockitoAnnotations.openMocks(this);

		image.setId(1);
		
		user.setId("user");
		user.setSurname("surname");
		user.setDescription("description");
		user.setAvatar(image);
		
	}

	@Test
	void testCommon() {

		Date now = new Date();	
		user.setRegisterDate(now);
		user.setBirthDate(now);
		
		assertEquals("surname", user.getSurname());
		assertEquals("description", user.getDescription());
		assertEquals(image, user.getAvatar());
		assertEquals(now, user.getRegisterDate());
		assertEquals(now, user.getBirthDate());
		assertEquals("UserDTO [id=user]", user.toString());
		
	}

}
