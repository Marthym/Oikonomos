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
import com.marthym.oikonomos.main.client.presenter.EditAccountPresenter;

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
	@UiField TextBox initialAmount;
	@UiField TextBox minimalAmount;
	@UiField TextBox maximalAmount;
	@UiField Button resetButton;
	@UiField Button submitButton;

	public EditAccountView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public EditAccountView(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
		
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
