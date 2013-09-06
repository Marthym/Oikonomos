package com.marthym.oikonomos.server.services;

import java.util.Arrays;
import java.util.Date;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.marthym.oikonomos.client.services.AuthenticationService;
import com.marthym.oikonomos.main.client.services.UserService;
import com.marthym.oikonomos.server.repositories.UserRepository;
import com.marthym.oikonomos.shared.exceptions.OikonomosException;
import com.marthym.oikonomos.shared.exceptions.OikonomosUnauthorizedException;
import com.marthym.oikonomos.shared.model.User;
import com.marthym.oikonomos.shared.model.UserProfile;

@Repository
@Service("userService")
public class UserServiceImpl extends RemoteServiceServlet implements UserService {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AuthenticationService authentificationService;

	@Override
	public User findUserByMail(String userMail) {
		return userRepository.findByUserEmail(userMail);
	}
	
	@Override
	public User findUserById(Long userId) {
		return userRepository.findOne(userId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public User saveUser(String userMail, String firstName, String lastName, String password, Date registration, Date lastlogin) throws OikonomosException {
		try {
			String hashPassword = BCrypt.hashpw(password, BCrypt.gensalt());
			User user = new User(userMail, firstName, lastName, hashPassword);
			return userRepository.saveAndFlush(user);
			
		} catch (Exception e){
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(e.getClass()+": "+e.getLocalizedMessage());
				LOGGER.trace("STACKTRACE", e);
			}
			throw new OikonomosException("error.message.user.unique", Arrays.asList(new String[]{userMail}), "");
		}
	}

	@Override
	@Secured("ROLE_USER")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public User updateUser(String userMail, String firstName, String lastName, String password, Date lastlogin) throws OikonomosException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) throw new OikonomosUnauthorizedException("error.message.user.unauthorized", "No authentification found !");
		User authentifiedUser = (User)authentication.getPrincipal();
		LOGGER.debug("Authentified user {}, id {}", authentifiedUser.getUserEmail(), authentifiedUser.getUserId());
		
		User user = userRepository.findByUserEmail(userMail);
		if (user == null) {
			LOGGER.warn("User {} not find in database ! Clear session !", userMail);
			SecurityContextHolder.clearContext();
			throw new OikonomosException("error.message.user.unauthorizedAction", "User {} not find in database ! Clear session !");
		}
		LOGGER.debug("Find user {}, id {}", user.getUserEmail(), user.getUserId());

		if (authentifiedUser.getUserId() != user.getUserId() && authentifiedUser.getUserProfile() != UserProfile.ADMIN) {
			LOGGER.error("Only ADMIN can modify other users !");
			throw new OikonomosUnauthorizedException("error.message.user.unauthorizedAction", "Only ADMIN can modify other users !");
		}
		
		if (user != null) {
			user.setUserFirstname(firstName);
			user.setUserLastname(lastName);
			if (password != null) {
				String hashPassword = BCrypt.hashpw(password, BCrypt.gensalt());
				user.setUserPassword(hashPassword);
			}
			user.setUserLastLoginDate(lastlogin);
			user = userRepository.save(user);
			LOGGER.debug("User saved successfully !");
		}
		
		// Update current security context
		if (authentifiedUser.getUserId() == user.getUserId()) {
			LOGGER.debug("Update spring security context ...");
			Authentication auth = new UsernamePasswordAuthenticationToken(user, password, authentication.getAuthorities());
			SecurityContext sc = new SecurityContextImpl();
			sc.setAuthentication(auth);
			SecurityContextHolder.setContext(sc);
			LOGGER.debug("... context updated !");
		}
		return user;
	}

	@Override
	@Secured("ROLE_USER")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deleteUser(String userMail) throws OikonomosException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) throw new OikonomosUnauthorizedException("error.message.user.unauthorized", "No authentification found !");
		User authentifiedUser = (User)authentication.getPrincipal();

		User user = userRepository.findByUserEmail(userMail);

		if (authentifiedUser.getUserId() != user.getUserId() && authentifiedUser.getUserProfile() != UserProfile.ADMIN) {
			throw new OikonomosUnauthorizedException("error.message.user.unauthorizedAction", "Only ADMIN can modify other users !");
		}
		
		if (user != null)
			userRepository.delete(user);
		
		if (authentifiedUser.getUserId() == user.getUserId()) {
			authentificationService.logout();
		}
	}

}
