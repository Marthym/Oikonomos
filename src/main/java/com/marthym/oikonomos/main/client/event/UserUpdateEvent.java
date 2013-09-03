package com.marthym.oikonomos.main.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.marthym.oikonomos.shared.model.User;

public class UserUpdateEvent extends GwtEvent<UserUpdateEventHandler> {
	public static Type<UserUpdateEventHandler> TYPE = new Type<UserUpdateEventHandler>();

	private final User user;
	public UserUpdateEvent(User user) {
		this.user = user;
	}
	
	@Override
	public Type<UserUpdateEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(UserUpdateEventHandler handler) {
		handler.onUserUpdate(this);
	}
	
	public final User getUser() {
		return user;
	}
}
