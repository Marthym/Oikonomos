package com.marthym.oikonomos.server.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import com.marthym.oikonomos.shared.exceptions.OikonomosException;

public class OikonomosExceptionFactory {
	public static final OikonomosException buildOikonomosException(ConstraintViolationException e) {
		ConstraintViolation<?> error = e.getConstraintViolations().iterator().next();
		Map<String, String> errorParameters = new HashMap<String, String>();
		for (Entry<String, Object> attribut : error.getConstraintDescriptor().getAttributes().entrySet()) {
			errorParameters.put(attribut.getKey(), attribut.getValue().toString());
		}
		
		return new OikonomosException(error.getMessage().replace("{", "").replace("}", ""), errorParameters, e.getLocalizedMessage());
	}
}
