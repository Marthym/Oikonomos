package com.marthym.oikonomos.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface LoginEventHandler extends EventHandler {
	String onLogin(LoginEvent event);
}
