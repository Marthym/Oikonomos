package com.marthym.oikonomos.main.client.services;

import java.util.Date;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.marthym.oikonomos.shared.exceptions.OikonomosException;
import com.marthym.oikonomos.shared.model.User;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("../rpc/userService")
public interface UserService extends RemoteService {
	
	public User findUserByMail(String userMail);
	public User findUserById(Long userId);
	public User saveUser(String userMail, String firstName, String lastName, String password, Date registration, Date lastlogin) throws OikonomosException;
	public User updateUser(String userMail, String firstName, String lastName, String password, Date lastlogin) throws OikonomosException;
	public void deleteUser(String userId) throws OikonomosException;

}
