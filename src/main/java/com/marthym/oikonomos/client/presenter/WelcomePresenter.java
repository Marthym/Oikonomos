package com.marthym.oikonomos.client.presenter;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.validation.client.impl.Validation;
import com.marthym.oikonomos.client.components.AnonymousNavigationBar;
import com.marthym.oikonomos.client.components.MessageFlyer;
import com.marthym.oikonomos.client.components.AnonymousNavigationBar.ActivationType;
import com.marthym.oikonomos.client.services.UserServiceAsync;
import com.marthym.oikonomos.client.view.WelcomeView;
import com.marthym.oikonomos.client.view.WelcomeView.DeckWidgetIndex;
import com.marthym.oikonomos.shared.exceptions.OikonomosException;
import com.marthym.oikonomos.shared.model.User;

public class WelcomePresenter implements Presenter {
	public interface Display {
		HasClickHandlers getLoginButton();
		HasValue<String> getEmailAddress();
		HasValue<String> getPassword();
		Widget asWidget();
	}

	private final UserServiceAsync rpcService;
	private final HandlerManager eventBus;
	private final Display display;

	public WelcomePresenter(HandlerManager eventBus, Display display) {
		this.rpcService = UserServiceAsync.Util.getInstance();
		this.eventBus = eventBus;
		this.display = display;
		bind();
	}

	private void bind() {
		this.display.getLoginButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// TODO: Implement
			}
		});
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(display.asWidget());
	}
	
}
