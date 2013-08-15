package com.marthym.oikonomos.main.client.presenter;

import com.google.gwt.event.shared.HandlerManager;
import com.marthym.oikonomos.client.presenter.Presenter;
import com.marthym.oikonomos.main.client.view.AccountsListView;
import com.marthym.oikonomos.main.client.view.EditAccountView;
import com.marthym.oikonomos.shared.view.data.AccountsListData;
import com.marthym.oikonomos.shared.view.data.ContentPanelData;
import com.marthym.oikonomos.shared.view.data.EditAccountData;

public class DashboardPresenterFactory {
	public enum ContentPanelType {
		DASHBOARD,
		ACCOUNTS,
		ACCOUNT
	}

	public final static Presenter createCentralPresenter(HandlerManager eventBus, ContentPanelData datas) {
		switch (datas.getContentType()) {
		case DASHBOARD:
		case ACCOUNTS:
			return new AccountsListPresenter(new AccountsListView(), (AccountsListData)datas);
			
		case ACCOUNT:
			return new EditAccountPresenter(new EditAccountView(), (EditAccountData)datas);
			
		default:
			return null;
		}
	}
}
