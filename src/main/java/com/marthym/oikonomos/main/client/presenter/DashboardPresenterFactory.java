package com.marthym.oikonomos.main.client.presenter;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.marthym.oikonomos.client.presenter.Presenter;
import com.marthym.oikonomos.shared.view.data.AccountsListData;
import com.marthym.oikonomos.shared.view.data.ContentPanelData;
import com.marthym.oikonomos.shared.view.data.EditAccountData;

public class DashboardPresenterFactory {
	public enum ContentPanelType {
		DASHBOARD,
		ACCOUNTS,
		ACCOUNT
	}

	public final static void createCentralPresenter(final HasWidgets parent, HandlerManager eventBus, ContentPanelData datas) {
		Presenter.Callback callback = new Presenter.Callback() {
			
			@Override
			public void onCreateFailed() {
				
			}
			
			@Override
			public void onCreate(Presenter presenter) {
				presenter.go(parent);
			}
		};
		
		switch (datas.getContentType()) {
		case DASHBOARD:
		case ACCOUNTS:
			Presenter accountListPres = AccountsListPresenter.getInstance((AccountsListData)datas);
			callback.onCreate(accountListPres);
			return;
			
		case ACCOUNT:
			EditAccountPresenter.createAsync(eventBus, (EditAccountData)datas, callback);
			return;
			
		default:
			return;
		}
	}
	
}
