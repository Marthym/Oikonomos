package com.marthym.oikonomos.main.client.view;

import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.marthym.oikonomos.client.i18n.OikonomosConstants;
import com.marthym.oikonomos.main.client.presenter.UserProfilePresenter;
import com.marthym.oikonomos.main.client.resources.MainFormViewResource;

//TODO: Ask old password for change the password
public class UserProfileView extends Composite implements UserProfilePresenter.Display {
	private static UserProfileViewUiBinder uiBinder = GWT.create(UserProfileViewUiBinder.class);

	interface UserProfileViewUiBinder extends UiBinder<Widget, UserProfileView> {}
	
	@UiField FormPanel formUser;
	@UiField Label userLogin;
	@UiField TextBox userLastName;
	@UiField TextBox userFirstName;
	@UiField PasswordTextBox userPassword;
	@UiField PasswordTextBox userPasswordConfirm;
	@UiField Label userRegistrationDate;
	@UiField Label userLastLoginDate;
	@UiField Button resetButton;
	@UiField Button submitButton;
	
	@Inject OikonomosConstants constants;

	public UserProfileView() {
		MainFormViewResource.INSTANCE.style().ensureInjected();
		
		initWidget(uiBinder.createAndBindUi(this));		
	}
	
	@Override
	public void reset() {
		formUser.reset();
	}

	@Override
	public HasClickHandlers getValidateButton() {
		return submitButton;
	}

	@Override
	public HasClickHandlers getResetButton() {
		return resetButton;
	}

	@Override
	public HasText getUserLogin() { return userLogin; }

	@Override
	public HasValue<String> getUserLastName() { return userLastName; }

	@Override
	public HasValue<String> getUserFirstName() { return userFirstName;}

	@Override
	public HasValue<String> getUserPassword() { return userPassword; }

	@Override
	public HasValue<String> getUserPasswordConfirm() { return userPasswordConfirm; }

	@Override
	public HasText getUserRegistrationDate() { return userRegistrationDate; }

	@Override
	public HasText getUserLastLoginDate() { return userLastLoginDate; }

}
