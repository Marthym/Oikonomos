package com.marthym.oikonomos.login.client.services;


import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.marthym.oikonomos.shared.exceptions.OikonomosException;
import com.marthym.oikonomos.shared.model.User;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("../rpc/authenticationService")
public interface AuthenticationService extends RemoteService {
	
	public User authenticate(String username, String password) throws OikonomosException;
	public void logout();

}
