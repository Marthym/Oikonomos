package com.marthym.oikonomos.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.marthym.oikonomos.client.components.AnonymousNavigationBar;
import com.marthym.oikonomos.client.components.ConnectionForm;
import com.marthym.oikonomos.client.components.RegisterForm;
import com.marthym.oikonomos.client.presenter.WelcomePresenter;

public class WelcomeView extends Composite implements WelcomePresenter.Display {
	public static enum DeckWidgetIndex {CONNECT_WIDGET, REGISTER_WIDGET}
	
	private static WelcomeViewUiBinder uiBinder = GWT.create(WelcomeViewUiBinder.class);
	
	interface WelcomeViewUiBinder extends UiBinder<Widget, WelcomeView> {}

	@UiField ConnectionForm connectionForm;
	
	public WelcomeView() {
		initWidget(uiBinder.createAndBindUi(this));				
	}

	@Override
	public HasClickHandlers getLoginButton() {
		return connectionForm.getLoginButton();
	}

	@Override
	public HasValue<String> getEmailAddress() {
		return connectionForm.getEmail();
	}

	@Override
	public HasValue<String> getPassword() {
		return connectionForm.getPassword();
	}

}
