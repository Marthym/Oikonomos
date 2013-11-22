package com.marthym.oikonomos.shared.view.data;

import java.io.Serializable;
import java.util.List;

import com.marthym.oikonomos.shared.model.Account;
import com.marthym.oikonomos.shared.model.dto.TransactionDTO;

public class AccountTabbedData implements Serializable {
	private static final long serialVersionUID = 1L;

	private Account account;
	private List<TransactionDTO> transactions;
	
	public final Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public final List<TransactionDTO> getTransactions() {
		return transactions;
	}
	public void setTransactions(List<TransactionDTO> transactions) {
		this.transactions = transactions;
	}
}
