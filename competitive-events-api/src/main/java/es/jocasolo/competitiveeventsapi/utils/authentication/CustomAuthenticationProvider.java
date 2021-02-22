package es.jocasolo.competitiveeventsapi.utils.authentication;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import es.jocasolo.competitiveeventsapi.dao.UserDAO;
import es.jocasolo.competitiveeventsapi.enums.user.UserStatusType;
import es.jocasolo.competitiveeventsapi.model.user.User;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
	
	@Autowired
	private UserDAO userDao;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		String username = authentication.getName();
		String password = authentication.getCredentials().toString();

		User user = userDao.findOne(username);
	    if (user == null) {
	        throw new BadCredentialsException("Invalid username or password.");
	    }
	    if (!passwordEncoder.matches(password, user.getPassword())) {
	        throw new BadCredentialsException("Invalid username or password.");
	    }
	    if (user.getStatus().equals(UserStatusType.DISABLED) || user.getStatus().equals(UserStatusType.DELETED)) {
	        throw new DisabledException("User does not exists.");
	    }
	    
	    List<GrantedAuthority> roles = new ArrayList<>();
	    roles.add(new SimpleGrantedAuthority(user.getType().name()));
	    
	    return new UsernamePasswordAuthenticationToken(username, password, roles);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}