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
import com.marthym.oikonomos.shared.model.AccountType;

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
	public void updateViewData(Account data) {
		if (data == null) return;
		if (data.getAccountName() != null) accountName.setValue(data.getAccountName());
		if (data.getAccountType() != null) accountType.setSelectedIndex(data.getAccountType().ordinal());
		if (data.getAccountCurrency() != null) {
			int indexToFind = -1;
			for (int i=0; i<accountCurrency.getItemCount(); i++) {
			    if (accountCurrency.getItemText(i).equals(data.getAccountCurrency())) {
			        indexToFind = i;
			        break;
			    }
			}
			accountCurrency.setSelectedIndex(indexToFind);
		}
		accountClosed.setValue(data.isClosed());
		if (data.getBankName() != null) bankName.setValue(data.getBankName());
		if (data.getBankCode() > -1) bankCode.setValue(Integer.toString(data.getBankCode()));
		if (data.getBankDesk() > -1) bankDesk.setValue(Integer.toString(data.getBankDesk()));
		if (data.getAccountNumber() > -1) accountNumber.setValue(Long.toString(data.getAccountNumber()));
		if (data.getAccountKey() > -1) accountKey.setValue(Long.toString(data.getAccountKey()));
		if (data.getInitialAmount() > -1) initialAmount.setValue(Double.toString(data.getInitialAmount()));
		if (data.getMinimalAmount() > -1) minimalAmount.setValue(Double.toString(data.getMinimalAmount()));
		if (data.getMaximalAmount() > -1) maximalAmount.setValue(Double.toString(data.getMaximalAmount()));
	}

}
