package com.marthym.oikonomos.main.client.presenter;

import com.google.gwt.event.shared.HandlerManager;
import com.marthym.oikonomos.client.presenter.Presenter;
import com.marthym.oikonomos.main.client.view.AccountsListView;

public class DashboardPresenterFactory {
	public enum CenterType {
		DASHBOARD,
		ACCOUNTS
	}

	public final static Presenter createCentralPresenter(HandlerManager eventBus, CenterType type) {
		switch (type) {
		case DASHBOARD:
		case ACCOUNTS:
			return new AccountsListPresenter(eventBus, new AccountsListView());
		default:
			return null;
		}
	}
}
