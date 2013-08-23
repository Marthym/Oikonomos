package com.marthym.oikonomos.shared.exceptions;

import java.io.Serializable;
import java.util.List;

public class OikonomosRuntimeException extends RuntimeException implements Serializable {
	private static final long serialVersionUID = 1L;
	
	protected String key;
	protected List<String> parameters;

	public OikonomosRuntimeException() {
		super();
	}
	
	public OikonomosRuntimeException(String message) {
		super(message);
	}
	
	public OikonomosRuntimeException(String message, Throwable e) {
		super(message, e);
	}	
}
