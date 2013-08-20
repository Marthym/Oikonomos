package com.marthym.oikonomos.main.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.marthym.oikonomos.main.client.components.SingleValueListBox;
import com.marthym.oikonomos.main.client.i18n.EditAccountConstants;
import com.marthym.oikonomos.main.client.presenter.EditAccountPresenter;
import com.marthym.oikonomos.main.client.resources.EditAccountResource;
import com.marthym.oikonomos.shared.model.AccountType;

public class EditAccountView extends Composite implements EditAccountPresenter.Display {
	private static EditAccountViewUiBinder uiBinder = GWT.create(EditAccountViewUiBinder.class);

	interface EditAccountViewUiBinder extends UiBinder<Widget, EditAccountView> {}
	
	@UiField TextBox accountName;
	@UiField SingleValueListBox accountType;
	@UiField SingleValueListBox accountCurrency;
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

	public EditAccountView() {
		EditAccountResource.INSTANCE.style().ensureInjected();
		
		initWidget(uiBinder.createAndBindUi(this));
		
		EditAccountConstants constants = (EditAccountConstants) GWT.create(EditAccountConstants.class);
		accountKey.getElement().setAttribute("placeholder", constants.accountNumberKey());
		
		for (AccountType type : AccountType.values()) {
			String translation = EnumTypeTranslator.getTranslation(type);
			accountType.addItem(translation, type.name());
		}
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
	public HasValue<String> getAccountName() { return accountName; }

	@Override
	public HasValue<String> getAccountType() { return accountType; }

	@Override
	public HasValue<String> getAccountCurrency() { return accountCurrency;}

	@Override
	public HasValue<Boolean> getAccountClosed() { return accountClosed; }

	@Override
	public HasValue<String> getBankName() { return bankName; }

	@Override
	public HasValue<String> getBankCode() { return bankCode; }

	@Override
	public HasValue<String> getBankDesk() { return bankDesk; }

	@Override
	public HasValue<String> getAccountNumber() { return accountNumber; }

	@Override
	public HasValue<String> getAccountKey() { return accountKey; }

	@Override
	public HasValue<String> getInitialAmount() { return initialAmount; }

	@Override
	public HasValue<String> getMinimalAmount() { return minimalAmount; }

	@Override
	public HasValue<String> getMaximalAmount() { return maximalAmount; }

}
