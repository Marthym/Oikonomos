package com.marthym.oikonomos.shared.exceptions;

import java.io.Serializable;
import java.util.List;

public class OikonomosUnauthorizedException extends OikonomosException implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public OikonomosUnauthorizedException() {
		super();
	}
	
	public OikonomosUnauthorizedException(String key, String serverMessage) {
		super(key, serverMessage);
	}
	
	public OikonomosUnauthorizedException(String key, List<String> parameters, String serverMessage) {
		super(key, parameters, serverMessage);
	}
	
	public OikonomosUnauthorizedException(String key, List<String> parameters, Throwable e) {
		super(key, parameters, e);
	}

}
