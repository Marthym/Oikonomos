package com.marthym.oikonomos.server.services;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.marthym.oikonomos.client.services.UserService;
import com.marthym.oikonomos.server.dao.IGenericDao;
import com.marthym.oikonomos.shared.dto.UserDTO;

@Service("userService")
public class UserServiceImpl extends RemoteServiceServlet implements UserService{
	private static final long serialVersionUID = 1L;

	private IGenericDao<String, UserDTO> userDAO;
	
	@Autowired
	public void setUserDAO (IGenericDao<String, UserDTO> daoToSet) {
		userDAO = daoToSet;
		userDAO.setClazz(UserDTO.class);
	}

	@PostConstruct
	public void init() {
	}

	@PreDestroy
	public void destroy() {
	}

	@Override
	public UserDTO findUser(String userId) {
		return userDAO.findById(userId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveUser(String userId, String firstName, String lastName, String password, Date registration, Date lastlogin) throws Exception {
		UserDTO userDTO = userDAO.findById(userId);
		if (userDTO == null) {
			userDTO = new UserDTO(userId, firstName, lastName, password, registration, lastlogin);
			userDAO.create(userDTO);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateUser(String userId, String firstName, String lastName, String password, Date lastlogin) throws Exception {
		UserDTO userDTO = userDAO.findById(userId);
		if (userDTO != null) {
			userDTO.setUserFirstname(firstName);
			userDTO.setUserLastname(lastName);
			userDTO.setUserPassword(password);
			userDTO.setUserLastLoginDate(lastlogin);
			userDAO.update(userDTO);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deleteUser(String employeeId) throws Exception {
		UserDTO userDTO = userDAO.findById(employeeId);
		if (userDTO != null)
			userDAO.delete(userDTO);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveOrUpdateUser(String userId, String firstName, String lastName, String password, Date registration, Date lastlogin) throws Exception {
		UserDTO userDTO = new UserDTO(userId, firstName, lastName, password, registration, lastlogin);
		userDAO.update(userDTO);
	}

}
