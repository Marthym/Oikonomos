package com.marthym.oikonomos.main.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;

import com.marthym.oikonomos.client.presenter.Presenter;
import com.marthym.oikonomos.main.client.presenter.DashboardPresenter;
import com.marthym.oikonomos.main.client.services.UserServiceAsync;
import com.marthym.oikonomos.main.client.view.DashboardView;

public class OikonomosController implements Presenter, ValueChangeHandler<String> {
	private final HandlerManager eventBus;
	private HasWidgets container;
	private Presenter dashboardPresenter;
	private final UserServiceAsync rpcService;

	public OikonomosController() {
		this.rpcService = UserServiceAsync.Util.getInstance();
		this.eventBus = new HandlerManager(null);
		bind();
	}

	private void bind() {
		History.addValueChangeHandler(this);

//		eventBus.addHandler(LoginEvent.TYPE,
//				new LoginEventHandler() {
//					
//					@Override
//					public String onLogin(LoginEvent event) {
//						return doOikonomosLogin(event.getUserName(), event.getUserPassword());
//					}
//				});
	}

	public void go(final HasWidgets container) {
		this.container = container;

		if (!History.getToken().isEmpty()) {
			History.fireCurrentHistoryState();
		} else {
			History.newItem("dashboard");
		}
	}

	public void onValueChange(ValueChangeEvent<String> event) {

		GWT.runAsync(new RunAsyncCallback() {
			public void onFailure(Throwable caught) {
				Window.alert(caught.getLocalizedMessage());
			}

			public void onSuccess() {
				if (dashboardPresenter == null) {
					dashboardPresenter = new DashboardPresenter(eventBus, new DashboardView());
				}
				dashboardPresenter.go(container);
			}
		});
	}
	
}
