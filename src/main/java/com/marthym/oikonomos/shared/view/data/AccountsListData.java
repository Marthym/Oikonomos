package com.marthym.oikonomos.shared.view.data;

import java.io.Serializable;
import java.util.List;

import com.marthym.oikonomos.main.client.presenter.DashboardPresenterFactory.ContentPanelType;
import com.marthym.oikonomos.shared.model.Account;

public class AccountsListData extends ContentPanelData implements Serializable {
	private static final long serialVersionUID = 191208960777541273L;
	private List<Account> accounts;
	
	@Deprecated
	public AccountsListData() {super(ContentPanelType.ACCOUNTS);};
	public AccountsListData(List<Account> accounts){
		super(ContentPanelType.ACCOUNTS);
		this.accounts = accounts;
	}
	
	public final List<Account> getAccountsList() { return this.accounts;}
}
