package com.marthym.oikonomos.main.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface AccountTransactionsDataLoadedEventHandler extends EventHandler {
	public void onAccountTransactionsDataLoaded(AccountTransactionsDataLoadedEvent event);
}
