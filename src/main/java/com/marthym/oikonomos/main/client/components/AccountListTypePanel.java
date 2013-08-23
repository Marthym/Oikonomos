package com.marthym.oikonomos.main.client.components;

import java.util.List;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Grid;

import com.marthym.oikonomos.main.client.view.EnumTypeTranslator;
import com.marthym.oikonomos.shared.model.Account;
import com.marthym.oikonomos.shared.model.AccountType;

public class AccountListTypePanel extends Composite {
	private DisclosurePanel panel;
	private AccountType type;
	
	public AccountListTypePanel(AccountType type) {
		this.type = type;
		panel = new DisclosurePanel(getTranslation(type));
		initWidget(panel);
	}

	public DisclosurePanel getDisclosurePanel() {
		return panel;
	}

	public void refreshAccountsList(List<Account> accounts) {
		if (accounts == null || accounts.isEmpty()) {
			panel.clear();
			panel.setVisible(false);
			return;
		}
		
		Grid content = new Grid(accounts.size(), 3);
		for (int i = 0; i < accounts.size(); i++) {
			Account account = accounts.get(i);
			int columnNum = 0;
			content.setText(i, columnNum++, account.getAccountName());
			content.setText(i, columnNum++, Double.toString(account.getCurrentAmount()));
			content.setText(i, columnNum++, Double.toString(account.getPointedAmount()));
		}
		panel.setContent(content);
		panel.setVisible(true);
	}

	public final AccountType getAccountType() {return type;}
	
	private String getTranslation(AccountType type) {
		return EnumTypeTranslator.getTranslation(type);
	}
}
