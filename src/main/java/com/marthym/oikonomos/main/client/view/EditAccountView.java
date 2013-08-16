package com.marthym.oikonomos.main.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.marthym.oikonomos.main.client.i18n.EditAccountConstants;
import com.marthym.oikonomos.main.client.presenter.EditAccountPresenter;
import com.marthym.oikonomos.main.client.resources.EditAccountResource;
import com.marthym.oikonomos.shared.model.Account;

public class EditAccountView extends Composite implements EditAccountPresenter.Display {

	private static EditAccountViewUiBinder uiBinder = GWT.create(EditAccountViewUiBinder.class);

	interface EditAccountViewUiBinder extends UiBinder<Widget, EditAccountView> {}
	
	@UiField TextBox accountName;
	@UiField ListBox accountType;
	@UiField ListBox accountCurrency;
	@UiField CheckBox accountClosed;
	@UiField TextBox bankName;
	@UiField TextBox bankCode;
	@UiField TextBox bankDesk;
	@UiField TextBox accountNumber;
	@UiField TextBox accountKey;
	@UiField TextBox initialAmount;
	@UiField TextBox minimalAmount;
	@UiField TextBox maximalAmount;
	@UiField Button resetButton;
	@UiField Button submitButton;

	@UiField(provided=true) Account account;
	
	public EditAccountView(Account account) {
		this.account = account;
		EditAccountResource.INSTANCE.style().ensureInjected();
		
		initWidget(uiBinder.createAndBindUi(this));
		
		EditAccountConstants constants = (EditAccountConstants) GWT.create(EditAccountConstants.class);
		accountKey.getElement().setAttribute("placeholder", constants.accountNumberKey());
	}

	@Override
	public HasClickHandlers getValidateButton() {
		return submitButton;
	}

	@Override
	public HasClickHandlers getResetButton() {
		return resetButton;
	}

}
