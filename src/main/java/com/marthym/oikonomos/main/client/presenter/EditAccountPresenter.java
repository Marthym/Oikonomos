package com.marthym.oikonomos.main.client.presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.marthym.oikonomos.main.client.view.EditAccountView;
import com.marthym.oikonomos.shared.model.Account;
import com.marthym.oikonomos.shared.view.data.EditAccountData;
import com.marthym.oikonomos.client.presenter.Presenter;

public class EditAccountPresenter implements Presenter {
	public interface Display {
		Widget asWidget();
		
		HasClickHandlers getValidateButton();
		HasClickHandlers getResetButton();
		void updateViewData(Account account);
	}
	
	private final Display display;
	private final HandlerManager eventBus;
	private static EditAccountPresenter instance = null;
	private Account account;
	
	public static void createAsync(final HandlerManager eventBus, final EditAccountData datas, final Presenter.Callback callback) {
		GWT.runAsync(new RunAsyncCallback() {
			
			@Override
			public void onSuccess() {
				if (instance == null) {
					instance = new EditAccountPresenter(eventBus, datas);
				}
				callback.onCreate(instance);
			}
			
			@Override
			public void onFailure(Throwable reason) {
				callback.onCreateFailed();
			}
		});
	}
	
	private EditAccountPresenter(HandlerManager eventBus, EditAccountData datas) {
		this.display = new EditAccountView();
		this.eventBus = eventBus;
		this.account = datas.getEditAccount();
		bind();
		
	}
	
	private void bind() {
		display.updateViewData(account);
		
	}
	
	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(display.asWidget());
	}

}