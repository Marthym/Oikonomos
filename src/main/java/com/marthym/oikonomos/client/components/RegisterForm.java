package com.marthym.oikonomos.client.components;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.marthym.oikonomos.client.i18n.OikonomosConstants;
import com.marthym.oikonomos.client.resources.WelcomeFormsRessource;
import com.marthym.oikonomos.client.resources.WelcomeFormsRessource.WelcomeFormsCss;

public class RegisterForm extends Composite {
	private static RegisterFormUiBinder uiBinder = GWT
			.create(RegisterFormUiBinder.class);

	interface RegisterFormUiBinder extends UiBinder<Widget, RegisterForm> {}

	@UiField TextBox email;
	@UiField TextBox lastname;
	@UiField TextBox firstname;
	@UiField PasswordTextBox password;
	@UiField PasswordTextBox passwordConfirm;
	@UiField Button btnRegister;
	
	private final WelcomeFormsCss css = WelcomeFormsRessource.INSTANCE.style();

	public RegisterForm() {
		OikonomosConstants constants = GWT.create(OikonomosConstants.class);
		css.ensureInjected();
		
		initWidget(uiBinder.createAndBindUi(this));
		email.getElement().setAttribute("placeholder", constants.email());
		lastname.getElement().setAttribute("placeholder", constants.lastname());
		firstname.getElement().setAttribute("placeholder", constants.firstname());
		password.getElement().setAttribute("placeholder", constants.password());
		passwordConfirm.getElement().setAttribute("placeholder", constants.passwordConfirm());
	}

	public HasClickHandlers getRegisterButton() {
		return btnRegister;
	}
	
	public HasValue<String> getEmail() {
		return email;
	}

	public HasValue<String> getLastname() {
		return lastname;
	}

	public HasValue<String> getFirstname() {
		return firstname;
	}

	public HasValue<String> getPassword() {
		return password;
	}

	public HasValue<String> getPasswordConfirm() {
		return passwordConfirm;
	}

}
