package com.marthym.oikonomos.main.client.presenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.marthym.oikonomos.client.presenter.Presenter;
import com.marthym.oikonomos.main.client.event.LogoutEvent;

public class TopNavigationPresenter implements Presenter {
	public interface Display {
		HasClickHandlers getDashboardLink();
		HasClickHandlers getPropertiesLink();
		HasClickHandlers getLogoutLink();
		HTMLPanel getUserName();
		Widget asWidget();
	}
	
	private final HandlerManager eventBus;
	private final Display display;

	public TopNavigationPresenter(HandlerManager eventBus, Display display) {
		this.eventBus = eventBus;
		this.display = display;
		bind();
	}
	
	private void bind() {
		this.display.getLogoutLink().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new LogoutEvent());
			}
		});
	}
	
	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(display.asWidget());
	}

}
