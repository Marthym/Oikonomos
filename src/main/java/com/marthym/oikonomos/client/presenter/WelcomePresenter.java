package com.marthym.oikonomos.client.presenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.marthym.oikonomos.client.event.LoginEvent;

public class WelcomePresenter implements Presenter {
	public interface Display {
		HasClickHandlers getLoginButton();
		HasValue<String> getEmailAddress();
		HasValue<String> getPassword();
		Widget asWidget();
	}

	private final HandlerManager eventBus;
	private final Display display;

	public WelcomePresenter(HandlerManager eventBus, Display display) {
		this.eventBus = eventBus;
		this.display = display;
		bind();
	}

	private void bind() {
		this.display.getLoginButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new LoginEvent(display.getEmailAddress().getValue(), display.getPassword().getValue()));
			}
		});
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(display.asWidget());
	}
	
}
