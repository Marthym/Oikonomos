package com.marthym.oikonomos.main.client.view;

import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.marthym.oikonomos.main.client.components.EditTransactionForm;
import com.marthym.oikonomos.main.client.i18n.AccountTransactionsConstants;
import com.marthym.oikonomos.main.client.presenter.AccountTransactionsPresenter;
import com.marthym.oikonomos.main.client.resources.MainFormViewResource;

public class AccountTransactionsView extends Composite implements AccountTransactionsPresenter.Display {
	private static AccountTransactionsViewUiBinder uiBinder = GWT.create(AccountTransactionsViewUiBinder.class);

	interface AccountTransactionsViewUiBinder extends UiBinder<Widget, AccountTransactionsView> {}
	
	@UiField Grid transactionsGrid;
	@UiField DisclosurePanel formDisclosure;
	@UiField EditTransactionForm transactionForm;
	@UiField VerticalPanel vertical;
	
	@Inject AccountTransactionsConstants constants;
	
	public AccountTransactionsView() {
		MainFormViewResource.INSTANCE.style().ensureInjected();
		
		initWidget(uiBinder.createAndBindUi(this));
		
		vertical.setCellHeight(vertical.getWidget(0), "100%");
	}

	@Override
	public void reset() {
		transactionForm.getForm().reset();
	}

	@Override
	public HasClickHandlers getValidateButton() {
		return transactionForm.getValidateButton();
	}

	@Override
	public HasClickHandlers getResetButton() {
		return transactionForm.getResetButton();
	}

}
