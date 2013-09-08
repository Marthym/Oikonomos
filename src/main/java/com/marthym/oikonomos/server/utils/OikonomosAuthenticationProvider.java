package com.marthym.oikonomos.server.utils;

import java.util.LinkedList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.marthym.oikonomos.server.repositories.UserRepository;
import com.marthym.oikonomos.shared.model.User;
import com.marthym.oikonomos.shared.model.UserProfile;

public class OikonomosAuthenticationProvider implements AuthenticationProvider {
	private static final Logger LOGGER = LoggerFactory.getLogger(OikonomosAuthenticationProvider.class);

	@Autowired
	private UserRepository userRepository;
	
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;
		String username = String.valueOf(auth.getPrincipal());
		String password = String.valueOf(auth.getCredentials());

		User currentUser = null;
		
		long count = userRepository.count();
		if (count == 0) {
			LOGGER.warn("First user login ! Create administrateur : "+username);
			String hashPassword = BCrypt.hashpw(password, BCrypt.gensalt());
			currentUser = new User(username, "", username.split("@")[0], hashPassword);
			currentUser.setUserProfile(UserProfile.ADMIN);
			currentUser = userRepository.saveAndFlush(currentUser);
		} else {
			currentUser = userRepository.findByUserEmail(username);
		}
		
		if (currentUser == null || !BCrypt.checkpw(password, currentUser.getUserHashPassword())) 
			throw new BadCredentialsException("User not found !");
		LOGGER.info("Connect to "+currentUser.getUserEmail()+" ...");

		currentUser.setUserPassword("");

		List<GrantedAuthority> authorities = new LinkedList<GrantedAuthority>();
		switch (currentUser.getUserProfile()) {
		case ADMIN:
			authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		case USER:
			authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
			break;
		default:
			throw new BadCredentialsException("User not found !");
		}

		return new UsernamePasswordAuthenticationToken(currentUser, null, authorities);
	}

	public boolean supports(Class<?> authentication) {

		return authentication.equals(UsernamePasswordAuthenticationToken.class);

	}
}
