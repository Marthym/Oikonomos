package com.marthym.oikonomos.main.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface LogoutEventHandler extends EventHandler {
	public void onLogout(LogoutEvent event);
}
