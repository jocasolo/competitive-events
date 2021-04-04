package es.jocasolo.competitiveeventsapi.utils.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import es.jocasolo.competitiveeventsapi.dao.UserDAO;
import es.jocasolo.competitiveeventsapi.model.User;

@Component
public class AuthenticationFacade implements AuthenticationFacadeImpl {
	
	@Autowired
	private UserDAO userDao;
	
    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

	@Override
	public User getUser() {
		return userDao.findOne(getAuthentication().getName());
	}
}