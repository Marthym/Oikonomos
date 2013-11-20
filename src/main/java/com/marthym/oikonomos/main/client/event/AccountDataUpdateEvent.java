package com.marthym.oikonomos.main.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.marthym.oikonomos.shared.model.Account;

public class AccountDataUpdateEvent extends GwtEvent<AccountDataUpdateEventHandler> {
	public static Type<AccountDataUpdateEventHandler> TYPE = new Type<AccountDataUpdateEventHandler>();

	private final Account account;
	public AccountDataUpdateEvent(Account account) {
		this.account = account;
	}
	
	@Override
	public Type<AccountDataUpdateEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(AccountDataUpdateEventHandler handler) {
		handler.onAccountDataUpdate(this);
	}
	
	public final Account getAccount() {
		return account;
	}
}
