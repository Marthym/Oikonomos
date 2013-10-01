package com.marthym.oikonomos.login.client.presenter;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.marthym.oikonomos.client.presenter.Presenter;
import com.marthym.oikonomos.login.client.event.LoginEvent;

public class WelcomePresenter implements Presenter {
	public interface Display {
		HasClickHandlers getLoginButton();
		TextBox getEmailAddress();
		TextBox getPassword();
		FormPanel getLoginForm();
		Widget asWidget();
	}

	private final EventBus eventBus;
	private final Display display;

	public WelcomePresenter(EventBus eventBus, Display display) {
		this.eventBus = eventBus;
		this.display = display;
		bind();
	}

	private void bind() {
		this.display.getLoginForm().addSubmitHandler(new FormPanel.SubmitHandler() {
			
			@Override
			public void onSubmit(SubmitEvent event) {
				eventBus.fireEvent(new LoginEvent(display.getEmailAddress().getValue(), display.getPassword().getValue()));
			}
		});
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(display.asWidget());
	}

	@Override
	public Widget getDisplay() {
		return display.asWidget();
	}
	
}
