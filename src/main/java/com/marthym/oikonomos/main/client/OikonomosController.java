package com.marthym.oikonomos.main.client;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.marthym.oikonomos.client.presenter.Presenter;
import com.marthym.oikonomos.main.client.event.LogoutEvent;
import com.marthym.oikonomos.main.client.event.LogoutEventHandler;
import com.marthym.oikonomos.main.client.event.UserUpdateEvent;
import com.marthym.oikonomos.main.client.event.UserUpdateEventHandler;
import com.marthym.oikonomos.main.client.presenter.DashboardPresenter;
import com.marthym.oikonomos.shared.exceptions.OikonomosRuntimeException;
import com.marthym.oikonomos.shared.model.User;
import com.marthym.oikonomos.shared.services.AuthenticationServiceAsync;

public class OikonomosController implements Presenter {
	private static final String CURRENT_MODULE_PATH = "oikonomos.html";
	private static final String LOGIN_MODULE_PATH = "index.html";
	
	private final EventBus eventBus;
	private final AuthenticationServiceAsync rpcService;
	private DashboardPresenter dashboardPresenter;
	private static User authentifiedUser;

	public OikonomosController() {
		this.eventBus = NomosInjector.INSTANCE.getEventBus();
		this.rpcService = NomosInjector.INSTANCE.getAuthenticationServiceAsync();
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
		
		eventBus.addHandler(UserUpdateEvent.TYPE,
				new UserUpdateEventHandler() {
					@Override
					public void onUserUpdate(UserUpdateEvent event) {
						authentifiedUser = event.getUser();
					}
				});
	}

	public void go(final HasWidgets container) {
		if (dashboardPresenter == null) {
			dashboardPresenter = NomosInjector.INSTANCE.getDashboardPresenter();
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

	public static final void redirectToLogin() {
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
