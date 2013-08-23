package com.marthym.oikonomos.main.client.presenter;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.marthym.oikonomos.client.components.MessageFlyer;
import com.marthym.oikonomos.client.presenter.Presenter;

public class DashboardPresenterFactory {
	public enum ContentPanelType {
		DASHBOARD,
		ACCOUNTS,
		ACCOUNT		
	}
		
	public final static void createCentralPresenter(final HasWidgets parent, HandlerManager eventBus, String historyToken) {
		Presenter.Callback callback = new Presenter.Callback() {
			@Override
			public void onCreateFailed() {
				MessageFlyer.error("Fail to create presenter !"); //TODO: Translate !!
			}
			
			@Override
			public void onCreate(Presenter presenter) {
				presenter.go(parent);
			}
		};
		
		String[] splitHistoryToken = historyToken.split("\\|");
		final ContentPanelType contentType = ContentPanelType.valueOf(splitHistoryToken[0].toUpperCase());
		
		switch (contentType) {
		case DASHBOARD:
		case ACCOUNTS:
			AccountsListPresenter.create(callback);
			return;
			
		case ACCOUNT:
			EditAccountPresenter.createAsync(eventBus, callback);
			return;
			
		default:
			return;
		}
	}
	
}
