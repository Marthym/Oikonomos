package com.marthym.oikonomos.server.services;

import java.util.Arrays;
import java.util.Date;

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
public class UserServiceImpl extends RemoteServiceServlet implements UserService{
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepository userRepository;

	@Override
	public User findUser(String userId) {
		return userRepository.findOne(userId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveUser(String userId, String firstName, String lastName, String password, Date registration, Date lastlogin) throws Exception {
		User user = userRepository.findOne(userId);
		if (user == null) {
			user = new User(userId, firstName, lastName, password);
			user = userRepository.saveAndFlush(user);
		}
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveUser(User user) throws OikonomosException {
		try {
			userRepository.saveAndFlush(user);
		} catch (Exception e){
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(e.getClass()+": "+e.getLocalizedMessage());
				LOGGER.trace("STACKTRACE", e);
			}
			throw new OikonomosException("error.message.user.unique", Arrays.asList(new String[]{user.getUserEmail()}), "");
		}
	}

	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateUser(String userId, String firstName, String lastName, String password, Date lastlogin) throws Exception {
		User user = userRepository.findOne(userId);
		if (user != null) {
			user.setUserFirstname(firstName);
			user.setUserLastname(lastName);
			user.setUserPassword(password);
			user.setUserLastLoginDate(lastlogin);
			userRepository.save(user);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deleteUser(String employeeId) throws Exception {
		User user = userRepository.findOne(employeeId);
		if (user != null)
			userRepository.delete(user);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveOrUpdateUser(String userId, String firstName, String lastName, String password, Date registration, Date lastlogin) throws Exception {
		User user = new User(userId, firstName, lastName, password);
		userRepository.save(user);
	}

}
