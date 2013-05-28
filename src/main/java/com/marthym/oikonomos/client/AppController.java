package com.marthym.oikonomos.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;

import com.marthym.oikonomos.client.presenter.Presenter;
import com.marthym.oikonomos.client.presenter.WelcomePresenter;
import com.marthym.oikonomos.client.view.WelcomeView;

public class AppController implements Presenter, ValueChangeHandler<String> {
	private final HandlerManager eventBus;
	private HasWidgets container;
	private Presenter welcomePresenter;

	public AppController() {
		this.eventBus = new HandlerManager(null);
		bind();
	}

	private void bind() {
		History.addValueChangeHandler(this);

		// eventBus.addHandler(AddContactEvent.TYPE,
		// new AddContactEventHandler() {
		// public void onAddContact(AddContactEvent event) {
		// doAddNewContact();
		// }
		// });
	}

	public void go(final HasWidgets container) {
		this.container = container;

		if (!History.getToken().isEmpty()) {
			History.fireCurrentHistoryState();
		} else {
			History.newItem("login");
		}
	}

	public void onValueChange(ValueChangeEvent<String> event) {
		//String token = event.getValue();

		GWT.runAsync(new RunAsyncCallback() {
			public void onFailure(Throwable caught) {
			}

			public void onSuccess() {
				if (welcomePresenter == null) {
					welcomePresenter = new WelcomePresenter(eventBus, new WelcomeView());
				}
				welcomePresenter.go(container);
			}
		});

	}
}
