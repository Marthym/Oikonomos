package com.marthym.oikonomos.server.services;

import java.util.Arrays;
import java.util.Date;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.marthym.oikonomos.client.services.UserService;
import com.marthym.oikonomos.server.repositories.UserRepository;
import com.marthym.oikonomos.shared.exceptions.OikonomosException;
import com.marthym.oikonomos.shared.model.User;

@Repository
@Service("userService")
public class UserServiceImpl extends RemoteServiceServlet implements UserService {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepository userRepository;

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
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public User updateUser(String userId, String firstName, String lastName, String password, Date lastlogin) throws OikonomosException {
		User user = userRepository.findByUserEmail(userId);
		if (user != null) {
			user.setUserFirstname(firstName);
			user.setUserLastname(lastName);
			String hashPassword = BCrypt.hashpw(password, BCrypt.gensalt());
			user.setUserPassword(hashPassword);
			user.setUserLastLoginDate(lastlogin);
			user = userRepository.save(user);
		}
		return user;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deleteUser(String userMail) throws OikonomosException {
		User user = userRepository.findByUserEmail(userMail);
		if (user != null)
			userRepository.delete(user);
	}

}
