package com.marthym.oikonomos.server.services;

import java.util.HashMap;
import java.util.Map;

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
import com.marthym.oikonomos.shared.exceptions.OikonomosException;
import com.marthym.oikonomos.shared.exceptions.OikonomosUnauthorizedException;
import com.marthym.oikonomos.shared.model.User;
import com.marthym.oikonomos.shared.services.AuthenticationService;

@Repository
@Service("authenticationService")
public class AuthenticationServiceImpl extends RemoteServiceServlet implements AuthenticationService {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
	
	@Autowired
    protected AuthenticationManager authenticationManager;

	@Override
	@Secured("IS_AUTHENTICATED_ANONYMOUSLY")
	public User authenticate(String username, String password, String locale) throws OikonomosException {
		if (password == null || password.length() < 8) {
			throw new OikonomosException("error.message.user.unauthorized", "User not found !");
		}
		Map<String, String> details = new HashMap<String, String>();
		if (locale != null && !locale.isEmpty()) details.put(SESSION_DETAIL_LOCALE, locale);
		else details.put(SESSION_DETAIL_LOCALE, "us");
		try {
			Authentication auth = new UsernamePasswordAuthenticationToken(username, password);
			((UsernamePasswordAuthenticationToken)auth).setDetails(details);
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
