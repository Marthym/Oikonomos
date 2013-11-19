package com.marthym.oikonomos.shared.exceptions;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.regexp.shared.RegExp;
import com.marthym.oikonomos.client.i18n.OikonomosErrorMessages;
import com.marthym.oikonomos.client.i18n.ValidatorMessages;

public class OikonomosException extends Exception implements Serializable {
	private static final long serialVersionUID = 1L;
	
	protected String key;
	protected Map<String, String> parameters;

	public OikonomosException() {
		super();
	}
	
	public OikonomosException(String key, String serverMessage) {
		super(serverMessage);
		this.key = key.replaceAll("\\.+","_");
		this.parameters = new HashMap<String, String>();
	}
	
	public OikonomosException(String key, List<String> parameters, String serverMessage) {
		super(serverMessage);
		this.key = key.replaceAll("\\.+","_");
		this.parameters = readParameters(parameters);
	}
	
	public OikonomosException(String key, Map<String, String> parameters, String serverMessage) {
		super(serverMessage);
		this.key = key.replaceAll("\\.+","_");
		this.parameters = parameters;
	}
	
	public OikonomosException(String key, List<String> parameters, Throwable e) {
		super(e.getMessage(), e);
		this.key = key.replaceAll("\\.+","_");
		this.parameters = readParameters(parameters);
	}

	public OikonomosException(String key, Throwable e) {
		super(e.getMessage(), e);
		this.key = key.replaceAll("\\.+","_");
		this.parameters = new HashMap<String, String>();
	}
	
	@Override
	public String getLocalizedMessage() {
		final OikonomosErrorMessages messages = GWT.create(OikonomosErrorMessages.class);
		
		String localizedMessage = "";
		try { localizedMessage = messages.getString(key);
		} catch (Exception e) {}
		
		if (localizedMessage.isEmpty()) {
			final ValidatorMessages validationMessages = GWT.create(ValidatorMessages.class);
			try {localizedMessage = validationMessages.getString(key);}
			catch (Exception e) {}
		}
		
		for (Entry<String, String> param : parameters.entrySet()) {
			RegExp pattern = RegExp.compile("\\{"+param.getKey()+"\\}");
			localizedMessage = pattern.replace(localizedMessage, param.getValue());
		}
		return localizedMessage;
	}
	
	private static final Map<String, String> readParameters(List<String> parameters) {
		Map<String, String> params = new HashMap<String, String>();
		for (int i=0; i < parameters.size(); i++) {
			params.put(Integer.toString(i), parameters.get(i));
		}
		return params;
	}
	
}
