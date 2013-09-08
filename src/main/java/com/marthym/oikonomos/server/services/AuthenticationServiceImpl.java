package com.marthym.oikonomos.server.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.marthym.oikonomos.client.services.AuthenticationService;
import com.marthym.oikonomos.shared.exceptions.OikonomosException;
import com.marthym.oikonomos.shared.exceptions.OikonomosUnauthorizedException;
import com.marthym.oikonomos.shared.model.User;

@Repository
@Service("authenticationService")
public class AuthenticationServiceImpl extends RemoteServiceServlet implements AuthenticationService {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
	
	@Autowired
    protected AuthenticationManager authenticationManager;

	@Override
	@Secured("IS_AUTHENTICATED_ANONYMOUSLY")
	public User authenticate(String username, String password) throws OikonomosException {
		if (password == null || password.length() < 8) {
			throw new OikonomosException("error.message.user.unauthorized", "User not found !");
		}

		try {
			Authentication auth = new UsernamePasswordAuthenticationToken(username, password, null);
			auth = authenticationManager.authenticate(auth);
			SecurityContext sc = SecurityContextHolder.getContext();
			sc.setAuthentication(auth);
			SecurityContextHolder.setContext(sc);
			
			return (User)auth.getPrincipal();
		} catch (Exception e) {
			LOGGER.error(e.getClass()+": "+e.getMessage());
			if (LOGGER.isTraceEnabled()) LOGGER.trace("STACKTRACE", e);
			throw new OikonomosException("error.message.user.unauthorized", e);
		}
	}

	@Override
	public void logout() {
		SecurityContextHolder.clearContext();
	}

	@Override
	@Secured("ROLE_USER")
	public User getAuthentifiedUser() throws OikonomosUnauthorizedException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) throw new OikonomosUnauthorizedException("error.message.user.unauthorized", "No authentification found !");
		User authentifiedUser = (User)authentication.getPrincipal();
		return authentifiedUser;
	}
}
