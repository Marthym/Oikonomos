package com.marthym.oikonomos.main.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

import com.marthym.oikonomos.client.components.MessageFlyer;
import com.marthym.oikonomos.client.presenter.Presenter;
import com.marthym.oikonomos.client.services.AuthenticationServiceAsync;
import com.marthym.oikonomos.main.client.event.LogoutEvent;
import com.marthym.oikonomos.main.client.event.LogoutEventHandler;
import com.marthym.oikonomos.main.client.presenter.DashboardPresenter;
import com.marthym.oikonomos.main.client.view.DashboardView;

public class OikonomosController implements Presenter {
	private static final String CURRENT_MODULE_PATH = "oikonomos.html";
	private static final String LOGIN_MODULE_PATH = "index.html";
	
	private final HandlerManager eventBus;
	private Presenter dashboardPresenter;
	private final AuthenticationServiceAsync rpcService;

	public OikonomosController() {
		this.rpcService = AuthenticationServiceAsync.Util.getInstance();
		this.eventBus = new HandlerManager(null);
		bind();
	}

	private void bind() {
		eventBus.addHandler(LogoutEvent.TYPE,
				new LogoutEventHandler() {
					
					@Override
					public void onLogout(LogoutEvent event) {
						doOikonomosLogout();
					}
				});
	}

	public void go(final HasWidgets container) {

		GWT.runAsync(new RunAsyncCallback() {
			public void onFailure(Throwable caught) {
				MessageFlyer.error(caught.getLocalizedMessage());
			}

			public void onSuccess() {
				if (dashboardPresenter == null) {
					dashboardPresenter = new DashboardPresenter(eventBus, new DashboardView());
				}
				dashboardPresenter.go(container);
			}
		});

	}
	
	private void doOikonomosLogout() {
		rpcService.logout(new AsyncCallback<Void>() {
			
			@Override
			public void onSuccess(Void result) {
				redirectToLogin();
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				redirectToLogin();
			}
			
			private void redirectToLogin() {
				 String path = Window.Location.getPath();
                 String modulePath = CURRENT_MODULE_PATH;
                 int index = path.indexOf(modulePath);
                 String contextPath = path.substring(0,index);

                 Window.open(contextPath + LOGIN_MODULE_PATH, "_self", "");
			}
		});
	}
	
}
