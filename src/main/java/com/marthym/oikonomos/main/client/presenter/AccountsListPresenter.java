package com.marthym.oikonomos.main.client.presenter;

import java.util.List;

import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.marthym.oikonomos.client.presenter.Presenter;
import com.marthym.oikonomos.shared.model.Account;
import com.marthym.oikonomos.shared.model.AccountType;
import com.marthym.oikonomos.shared.view.data.AccountsListData;

public class AccountsListPresenter implements Presenter {
	public interface Display {
		Widget asWidget();

		DisclosurePanel getDisclosurePanel(AccountType entity);
		void refreshAccountsTypePanel(AccountType type, List<Account> datas);
	}
	
	private final Display display;

	public AccountsListPresenter(Display display, AccountsListData datas) {
		this.display = display;
		bind(datas);
	}
	
	private void bind(AccountsListData datas) {
		for (AccountType type : AccountType.values()) {
			display.refreshAccountsTypePanel(type, datas.getAccountsList(type));
		}
	}
	
	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(display.asWidget());
	}

}
