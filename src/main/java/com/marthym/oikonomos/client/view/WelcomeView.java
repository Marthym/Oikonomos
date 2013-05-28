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

	@UiField AnonymousNavigationBar navigationBar;
	@UiField DeckLayoutPanel deck;
	@UiField ConnectionForm connectionForm;
	@UiField RegisterForm registerForm;
	
	public WelcomeView() {
		initWidget(uiBinder.createAndBindUi(this));				
	}

	@Override
	public HasClickHandlers getRegisterButton() {
		return registerForm.getRegisterButton();
	}

	@Override
	public HasClickHandlers getLoginButton() {
		return connectionForm.getLoginButton();
	}

	@Override
	public HasClickHandlers getLoginLink() {
		return navigationBar.getLoginLink();
	}

	@Override
	public HasClickHandlers getRegisterLink() {
		return navigationBar.getRegisterLink();
	}

	@Override
	public HasValue<String> getFirstName() {
		return registerForm.getFirstname();
	}

	@Override
	public HasValue<String> getLastName() {
		return registerForm.getLastname();
	}

	@Override
	public HasValue<String> getEmailAddress() {
		return registerForm.getEmail();
	}

	@Override
	public HasValue<String> getPassword() {
		return registerForm.getPassword();
	}

	@Override
	public HasValue<String> getPasswordConfirm() {
		return registerForm.getPasswordConfirm();
	}

	@Override
	public DeckLayoutPanel getDeckLayoutPanel() {
		return deck;
	}

	@Override
	public AnonymousNavigationBar getNavigationBar() {
		return navigationBar;
	}
}
