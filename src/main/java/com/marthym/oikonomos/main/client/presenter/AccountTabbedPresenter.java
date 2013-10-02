package com.marthym.oikonomos.main.client.presenter;

import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.marthym.oikonomos.main.client.NomosInjector;
import com.marthym.oikonomos.main.client.OikonomosController;
import com.marthym.oikonomos.shared.model.Account;
import com.marthym.oikonomos.shared.model.User;
import com.marthym.oikonomos.shared.services.AccountServiceAsync;
import com.marthym.oikonomos.client.components.MessageFlyer;
import com.marthym.oikonomos.client.components.WaitingFlyer;
import com.marthym.oikonomos.client.presenter.Presenter;

public class AccountTabbedPresenter implements Presenter {
	
	public interface Display {
		Widget asWidget();
		HasWidgets getAccountPropertiesTab();
		HasWidgets getAccountTransactionsTab();
	}
	
	private final Display display;
	private final EventBus eventBus;
	private static AccountTabbedPresenter instance = null;
	private static EditAccountPresenter editAccount = null;
	private Account account;
	
	@Inject private AccountServiceAsync rcpAccountService;
	
	public static void createAsync(final Presenter.Callback callback) {
		GWT.runAsync(new RunAsyncCallback() {
			
			@Override public void onSuccess() {
				if (instance == null) {
					instance = NomosInjector.INSTANCE.getAccountTabbedPresenter();
				}
				
				String[] splitHistoryToken = History.getToken().split(DashboardPresenter.HISTORY_PARAM_SEPARATOR);
				try {
					long accountId = Long.parseLong(splitHistoryToken[1]);
					instance.getRemoteData(accountId);
				} catch (Exception e) {
					User authentifiedUser = OikonomosController.getAuthentifiedUser();
					instance.account = new Account(authentifiedUser.getUserEmail());
					editAccount.setAccountData(instance.account);
				}
				callback.onCreate(instance);
			}
			
			@Override public void onFailure(Throwable reason) {
				callback.onCreateFailed();
			}
		});
	}
	
	@Inject
	private AccountTabbedPresenter(EventBus eventBus, Display display) {
		this.eventBus = eventBus;
		this.display = display;
		
		bind();
	}
	
	private void bind() {
		editAccount = EditAccountPresenter.create(display.getAccountPropertiesTab());
	}

	
	private final void getRemoteData(long accountId) {
		WaitingFlyer.start();
		rcpAccountService.getEntity(accountId, new AsyncCallback<Account>() {
			@Override
			public void onSuccess(Account result) {
				account = result;
				editAccount.setAccountData(account);
				WaitingFlyer.stop();
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
