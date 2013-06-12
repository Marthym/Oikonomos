package com.marthym.oikonomos.main.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import com.marthym.oikonomos.main.client.presenter.AccountsListPresenter;

public class AccountsListView extends Composite implements AccountsListPresenter.Display {
	private static AccountsListViewUiBinder uiBinder = GWT.create(AccountsListViewUiBinder.class);
	interface AccountsListViewUiBinder extends UiBinder<Widget, AccountsListView> {}
	
	public AccountsListView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
