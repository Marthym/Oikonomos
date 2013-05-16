package com.marthym.oikonomos.client.components;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.marthym.oikonomos.client.i18n.OikonomosConstants;
import com.marthym.oikonomos.client.resources.WelcomeFormsRessource;
import com.marthym.oikonomos.client.resources.WelcomeFormsRessource.WelcomeFormsCss;
import com.marthym.oikonomos.client.services.UserServiceAsync;

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

	@UiHandler("btnRegister")
	void onClick(ClickEvent event) {
		final UserServiceAsync userService = UserServiceAsync.Util.getInstance();
		userService.saveUser(email.getValue(), firstname.getValue(), lastname.getValue(), password.getValue(), new Date(), new Date(), new AsyncCallback<Void>() {
				public void onFailure(Throwable caught) {
					Window.alert(caught.getClass()+": "+caught.getLocalizedMessage());
				}
				public void onSuccess(Void noAnswer) {
					Window.alert("Utilisateur "+email.getValue()+" créé avec success !");
				}
			});
	}

}
