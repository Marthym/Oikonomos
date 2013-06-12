package com.marthym.oikonomos.main.client.presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.marthym.oikonomos.client.components.MessageFlyer;
import com.marthym.oikonomos.client.presenter.Presenter;
import com.marthym.oikonomos.main.client.components.TopNavigationBar;
import com.marthym.oikonomos.main.client.presenter.DashboardPresenterFactory.CenterType;

public class DashboardPresenter implements Presenter, ValueChangeHandler<String> {
	public interface Display {
		Widget asWidget();
		HasWidgets getTopPanel();
		HasWidgets getCenterPanel();
	}
	
	private final HandlerManager eventBus;
	private final Display display;
	private TopNavigationPresenter topNavigationPresenter;
	private Presenter centralPresenter;

	public DashboardPresenter(HandlerManager eventBus, Display display) {
		this.eventBus = eventBus;
		this.display = display;
		bind();
	}
	
	private void bind() {
		History.addValueChangeHandler(this);
	}
	
	@Override
	public void go(HasWidgets container) {
		GWT.runAsync(new RunAsyncCallback() {
			public void onFailure(Throwable caught) {
				MessageFlyer.error(caught.getLocalizedMessage());
			}

			public void onSuccess() {
				if (topNavigationPresenter == null) {
					topNavigationPresenter = new TopNavigationPresenter(eventBus, new TopNavigationBar());
				}
				topNavigationPresenter.go(display.getTopPanel());
			}
		});
		
		container.clear();
		container.add(display.asWidget());
		
		if (!History.getToken().isEmpty()) {
			History.fireCurrentHistoryState();
		} else {
			History.newItem("dashboard");
		}
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		final String historyToken = event.getValue();
		
		GWT.runAsync(new RunAsyncCallback() {
			public void onFailure(Throwable caught) {
				MessageFlyer.error(caught.getLocalizedMessage());
			}

			public void onSuccess() {
				centralPresenter = DashboardPresenterFactory.createCentralPresenter(eventBus, CenterType.valueOf(historyToken.toUpperCase()));
				centralPresenter.go(display.getCenterPanel());
			}
		});
	}

}
