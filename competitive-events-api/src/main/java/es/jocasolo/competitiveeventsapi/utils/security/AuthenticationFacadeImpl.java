package es.jocasolo.competitiveeventsapi.utils.security;

import org.springframework.security.core.Authentication;

import es.jocasolo.competitiveeventsapi.model.user.User;

public interface AuthenticationFacadeImpl {
    
	Authentication getAuthentication();
   
	User getUser();
}
