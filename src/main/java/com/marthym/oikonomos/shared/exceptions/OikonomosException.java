package com.marthym.oikonomos.shared.exceptions;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.regexp.shared.RegExp;
import com.marthym.oikonomos.client.i18n.OikonomosErrorMessages;

public class OikonomosException extends Exception implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String key;
	private List<String> parameters;

	public OikonomosException() {
		super();
	}
	
	public OikonomosException(String key, String serverMessage) {
		super(serverMessage);
		this.key = key.replaceAll("\\.+","_");
		this.parameters = new LinkedList<String>();
	}
	
	public OikonomosException(String key, List<String> parameters, String serverMessage) {
		super(serverMessage);
		this.key = key.replaceAll("\\.+","_");
		this.parameters = parameters;
	}
	
	public OikonomosException(String key, List<String> parameters, Throwable e) {
		super(e.getMessage(), e);
		this.key = key.replaceAll("\\.+","_");
		this.parameters = parameters;
	}


	@Override
	public String getLocalizedMessage() {
		final OikonomosErrorMessages messages = GWT.create(OikonomosErrorMessages.class);
		String localizedMessage = messages.getString(key);
		for (int i = 0; i < parameters.size(); i++) {
			RegExp pattern = RegExp.compile("\\{"+i+"\\}");
			localizedMessage = pattern.replace(localizedMessage, parameters.get(i));
		}
		return localizedMessage;
	}
	
	
}
