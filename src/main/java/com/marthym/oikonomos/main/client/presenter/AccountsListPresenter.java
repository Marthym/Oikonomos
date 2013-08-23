package com.marthym.oikonomos.main.client.presenter;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.marthym.oikonomos.client.components.MessageFlyer;
import com.marthym.oikonomos.client.components.WaitingFlyer;
import com.marthym.oikonomos.client.presenter.Presenter;
import com.marthym.oikonomos.main.client.services.AccountServiceAsync;
import com.marthym.oikonomos.main.client.view.AccountsListView;
import com.marthym.oikonomos.shared.model.Account;
import com.marthym.oikonomos.shared.model.AccountType;

public class AccountsListPresenter implements Presenter {
	public interface Display {
		Widget asWidget();

		DisclosurePanel getDisclosurePanel(AccountType entity);
		void refreshAccountsTypePanel(List<Account> datas);
	}
	
	private final Display display;
	private static AccountsListPresenter instance = null;
	private static AccountServiceAsync RCP_ACCOUNT_SERVICE = AccountServiceAsync.Util.getInstance();

	private AccountsListPresenter() {
		this.display = new AccountsListView();
	}

	public static final void create(final Callback callback) {
		if (instance == null) {
			instance = new AccountsListPresenter();
		}
		RCP_ACCOUNT_SERVICE.getList(true, new AsyncCallback<List<Account>>() {
			
			@Override
			public void onSuccess(List<Account> result) {
				instance.updateViewFromData(result);
				callback.onCreate(instance);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				WaitingFlyer.stop();
				MessageFlyer.error(caught.getLocalizedMessage());
			}
		});		
	}
	
	private void updateViewFromData(List<Account> datas) {
		display.refreshAccountsTypePanel(datas);
	}
	
	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(display.asWidget());
	}

}
