package com.marthym.oikonomos.main.client.event;

import java.util.List;

import com.google.gwt.event.shared.GwtEvent;
import com.marthym.oikonomos.shared.model.dto.TransactionDTO;

public class AccountTransactionsDataLoadedEvent extends GwtEvent<AccountTransactionsDataLoadedEventHandler> {
	public static Type<AccountTransactionsDataLoadedEventHandler> TYPE = new Type<AccountTransactionsDataLoadedEventHandler>();

	private final List<TransactionDTO> transactions;
	public AccountTransactionsDataLoadedEvent(List<TransactionDTO> transactions) {
		this.transactions = transactions;
	}
	
	@Override
	public Type<AccountTransactionsDataLoadedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(AccountTransactionsDataLoadedEventHandler handler) {
		handler.onAccountTransactionsDataLoaded(this);
	}
	
	public final List<TransactionDTO> getTransactions() {
		return transactions;
	}
}
