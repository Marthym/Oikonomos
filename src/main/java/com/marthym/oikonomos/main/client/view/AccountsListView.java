package com.marthym.oikonomos.main.client.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import com.marthym.oikonomos.main.client.components.AccountListTypePanel;
import com.marthym.oikonomos.main.client.presenter.AccountsListPresenter;
import com.marthym.oikonomos.shared.model.Account;
import com.marthym.oikonomos.shared.model.AccountType;

public class AccountsListView extends Composite implements AccountsListPresenter.Display {
	private Map<AccountType, AccountListTypePanel> accountsTypePanels;
	
	public AccountsListView() {
		VerticalPanel panel = new VerticalPanel();
		accountsTypePanels = new HashMap<AccountType, AccountListTypePanel>();
		for (AccountType type : AccountType.values()) {
			AccountListTypePanel accountTypePanel = new AccountListTypePanel(type);
			panel.add(accountTypePanel);
			accountsTypePanels.put(type, accountTypePanel);
		}
		
		initWidget(panel);
	}

	@Override
	public DisclosurePanel getDisclosurePanel(AccountType entity) {
		return accountsTypePanels.get(entity).getDisclosurePanel();
	}

	@Override
	public void refreshAccountsTypePanel(AccountType type, List<Account> datas) {
		accountsTypePanels.get(type).refreshAccountsList(datas);
	}

}
