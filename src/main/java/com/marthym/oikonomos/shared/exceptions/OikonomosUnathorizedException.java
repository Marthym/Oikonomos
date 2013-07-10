package com.marthym.oikonomos.shared.exceptions;

import java.io.Serializable;
import java.util.List;

public class OikonomosUnathorizedException extends OikonomosException implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public OikonomosUnathorizedException() {
		super();
	}
	
	public OikonomosUnathorizedException(String key, String serverMessage) {
		super(key, serverMessage);
	}
	
	public OikonomosUnathorizedException(String key, List<String> parameters, String serverMessage) {
		super(key, parameters, serverMessage);
	}
	
	public OikonomosUnathorizedException(String key, List<String> parameters, Throwable e) {
		super(key, parameters, e);
	}

}
