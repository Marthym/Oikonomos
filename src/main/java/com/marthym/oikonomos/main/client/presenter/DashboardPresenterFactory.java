package com.marthym.oikonomos.main.client.presenter;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.marthym.oikonomos.client.components.MessageFlyer;
import com.marthym.oikonomos.client.presenter.Presenter;
import com.marthym.oikonomos.shared.view.data.AccountsListData;
import com.marthym.oikonomos.shared.view.data.ContentPanelData;
import com.marthym.oikonomos.shared.view.data.EditAccountData;

public class DashboardPresenterFactory {
	public enum ContentPanelType {
		DASHBOARD(true),
		ACCOUNTS(true),
		ACCOUNT(false);
		
		private boolean needData;
		private ContentPanelType(boolean needData) {
			this.needData = needData;
		}
		
		public final boolean isDataNeeded() {return needData;}
	}

	public final static void createCentralPresenter(final HasWidgets parent, HandlerManager eventBus, ContentPanelData datas) {
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
		
		switch (datas.getContentType()) {
		case DASHBOARD:
		case ACCOUNTS:
			Presenter accountListPres = AccountsListPresenter.getInstance((AccountsListData)datas);
			callback.onCreate(accountListPres);
			return;
			
		case ACCOUNT:
			EditAccountPresenter.createAsync(eventBus, EditAccountData.cast(datas), callback);
			return;
			
		default:
			return;
		}
	}

	public static void createCentralPresenter(HasWidgets centerPanel, HandlerManager eventBus, ContentPanelType type) {
		createCentralPresenter(centerPanel, eventBus, ContentPanelData.getEmptyData(type));
	}
	
}
