package com.marthym.oikonomos.main.client.components;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLPanel;

import com.marthym.oikonomos.client.i18n.OikonomosConstants;
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
			panel.setContent(new HTMLPanel("No account for "+getTranslation(this.type)));
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
	}

	private String getTranslation(AccountType type) {
		OikonomosConstants traduction = GWT.create(OikonomosConstants.class);
		switch (type) {
		case BANK_ACCOUNT:
			return traduction.bankAccount();
			
		case ASSET_ACCOUNT:
			return traduction.assetAccount();
			
		case LIABILITY_ACCOUNT:
			return traduction.liabilityAccount();
			
		case CASH_ACCOUNT:
			return traduction.cashAccount();
		}
		
		return "";
	}
}
