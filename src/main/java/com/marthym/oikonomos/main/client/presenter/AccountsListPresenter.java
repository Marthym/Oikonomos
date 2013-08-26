package com.marthym.oikonomos.main.client.presenter;

import java.util.List;

import javax.inject.Inject;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.marthym.oikonomos.client.components.MessageFlyer;
import com.marthym.oikonomos.client.components.WaitingFlyer;
import com.marthym.oikonomos.client.presenter.Presenter;
import com.marthym.oikonomos.main.client.NomosInjector;
import com.marthym.oikonomos.main.client.services.AccountServiceAsync;
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
	@Inject private AccountServiceAsync rcpAccountService;

	@Inject
	private AccountsListPresenter(Display display) {
		this.display = display;
	}

	public static void create(final Callback callback) {
		if (instance == null) {
			instance = NomosInjector.INSTANCE.getAccountsListPresenter();
		}
		instance.getRemoteData(true, callback);
	}
	
	private void updateViewFromData(List<Account> datas) {
		display.refreshAccountsTypePanel(datas);
	}
	
	private final void getRemoteData(boolean isOrdered, final Presenter.Callback callback) {
		rcpAccountService.getList(isOrdered, new AsyncCallback<List<Account>>() {
			
			@Override
			public void onSuccess(List<Account> result) {
				updateViewFromData(result);
				callback.onCreate(instance);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				WaitingFlyer.stop();
				MessageFlyer.error(caught.getLocalizedMessage());
			}
		});		
	}
	
	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(display.asWidget());
	}

}
