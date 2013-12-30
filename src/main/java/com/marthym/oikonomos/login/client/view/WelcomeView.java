package com.marthym.oikonomos.login.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.marthym.oikonomos.login.client.components.ConnectionForm;
import com.marthym.oikonomos.login.client.presenter.WelcomePresenter;

public class WelcomeView extends Composite implements WelcomePresenter.Display {	
	private static WelcomeViewUiBinder uiBinder = GWT.create(WelcomeViewUiBinder.class);
	
	interface WelcomeViewUiBinder extends UiBinder<Widget, WelcomeView> {}

	@UiField ConnectionForm connectionForm;
	
	public WelcomeView() {
		initWidget(uiBinder.createAndBindUi(this));				
	}

	@Override
	public TextBox getEmailAddress() {
		return connectionForm.getEmail();
	}

	@Override
	public TextBox getPassword() {
		return connectionForm.getPassword();
	}

	@Override
	public FormPanel getLoginForm() {
		return connectionForm.getLoginForm();
	}

}
