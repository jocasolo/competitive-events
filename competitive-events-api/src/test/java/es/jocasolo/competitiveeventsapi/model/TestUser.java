package es.jocasolo.competitiveeventsapi.model;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.GrantedAuthority;

import es.jocasolo.competitiveeventsapi.enums.user.UserStatusType;
import es.jocasolo.competitiveeventsapi.enums.user.UserType;

@RunWith(MockitoJUnitRunner.class)
class TestUser {
	
	private User user = new User();
	
	@BeforeEach
	void init() {
		
		MockitoAnnotations.openMocks(this);
		
		Image image = new Image();
		image.setId(1);
		
		user.setId("user");
		user.setAvatar(image);
		user.setConfirmKey("123");
		user.setImages(List.of(image));
		user.setPhone("123456789");
		user.setType(UserType.NORMAL);
		user.setEvents(Set.of(new Event()));
		user.setStatus(UserStatusType.DELETED);
		
	}
	
	 @SuppressWarnings("unlikely-arg-type")
	 @Test
	 void testEquals() {
		 assertTrue(user.equals(user));
		 assertFalse(user.equals(new User()));
		 assertFalse(user.equals(null));
		 assertFalse(user.equals(new Event()));
	 }
	 
	 @Test
	 void testHashCode() {
		 assertNotEquals(0, user.hashCode());
	 }
	 
	 @Test
	 void testGetters() {
		 Date now = new Date();
		 user.setRegisterDate(now);
		 assertEquals("123456789", user.getPhone());
		 assertEquals("User [id=user]", user.toString());
		 assertEquals(UserType.NORMAL, user.getType());
		 assertEquals(now, user.getRegisterDate());
		 assertFalse(user.getEvents().isEmpty());
		 assertEquals("123", user.getConfirmKey());
		 assertFalse(user.getImages().isEmpty());
		 assertTrue(user.isAccountNonExpired());
		 assertFalse(user.isAccountNonLocked());
		 assertFalse(user.isEnabled());
		 user.setStatus(UserStatusType.ACTIVE);
		 assertTrue(user.isAccountNonLocked());
		 assertTrue(user.isCredentialsNonExpired());
		 assertEquals("user", user.getUsername());
	 }
	 
	 @Test
	 void testGetAuthorities() {
		 List<GrantedAuthority> authorities = (List<GrantedAuthority>) user.getAuthorities();
		 assertNotNull(authorities);
	 }
	 
}
