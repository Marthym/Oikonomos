package com.marthym.oikonomos.main.client.presenter;

import javax.inject.Inject;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.marthym.oikonomos.client.components.MessageFlyer;
import com.marthym.oikonomos.client.components.WaitingFlyer;
import com.marthym.oikonomos.client.presenter.Presenter;
import com.marthym.oikonomos.main.client.NomosInjector;
import com.marthym.oikonomos.shared.services.DashboardDataServiceAsync;
import com.marthym.oikonomos.shared.view.data.DashboardData;
import com.marthym.oikonomos.shared.view.data.EntityType;
import com.marthym.oikonomos.shared.view.data.HasCurrentUserData;
import com.marthym.oikonomos.shared.view.data.HasEntityCountData;

public class DashboardPresenter implements Presenter, ValueChangeHandler<String> {
	
	public interface Display {
		Widget asWidget();
		HasWidgets getTopPanel();
		HasWidgets getLeftPanel();
		HasWidgets getCenterPanel();
	}
	
	private final EventBus eventBus;
	
	private final Display display;
	private TopNavigationPresenter topNavigationPresenter;
	private LeftMenuPresenter leftMenuPresenter;

	static final String HISTORY_PARAM_SEPARATOR = "\\|";

	@Inject
	public DashboardPresenter(EventBus eventBus, Display display) {
		this.eventBus = eventBus;
		this.display = display;
		bind();
	}
	
	private void bind() {
		History.addValueChangeHandler(this);
	}
	
	@Override
	public void go(final HasWidgets container) {
		DashboardDataServiceAsync rpcDataService = DashboardDataServiceAsync.Util.getInstance();
		
		WaitingFlyer.start();
		rpcDataService.getDashboardData(new AsyncCallback<DashboardData>() {
			
			@Override
			public void onSuccess(DashboardData result) {
				displayTopNavigation(result);
				displayLeftMenu(result);
				
				container.clear();
				container.add(display.asWidget());
				
				if (!History.getToken().isEmpty()) {
					History.fireCurrentHistoryState();
				} else {
					History.newItem("dashboard");
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				MessageFlyer.error(caught.getLocalizedMessage());
			}
		});				
	}
	
	private void displayTopNavigation(final HasCurrentUserData data) {
		if (topNavigationPresenter == null) {
			topNavigationPresenter = NomosInjector.INSTANCE.getTopNavigationPresenter();
		}
		topNavigationPresenter.updateViewData(data);
		topNavigationPresenter.go(display.getTopPanel());
	}
	
	private void displayLeftMenu(final HasEntityCountData data) {
		if (leftMenuPresenter == null) {
			leftMenuPresenter = NomosInjector.INSTANCE.getLeftMenuPresenter();
		}
		leftMenuPresenter.updateViewData(data);
		leftMenuPresenter.go(display.getLeftPanel());
		
		String historyToken = History.getToken();
		String[] splitHistoryToken = historyToken.split(HISTORY_PARAM_SEPARATOR);
		try {
			leftMenuPresenter.openEntityMenu(EntityType.valueOf(splitHistoryToken[0].toUpperCase()));
		} catch (Exception e) {}
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		final String historyToken = event.getValue();
		
		WaitingFlyer.start();
		DashboardPresenterFactory.createCentralPresenter(display.getCenterPanel(), eventBus, historyToken);
		WaitingFlyer.stop();
	}
}
