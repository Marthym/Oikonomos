package com.marthym.oikonomos.shared.view.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.marthym.oikonomos.main.client.presenter.DashboardPresenterFactory.ContentPanelType;
import com.marthym.oikonomos.shared.model.Account;
import com.marthym.oikonomos.shared.model.AccountType;

public class AccountsListData extends ContentPanelData implements Serializable {
	private static final long serialVersionUID = 191208960777541273L;
	private Map<AccountType, List<Account>> accounts;
	
	/**
	 * @Deprecated Use only for serialization issue
	 */
	@Deprecated
	public AccountsListData() {super(ContentPanelType.ACCOUNTS);};
	public AccountsListData(List<Account> accounts){
		super(ContentPanelType.ACCOUNTS);
		this.accounts = new HashMap<AccountType, List<Account>>();
		
		for (Account anAccount : accounts) {
			AccountType accountType = anAccount.getAccountType();
			List<Account> listAccountByType = this.accounts.get(accountType);
			if (listAccountByType == null) {
				listAccountByType = new ArrayList<Account>();
				this.accounts.put(accountType, listAccountByType);
			}
			listAccountByType.add(anAccount);
		}
	}
	
	public final List<Account> getAccountsList(AccountType type) { return this.accounts.get(type);}
}
