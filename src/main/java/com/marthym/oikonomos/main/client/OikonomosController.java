package com.marthym.oikonomos.main.client;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

import com.marthym.oikonomos.client.presenter.Presenter;
import com.marthym.oikonomos.client.services.AuthenticationServiceAsync;
import com.marthym.oikonomos.main.client.event.LogoutEvent;
import com.marthym.oikonomos.main.client.event.LogoutEventHandler;
import com.marthym.oikonomos.main.client.presenter.DashboardPresenter;
import com.marthym.oikonomos.main.client.view.DashboardView;
import com.marthym.oikonomos.shared.exceptions.OikonomosRuntimeException;
import com.marthym.oikonomos.shared.model.User;

public class OikonomosController implements Presenter {
	private static final String CURRENT_MODULE_PATH = "oikonomos.html";
	private static final String LOGIN_MODULE_PATH = "index.html";
	
	private final HandlerManager eventBus;
	private Presenter dashboardPresenter;
	private static User authentifiedUser;
	private static final AuthenticationServiceAsync rpcService = AuthenticationServiceAsync.Util.getInstance();

	public OikonomosController() {
		this.eventBus = new HandlerManager(null);
		bind();
	}

	private void bind() {
		doRemoteAuthentifiedUserRequest();
		
		eventBus.addHandler(LogoutEvent.TYPE,
				new LogoutEventHandler() {
					
					@Override
					public void onLogout(LogoutEvent event) {
						doOikonomosLogout();
					}
				});
	}

	public void go(final HasWidgets container) {
		if (dashboardPresenter == null) {
			dashboardPresenter = new DashboardPresenter(eventBus, new DashboardView());
		}
		dashboardPresenter.go(container);
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
			
		});
	}
	
	private void doRemoteAuthentifiedUserRequest() {
		rpcService.getAuthentifiedUser(new AsyncCallback<User>() {
			@Override
			public void onFailure(Throwable caught) {
				redirectToLogin();
			}

			@Override
			public void onSuccess(User result) {
				authentifiedUser = result;
			}
		});
	}

	private void redirectToLogin() {
		 String path = Window.Location.getPath();
        String modulePath = CURRENT_MODULE_PATH;
        int index = path.indexOf(modulePath);
        String contextPath = path.substring(0,index);

        Window.open(contextPath + LOGIN_MODULE_PATH, "_self", "");
	}
	
	public static final User getAuthentifiedUser() {
		if (authentifiedUser == null) throw new OikonomosRuntimeException();
		return authentifiedUser;
	}
}
