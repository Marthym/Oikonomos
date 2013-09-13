package com.marthym.oikonomos.login.client.components;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HTML;
import com.marthym.oikonomos.client.i18n.OikonomosConstants;
import com.marthym.oikonomos.login.client.resources.WelcomeFormsRessource;

public class ConnectionForm extends Composite {

	private static ConnectionFormUiBinder uiBinder = GWT.create(ConnectionFormUiBinder.class);

	interface ConnectionFormUiBinder extends UiBinder<Widget, ConnectionForm> {}

	@UiField TextBox email;
	@UiField PasswordTextBox password;
	@UiField Button btnConnect;
	@UiField HTML errorHandler;
	@UiField FormPanel formPanel;
	@UiField VerticalPanel vPanel;
	
	public ConnectionForm() {
		TextBox newEmail = TextBox.wrap(DOM.getElementById("remember_username"));
		initWidget(uiBinder.createAndBindUi(this));
		WelcomeFormsRessource.INSTANCE.style().ensureInjected();
		
		OikonomosConstants constants = (OikonomosConstants) GWT.create(OikonomosConstants.class);
		int index = vPanel.getWidgetIndex(email);
		vPanel.remove(index);
		email = newEmail;
		vPanel.insert(email, index);
		
		email.getElement().setAttribute("placeholder", constants.email());
		password.getElement().setAttribute("placeholder", constants.password());
	}

	public HasClickHandlers getLoginButton() {
		return btnConnect;
	}
	
	public TextBox getEmail() {
		return email;
	}

	public TextBox getPassword() {
		return password;
	}
	
	public FormPanel getLoginForm() {
		return formPanel;
	}
}
