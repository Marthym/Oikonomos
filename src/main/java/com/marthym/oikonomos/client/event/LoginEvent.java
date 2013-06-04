package com.marthym.oikonomos.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class LoginEvent extends GwtEvent<LoginEventHandler> {
	public static Type<LoginEventHandler> TYPE = new Type<LoginEventHandler>();

	private final String userName;
	private final String userPassword;
	
	public LoginEvent(String userName, String userPassword) {
		this.userName = userName;
		this.userPassword = userPassword;
	}
	
	@Override
	public Type<LoginEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(LoginEventHandler handler) {
		handler.onLogin(this);
	}

	public String getUserName() {
		return userName;
	}
	
	public String getUserPassword() {
		return userPassword;
	}
}
