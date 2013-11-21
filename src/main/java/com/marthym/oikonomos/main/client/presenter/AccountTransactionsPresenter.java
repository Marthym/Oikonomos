package com.marthym.oikonomos.main.client.presenter;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.marthym.oikonomos.main.client.NomosInjector;
import com.marthym.oikonomos.main.client.i18n.AccountTransactionsConstants;
import com.marthym.oikonomos.shared.FieldVerifier;
import com.marthym.oikonomos.shared.model.Account;
import com.marthym.oikonomos.shared.model.PaiementMeans;
import com.marthym.oikonomos.shared.model.Payee;
import com.marthym.oikonomos.shared.model.Transaction;
import com.marthym.oikonomos.shared.model.dto.Category;
import com.marthym.oikonomos.client.components.MessageFlyer;
import com.marthym.oikonomos.client.components.WaitingFlyer;
import com.marthym.oikonomos.client.i18n.OikonomosErrorMessages;
import com.marthym.oikonomos.client.presenter.Presenter;

public class AccountTransactionsPresenter implements Presenter {
	
	public interface Display {
		Widget asWidget();
		
		Payee getTransactionPayee();
		Category getTransactionCategory();
		HasValue<Date> getTransactionDate();
		HasValue<String> getTransactionDebit();
		HasValue<String> getTransactionCredit();
		HasValue<String> getTransactionPaiementMean();
		HasValue<String> getTransactionAccountingDocument();
		HasValue<String> getTransactionComment();
		HasValue<String> getTransactionBudgetaryLine();
		
		void reset();
		HasClickHandlers getValidateButton();
		HasClickHandlers getResetButton();
	}
	
	private final Display display;
	private static AccountTransactionsPresenter instance = null;
	private Transaction transaction = null;
	
	@Inject private AccountTabbedPresenter parent;
	@Inject private OikonomosErrorMessages errorMessages;
	@Inject private AccountTransactionsConstants constants;
		
	public static AccountTransactionsPresenter create(HasWidgets container) {
		if (instance == null) {
			instance = NomosInjector.INSTANCE.getAccountTransactionsPresenter();
		}
		
		instance.go(container);
		
		return instance;
	}
	
	@Inject
	private AccountTransactionsPresenter(EventBus eventBus, Display display) {
		this.display = display;
		
		bind();
	}
	
	private void bind() {
		this.display.getValidateButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				saveDataFromView();
			}
		});

		this.display.getResetButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				updateViewFromData();
			}
		});		
	}
	
	private void updateViewFromData() {
		display.reset();
	}
	
	private void saveDataFromView() {
		WaitingFlyer.start();
		List<String> errors = new LinkedList<String>();
		Account account = parent.getCurrentAccount();
		if (account == null) {
			errors.add(errorMessages.error_message_unexpected());
			errors.add(errorMessages.error_message_account_notfound());
		}
		if (transaction == null) transaction = new Transaction(account);
		
		transaction.setDate(display.getTransactionDate().getValue());
		transaction.setAccountingDocument(display.getTransactionAccountingDocument().getValue());
		//transaction.setBudgetLine(budgetLine); //TODO: Implement BudgetaryLine
		transaction.setCategory(display.getTransactionCategory().getEntityId());
		transaction.setPayee(display.getTransactionPayee());
		transaction.setComment(display.getTransactionComment().getValue());
		
		try {
			String credit = display.getTransactionCredit().getValue();
			if (!credit.isEmpty()) transaction.setCredit(Long.parseLong(credit));
		} catch (NumberFormatException e) {
			errors.add(errorMessages.error_message_numberformat().replace("{0}", constants.placeholder_credit()));
		}
		
		try {
			String debit = display.getTransactionDebit().getValue();
			if (!debit.isEmpty()) transaction.setDebit(Long.parseLong(debit));
		} catch (NumberFormatException e) {
			errors.add(errorMessages.error_message_numberformat().replace("{0}", constants.placeholder_debit()));
		}

		final PaiementMeans paiementMean = PaiementMeans.valueOf(display.getTransactionPaiementMean().getValue());
		transaction.setPaiementMean(paiementMean);
		
		errors.addAll(FieldVerifier.validate(transaction));
		if (!errors.isEmpty()) {
			WaitingFlyer.stop();
			MessageFlyer.error(errors);
			return;
		}
		
		
		
	}
	
	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(display.asWidget());
	}

}
