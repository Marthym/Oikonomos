package com.marthym.oikonomos.main.client.presenter;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.HasWidgets;
import com.marthym.oikonomos.client.components.MessageFlyer;
import com.marthym.oikonomos.client.i18n.OikonomosErrorMessages;
import com.marthym.oikonomos.client.presenter.Presenter;

public class DashboardPresenterFactory {
	public enum ContentPanelType {
		DASHBOARD,
		ACCOUNTS,
		ACCOUNT,
		PROFILE,
		PAYEE,
		CATEGORY
	}
		
	public final static void createCentralPresenter(final HasWidgets parent, EventBus eventBus, String historyToken) {
		Presenter.Callback callback = new Presenter.Callback() {
			@Override
			public void onCreateFailed() {
				final OikonomosErrorMessages errorMessages = GWT.create(OikonomosErrorMessages.class);
				MessageFlyer.error(errorMessages.error_message_presenter_failToCreate());
			}
			
			@Override
			public void onCreate(Presenter presenter) {
				presenter.go(parent);
			}
		};
		
		String[] splitHistoryToken = historyToken.split(DashboardPresenter.HISTORY_PARAM_SEPARATOR);
		final ContentPanelType contentType = ContentPanelType.valueOf(splitHistoryToken[0].toUpperCase());
		
		switch (contentType) {
		case DASHBOARD:
		case ACCOUNTS:
			AccountsListPresenter.create(callback);
			return;
			
		case ACCOUNT:
			EditAccountPresenter.createAsync(callback);
			return;
		
		case PROFILE:
			UserProfilePresenter.createAsync(callback);
			return;
			
		case CATEGORY:
			EditCategoryPresenter.createAsync(callback);
			return;
		
		case PAYEE:
			EditPayeePresenter.createAsync(callback);
			return;
			
		default:
			return;
		}
	}
	
}
