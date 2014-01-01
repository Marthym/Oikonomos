package com.marthym.oikonomos.main.client.presenter;

import java.util.logging.Logger;

import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.marthym.oikonomos.main.client.NomosInjector;
import com.marthym.oikonomos.main.client.event.AccountDataUpdateEvent;
import com.marthym.oikonomos.main.client.event.AccountDataUpdateEventHandler;
import com.marthym.oikonomos.main.client.event.AccountTransactionsDataLoadedEvent;
import com.marthym.oikonomos.shared.model.Account;
import com.marthym.oikonomos.shared.services.ViewsDataServiceAsync;
import com.marthym.oikonomos.shared.view.data.AccountTabbedData;
import com.marthym.oikonomos.client.components.MessageFlyer;
import com.marthym.oikonomos.client.components.WaitingFlyer;
import com.marthym.oikonomos.client.presenter.Presenter;

public class AccountTabbedPresenter implements Presenter {
	private static final Logger LOG = Logger.getLogger(AccountTabbedPresenter.class.getName());
	
	public interface Display {
		Widget asWidget();
		HasWidgets getAccountPropertiesTab();
		HasWidgets getAccountTransactionsTab();
		void displayTransactionsListTab();
		void displayAccountPropertiesTab();
	}
	
	private final Display display;
	private static AccountTabbedPresenter instance = null;
	private Account account;
	private EventBus eventBus;
	
	@Inject private ViewsDataServiceAsync rpcViewsData;
	
	public static void createAsync(final Presenter.Callback callback) {
		GWT.runAsync(new RunAsyncCallback() {
			
			@Override public void onSuccess() {
				if (instance == null) {
					instance = NomosInjector.INSTANCE.getAccountTabbedPresenter();
					EditAccountPresenter.create(instance.display.getAccountPropertiesTab());
					AccountTransactionsPresenter.create(instance.display.getAccountTransactionsTab());
				}
				
				String[] splitHistoryToken = History.getToken().split(DashboardPresenter.HISTORY_PARAM_SEPARATOR);
				try {
					long accountId = Long.parseLong(splitHistoryToken[1]);
					instance.getRemoteData(accountId);
					instance.display.displayTransactionsListTab();
				} catch (Exception e) {
					instance.display.displayAccountPropertiesTab();
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
		this.display = display;
		this.eventBus = eventBus;
		
		bind();
	}
	
	private void bind() {
		eventBus.addHandler(AccountDataUpdateEvent.TYPE, new AccountDataUpdateEventHandler() {
			@Override public void onAccountDataUpdate(AccountDataUpdateEvent event) {
				account = event.getAccount();
			}
		});
	}

	
	private final void getRemoteData(long accountId) {
		if (account != null && account.getId().equals(accountId)) return;
		
		WaitingFlyer.start();
		rpcViewsData.getAccountTabbedData(accountId, new AsyncCallback<AccountTabbedData>() {
			@Override
			public void onSuccess(AccountTabbedData data) {
				LOG.finer("Account loaded: "+data.getAccount().getId());
				WaitingFlyer.stop();
				eventBus.fireEvent(new AccountDataUpdateEvent(data.getAccount()));
				eventBus.fireEvent(new AccountTransactionsDataLoadedEvent(data.getTransactions()));
			}
			
			@Override
			public void onFailure(Throwable caught) {
				WaitingFlyer.stop();
				MessageFlyer.error(caught.getLocalizedMessage());
			}
		});
	}
	
	public final Account getCurrentAccount() {
		return account;
	}
	
	public void setCurrentAccount(Account account) {
		this.account = account;
	}
	
	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(display.asWidget());
	}

}
