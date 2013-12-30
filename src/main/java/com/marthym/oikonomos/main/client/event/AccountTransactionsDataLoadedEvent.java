package com.marthym.oikonomos.main.client.event;

import java.util.List;

import com.google.gwt.event.shared.GwtEvent;
import com.marthym.oikonomos.shared.model.dto.TransactionDTO;

public class AccountTransactionsDataLoadedEvent extends GwtEvent<AccountTransactionsDataLoadedEventHandler> {
	public static Type<AccountTransactionsDataLoadedEventHandler> TYPE = new Type<AccountTransactionsDataLoadedEventHandler>();

	private final List<TransactionDTO> transactions;
	private final boolean isDeleted;
	
	public AccountTransactionsDataLoadedEvent(List<TransactionDTO> transactions) {
		this.transactions = transactions;
		this.isDeleted = false;
	}
	
	public AccountTransactionsDataLoadedEvent(List<TransactionDTO> transactions, boolean isDeleted) {
		this.transactions = transactions;
		this.isDeleted = isDeleted;
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
	
	public final boolean isDeleted() {
		return isDeleted;
	}
}
