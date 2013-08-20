package com.marthym.oikonomos.main.client.presenter;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.marthym.oikonomos.client.components.MessageFlyer;
import com.marthym.oikonomos.client.components.WaitingFlyer;
import com.marthym.oikonomos.client.presenter.Presenter;
import com.marthym.oikonomos.main.client.components.TopNavigationBar;
import com.marthym.oikonomos.main.client.presenter.DashboardPresenterFactory.ContentPanelType;
import com.marthym.oikonomos.main.client.services.DashboardDataServiceAsync;
import com.marthym.oikonomos.main.client.view.LeftMenuView;
import com.marthym.oikonomos.shared.exceptions.OikonomosUnathorizedException;
import com.marthym.oikonomos.shared.model.User;
import com.marthym.oikonomos.shared.view.data.ContentPanelData;
import com.marthym.oikonomos.shared.view.data.DashboardData;
import com.marthym.oikonomos.shared.view.data.EntityType;
import com.marthym.oikonomos.shared.view.data.HasCurrentUserData;
import com.marthym.oikonomos.shared.view.data.HasEntityCountData;

public class DashboardPresenter implements Presenter, ValueChangeHandler<String> {
	public interface Display {
		Widget asWidget();
		HasWidgets getTopPanel();
		HasWidgets getLeftpPanel();
		HasWidgets getCenterPanel();
	}
	
	private final HandlerManager eventBus;
	private final Display display;
	private TopNavigationPresenter topNavigationPresenter;
	private LeftMenuPresenter leftMenuPresenter;
	private User currentUserData;

	public DashboardPresenter(HandlerManager eventBus, Display display) {
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
				try {
					currentUserData = result.getCurrentUserData();
					displayTopNavigation(result);
					displayLeftMenu(result);
					
					container.clear();
					container.add(display.asWidget());
					
					if (!History.getToken().isEmpty()) {
						History.fireCurrentHistoryState();
					} else {
						History.newItem("dashboard");
					}

				} catch (OikonomosUnathorizedException e) {
					MessageFlyer.error(e.getLocalizedMessage());
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
			topNavigationPresenter = new TopNavigationPresenter(eventBus, data, new TopNavigationBar());
		}
		topNavigationPresenter.go(display.getTopPanel());
	}
	
	private void displayLeftMenu(final HasEntityCountData data) {
		if (leftMenuPresenter == null) {
			leftMenuPresenter = new LeftMenuPresenter(eventBus, data, new LeftMenuView());
		}
		leftMenuPresenter.go(display.getLeftpPanel());
		
		String historyToken = History.getToken();
		String[] splitHistoryToken = historyToken.split("\\|");
		try {
			leftMenuPresenter.openEntityMenu(EntityType.valueOf(splitHistoryToken[0].toUpperCase()));
		} catch (Exception e) {}
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		final String historyToken = event.getValue();
		
		DashboardDataServiceAsync rpcDataService = DashboardDataServiceAsync.Util.getInstance();
		String[] splitHistoryToken = historyToken.split("\\|");
		final ContentPanelType contentType = ContentPanelType.valueOf(splitHistoryToken[0].toUpperCase());
		List<String> parameters = new LinkedList<String>();
		for (int i=1; i<splitHistoryToken.length; i++) {
			parameters.add(splitHistoryToken[i]);
		}
		
		if (parameters.isEmpty() && !contentType.isDataNeeded()) {
			DashboardPresenterFactory.createCentralPresenter(
					display.getCenterPanel(), 
					eventBus, 
					contentType, 
					currentUserData);
			return;
		}
		
		WaitingFlyer.start();
		rpcDataService.getContentPanelData(contentType, parameters, new AsyncCallback<ContentPanelData>() {
			
			@Override
			public void onSuccess(ContentPanelData result) {
				WaitingFlyer.stop();
				DashboardPresenterFactory.createCentralPresenter(display.getCenterPanel(), eventBus, result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				WaitingFlyer.stop();
				MessageFlyer.error(caught.getLocalizedMessage());
			}
		});
	}
}
