package com.marthym.oikonomos.main.client.components;

import java.util.Date;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.datepicker.client.DateBox;
import com.marthym.oikonomos.client.i18n.OikonomosConstants;
import com.marthym.oikonomos.main.client.NomosInjector;
import com.marthym.oikonomos.main.client.i18n.AccountTransactionsConstants;
import com.marthym.oikonomos.main.client.view.EnumTypeTranslator;
import com.marthym.oikonomos.shared.model.PaiementMeans;
import com.marthym.oikonomos.shared.model.Payee;
import com.marthym.oikonomos.shared.model.dto.Category;

public class EditTransactionForm extends Composite {
	private static EditTransactionFormUiBinder uiBinder = GWT.create(EditTransactionFormUiBinder.class);
	interface EditTransactionFormUiBinder extends UiBinder<Widget, EditTransactionForm> {}
	private static final Logger LOG = Logger.getLogger(EditTransactionForm.class.getName());
	
	@UiField FormPanel formTransaction;
	@UiField DateBox transactionDate;
	@UiField(provided=true)
	 		 SuggestBox transactionPayee;
	@UiField TextBox transactionDebit;
	@UiField TextBox transactionCredit;
	@UiField(provided=true)
			 SuggestBox transactionCategory;
	@UiField SingleValueListBox transactionPaiementMean;
	@UiField TextBox transactionAccountingDocument;
	@UiField TextBox transactionComment;
	@UiField TextBox transactionBudgetaryLine;
	@UiField Button resetButton;
	@UiField Button submitButton;
	
	private Category selectedCategory;
	private Payee selectedPayee;
	
	private final OikonomosConstants oConstants = GWT.create(OikonomosConstants.class);
	private final AccountTransactionsConstants constants = GWT.create(AccountTransactionsConstants.class);
	private final SuggestOracle categoriesOracle = NomosInjector.INSTANCE.getCategoriesSuggestOracle();
	private final SuggestOracle payeesOracle = NomosInjector.INSTANCE.getPayeesSuggestOracle();
	
	public EditTransactionForm() {
		
		transactionCategory = new SuggestBox(categoriesOracle);
		transactionPayee = new SuggestBox(payeesOracle);
		initWidget(uiBinder.createAndBindUi(this));
		
		LOG.fine(oConstants.dateFormat());
		transactionDate.getElement().setAttribute("placeholder", constants.placeholder_date());
		transactionDate.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat(oConstants.dateFormat())));
		transactionDate.setValue(new Date());
		
		transactionPayee.getElement().setAttribute("placeholder", constants.placeholder_payee());
		transactionDebit.getElement().setAttribute("placeholder", constants.placeholder_debit());
		transactionCredit.getElement().setAttribute("placeholder", constants.placeholder_credit());
		transactionCategory.getElement().setAttribute("placeholder", constants.placeholder_category());
		transactionPaiementMean.getElement().setAttribute("placeholder", constants.placeholder_paiementMean());
		transactionAccountingDocument.getElement().setAttribute("placeholder", constants.placeholder_accountingDocument());
		transactionComment.getElement().setAttribute("placeholder", constants.placeholder_comment());
		transactionBudgetaryLine.getElement().setAttribute("placeholder", constants.placeholder_budgetaryLine());
		
		for (PaiementMeans mean : PaiementMeans.values()) {
			String translation = EnumTypeTranslator.getTranslation(mean);
			transactionPaiementMean.addItem(translation, mean.name());
		}
		
		transactionCategory.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				CategorySuggestion suggestion = (CategorySuggestion) event.getSelectedItem();
				selectedCategory = suggestion.getCategory();
			}
		});
		
		transactionPayee.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
			@Override public void onSelection(SelectionEvent<Suggestion> event) {
				PayeeSuggestion suggestion = (PayeeSuggestion) event.getSelectedItem();
				selectedPayee = suggestion.getPayee();
			}
		});
	}
	
	public Payee getSeletedPayee() {
		String value = transactionPayee.getValue();
		if (selectedPayee != null && selectedPayee.getName().equals(value)) {
			return selectedPayee;			
		} else if (!value.trim().isEmpty()) {
			return new Payee(value);
		} else {
			return null;
		}
	}
	
	public Category getSelectedCategory() {
		String value = transactionCategory.getValue();
		if (selectedCategory != null && selectedCategory.getAbsoluteDescription().equals(value)) {
			return selectedCategory;			
		} else {
			return null;
		}
	}

	public HasClickHandlers getValidateButton() {
		return submitButton;
	}

	public HasClickHandlers getResetButton() {
		return resetButton;
	}

	public FormPanel getForm() {
		return formTransaction;
	}
}
