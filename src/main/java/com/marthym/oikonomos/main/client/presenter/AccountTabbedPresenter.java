package com.marthym.oikonomos.main.client.presenter;

import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.marthym.oikonomos.main.client.NomosInjector;
import com.marthym.oikonomos.main.client.i18n.EditAccountConstants;
import com.marthym.oikonomos.shared.services.AccountServiceAsync;
import com.marthym.oikonomos.shared.model.Account;
import com.marthym.oikonomos.client.i18n.OikonomosErrorMessages;
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
	private static Presenter editAccount = null;
	private Account account;
	
	@Inject private OikonomosErrorMessages errorMessages;
	@Inject private EditAccountConstants constants;
	@Inject private AccountServiceAsync rcpAccountService;

	public static void createAsync(final Presenter.Callback callback) {
		GWT.runAsync(new RunAsyncCallback() {
			
			@Override public void onSuccess() {
				if (instance == null) {
					instance = NomosInjector.INSTANCE.getAccountTabbedPresenter();
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

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(display.asWidget());
	}

	@Override
	public Widget getDisplay() {
		return display.asWidget();
	}

}
