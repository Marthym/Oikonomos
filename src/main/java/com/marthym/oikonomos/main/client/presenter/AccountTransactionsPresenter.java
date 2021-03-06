package com.marthym.oikonomos.main.client.presenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.HasSelectionChangedHandlers;
import com.marthym.oikonomos.main.client.NomosInjector;
import com.marthym.oikonomos.main.client.event.AccountTransactionsDataLoadedEvent;
import com.marthym.oikonomos.main.client.event.AccountTransactionsDataLoadedEventHandler;
import com.marthym.oikonomos.main.client.i18n.AccountTransactionsConstants;
import com.marthym.oikonomos.shared.FieldVerifier;
import com.marthym.oikonomos.shared.model.Account;
import com.marthym.oikonomos.shared.model.PaiementMeans;
import com.marthym.oikonomos.shared.model.Payee;
import com.marthym.oikonomos.shared.model.dto.CategoryDTO;
import com.marthym.oikonomos.shared.model.dto.TransactionDTO;
import com.marthym.oikonomos.shared.services.TransactionServiceAsync;
import com.marthym.oikonomos.client.components.MessageFlyer;
import com.marthym.oikonomos.client.components.WaitingFlyer;
import com.marthym.oikonomos.client.i18n.OikonomosErrorMessages;
import com.marthym.oikonomos.client.presenter.Presenter;

public class AccountTransactionsPresenter implements Presenter {
	private static final Logger LOG = Logger.getLogger(AccountTransactionsPresenter.class.getName());
	
	public interface Display {
		Widget asWidget();
		
		Payee getTransactionPayee();
		CategoryDTO getTransactionCategory();
		HasValue<Date> getTransactionDate();
		HasValue<String> getTransactionDebit();
		HasValue<String> getTransactionCredit();
		HasValue<String> getTransactionPaiementMean();
		HasValue<String> getTransactionAccountingDocument();
		HasValue<String> getTransactionComment();
		HasValue<String> getTransactionBudgetaryLine();
		TransactionDTO getSelectedTransaction();
		void setSelectedTransaction(TransactionDTO object);
		
		void addTransactionGridLine(List<TransactionDTO> transaction);
		void setTransactionCategory(CategoryDTO category);
		void setTransactionPayee(Payee payee);
		void setTransactionFormVisisble(boolean isVisible);
		
		void reset();
		HasClickHandlers getValidateButton();
		HasClickHandlers getResetButton();
		HasSelectionChangedHandlers getTransactionsSelectionModel();
	}
	
	private final Display display;
	private static AccountTransactionsPresenter instance = null;
	private int currentTransactionIndex = -1;
	private List<TransactionDTO> transactions = null;
	private EventBus eventBus;
	
	@Inject private AccountTabbedPresenter parent;
	@Inject private OikonomosErrorMessages errorMessages;
	@Inject private AccountTransactionsConstants constants;
	@Inject private TransactionServiceAsync rpcTransaction;
		
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
		this.eventBus = eventBus;
		this.transactions = new ArrayList<TransactionDTO>();
		
		bind();
	}
	
	private void bind() {
		this.display.getValidateButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				saveTransactionFormFromView();
			}
		});

		this.display.getResetButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				display.reset();
				display.setSelectedTransaction(null);
				currentTransactionIndex = -1;
			}
		});
		
		this.display.getTransactionsSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override public void onSelectionChange(SelectionChangeEvent event) {
				LOG.fine("Select line ...");
				updateTransactionFormFromData(display.getSelectedTransaction());
				display.setTransactionFormVisisble(true);
			}
		});
		
		eventBus.addHandler(AccountTransactionsDataLoadedEvent.TYPE, new AccountTransactionsDataLoadedEventHandler() {
			@Override public void onAccountTransactionsDataLoaded(AccountTransactionsDataLoadedEvent event) {
				transactions = event.getTransactions();
				LOG.finer("AccountTransactionsDataLoadedEvent -> transactions.size: "+transactions.size());
				display.addTransactionGridLine(transactions);
			}
		});
	}
	
	private void updateTransactionFormFromData(TransactionDTO transaction) {
		display.reset();
		currentTransactionIndex = -1;
		if (transaction != null) {
			display.getTransactionDate().setValue(transaction.getTransactionDate());
			display.getTransactionAccountingDocument().setValue(transaction.getAccountingDocument());
			display.setTransactionCategory(transaction.getCategory());
			display.setTransactionPayee(transaction.getPayee());
			display.getTransactionComment().setValue(transaction.getTransactionComment());
			if (transaction.getCredit() != null)
				display.getTransactionCredit().setValue(Long.toString(transaction.getCredit()));
			if (transaction.getDebit() != null)
				display.getTransactionDebit().setValue(Long.toString(transaction.getDebit()));
			display.getTransactionPaiementMean().setValue(transaction.getPaiementMean().name());
			
			currentTransactionIndex = transactions.indexOf(transaction);
			LOG.finer("Find index "+currentTransactionIndex+" pour "+transaction.getId());
		}
	}
	
	private void saveTransactionFormFromView() {
		WaitingFlyer.start();
		List<String> errors = new LinkedList<String>();
		Account account = parent.getCurrentAccount();
		if (account == null) {
			errors.add(errorMessages.error_message_unexpected());
			errors.add(errorMessages.error_message_account_notfound());
		}
		TransactionDTO transaction = null;
		if (currentTransactionIndex < 0) {
			transaction = new TransactionDTO(account);
		} else {
			transaction = transactions.get(currentTransactionIndex);
		}
		
		transaction.setTransactionDate(display.getTransactionDate().getValue());
		transaction.setAccountingDocument(display.getTransactionAccountingDocument().getValue());
		transaction.setCategory(display.getTransactionCategory());
		//transaction.setBudgetLine(budgetLine); //TODO: Implement BudgetaryLine
		transaction.setPayee(display.getTransactionPayee());
		transaction.setTransactionComment(display.getTransactionComment().getValue());
		
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

		PaiementMeans paiementMean = PaiementMeans.valueOf(display.getTransactionPaiementMean().getValue());
		transaction.setPaiementMean(paiementMean);
		
		errors.addAll(FieldVerifier.validate(transaction));
		if (!errors.isEmpty()) {
			WaitingFlyer.stop();
			MessageFlyer.error(errors);
			return;
		}
		
		rpcTransaction.addOrUpdateEntity(transaction, new AsyncCallback<TransactionDTO>() {
			@Override public void onSuccess(TransactionDTO result) {
				LOG.finer("onSuccess !");
				if (currentTransactionIndex < 0) {
					transactions.add(result);
					currentTransactionIndex = -1;
					display.reset();
				}
				eventBus.fireEvent(new AccountTransactionsDataLoadedEvent(transactions));
				
				WaitingFlyer.stop();
			}
			
			@Override public void onFailure(Throwable caught) {
				WaitingFlyer.stop();
				MessageFlyer.error(caught.getLocalizedMessage());
			}
		});
		
	}
	
	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(display.asWidget());
	}
}
