package com.marthym.oikonomos.client.services;


import org.springframework.security.access.annotation.Secured;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.marthym.oikonomos.shared.exceptions.OikonomosException;
import com.marthym.oikonomos.shared.exceptions.OikonomosUnathorizedException;
import com.marthym.oikonomos.shared.model.User;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("../rpc/authenticationService")
public interface AuthenticationService extends RemoteService {
	
	@Secured("IS_AUTHENTICATED_ANONYMOUSLY")
	public User authenticate(String username, String password) throws OikonomosException;
	public void logout();
	
	@Secured("ROLE_USER")
	public User getAuthentifiedUser() throws OikonomosUnathorizedException;
}
