package com.marthym.oikonomos.main.client.presenter;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.marthym.oikonomos.main.client.NomosInjector;
import com.marthym.oikonomos.main.client.event.AccountDataUpdateEvent;
import com.marthym.oikonomos.main.client.event.AccountDataUpdateEventHandler;
import com.marthym.oikonomos.main.client.event.LeftmenuEntityChangeEvent;
import com.marthym.oikonomos.main.client.i18n.EditAccountConstants;
import com.marthym.oikonomos.shared.FieldVerifier;
import com.marthym.oikonomos.shared.services.AccountServiceAsync;
import com.marthym.oikonomos.shared.model.Account;
import com.marthym.oikonomos.shared.model.AccountType;
import com.marthym.oikonomos.client.components.MessageFlyer;
import com.marthym.oikonomos.client.components.WaitingFlyer;
import com.marthym.oikonomos.client.i18n.OikonomosErrorMessages;
import com.marthym.oikonomos.client.presenter.Presenter;

public class EditAccountPresenter implements Presenter {
	private static final Logger LOG = Logger.getLogger(EditAccountPresenter.class.getName());
	
	public interface Display {
		Widget asWidget();
		HasValue<String> getAccountName();
		HasValue<String> getAccountType();
		HasValue<String> getAccountCurrency();
		HasValue<Boolean> getAccountClosed();
		HasValue<String> getBankName();
		HasValue<String> getBankCode();
		HasValue<String> getBankDesk();
		HasValue<String> getAccountNumber();
		HasValue<String> getAccountKey();
		HasValue<String> getInitialAmount();
		HasValue<String> getMinimalAmount();
		HasValue<String> getMaximalAmount();

		void setEnableInitialAmount(boolean isEnable);
		void reset();
		HasClickHandlers getValidateButton();
		HasClickHandlers getResetButton();
	}
	
	private final Display display;
	private final EventBus eventBus;
	private static EditAccountPresenter instance = null;
	
	@Inject private AccountTabbedPresenter parent;
	@Inject private OikonomosErrorMessages errorMessages;
	@Inject private EditAccountConstants constants;
	@Inject private AccountServiceAsync rcpAccountService;
	
	public static EditAccountPresenter create(HasWidgets container) {
		if (instance == null) {
			instance = NomosInjector.INSTANCE.getEditAccountPresenter();
			instance.updateViewFromData();
		}
		
		instance.go(container);
		
		return instance;
	}
	
	@Inject
	private EditAccountPresenter(EventBus eventBus, Display display) {
		this.eventBus = eventBus;
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
		
		eventBus.addHandler(AccountDataUpdateEvent.TYPE, new AccountDataUpdateEventHandler() {
			@Override
			public void onAccountDataUpdate(AccountDataUpdateEvent event) {
				updateViewFromData();
			}
		});
	}
	
	private void updateViewFromData() {
		display.reset();
		
		LOG.finer("updateViewFromData()-> Look for account ...");
		Account account = parent.getCurrentAccount();
		LOG.finer("updateViewFromData()-> ... account found: "+account);
		
		if (account == null) return;
		LOG.finer("updateViewFromData()-> ... id: "+account.getId());

		if (account.getAccountName() != null) display.getAccountName().setValue(account.getAccountName());
		if (account.getAccountType() != null) display.getAccountType().setValue(account.getAccountType().name());
		if (account.getAccountCurrency() != null) display.getAccountCurrency().setValue(account.getAccountCurrency());
		display.getAccountClosed().setValue(account.isClosed());
		if (account.getBankName() != null) display.getBankName().setValue(account.getBankName());
		if (account.getBankCode() > -1) display.getBankCode().setValue(Integer.toString(account.getBankCode()));
		if (account.getBankDesk() > -1) display.getBankDesk().setValue(Integer.toString(account.getBankDesk()));
		if (account.getAccountNumber() > -1) display.getAccountNumber().setValue(Long.toString(account.getAccountNumber()));
		if (account.getAccountKey() > -1) display.getAccountKey().setValue(Long.toString(account.getAccountKey()));
		if (account.getInitialAmount() > -1) {
			display.getInitialAmount().setValue(Double.toString(account.getInitialAmount()));
			display.setEnableInitialAmount(false);
		}
		if (account.getMinimalAmount() > -1) display.getMinimalAmount().setValue(Double.toString(account.getMinimalAmount()));
		if (account.getMaximalAmount() > -1) display.getMaximalAmount().setValue(Double.toString(account.getMaximalAmount()));
	}
	
	private void saveDataFromView() {
		WaitingFlyer.start();
		List<String> errors = new LinkedList<String>();
		
		Account account = parent.getCurrentAccount();
		
		account.setAccountName(display.getAccountName().getValue());
		
		final AccountType accountType = AccountType.valueOf(display.getAccountType().getValue());
		account.setAccountType(accountType);
		account.setAccountCurrency(display.getAccountCurrency().getValue());
		account.setClosed(display.getAccountClosed().getValue());
		account.setBankName(display.getBankName().getValue());
		try {
			String bankCode = display.getBankCode().getValue();
			if (!bankCode.isEmpty()) account.setBankCode(Integer.parseInt(bankCode));
		} catch (NumberFormatException e) {
			errors.add(errorMessages.error_message_numberformat().replace("{0}", constants.bankCodeLabel()));
		}

		try {
			String bankDesk = display.getBankDesk().getValue();
			if (!bankDesk.isEmpty()) account.setBankDesk(Integer.parseInt(bankDesk));
		} catch (NumberFormatException e) {
			errors.add(errorMessages.error_message_numberformat().replace("{0}", constants.bankDeskLabel()));
		}

		try {
			String accountNumber = display.getAccountNumber().getValue();
			if (!accountNumber.isEmpty()) 
				account.setAccountNumber(Long.parseLong(accountNumber));
		} catch (NumberFormatException e) {
			errors.add(errorMessages.error_message_numberformat().replace("{0}", constants.accountNumberLabel()));
		}

		try {
			String accountKey = display.getAccountKey().getValue();
			if (!accountKey.isEmpty()) 
				account.setAccountKey(Integer.parseInt(accountKey));
		} catch (NumberFormatException e) {
			errors.add(errorMessages.error_message_numberformat().replace("{0}", constants.accountNumberKey()));
		}

		try{
			long initialAmount = Long.parseLong(display.getInitialAmount().getValue());
			if (account.getInitialAmount() < 0)
				account.setInitialAmount(initialAmount);
		} catch (NumberFormatException e) {
			errors.add(errorMessages.error_message_numberformat().replace("{0}", constants.initialAmountLabel()));
		}

		try {
			String minimalAmount = display.getMinimalAmount().getValue();
			if (!minimalAmount.isEmpty()) account.setMinimalAmount(Double.parseDouble(minimalAmount));
		} catch (NumberFormatException e) {
			errors.add(errorMessages.error_message_numberformat().replace("{0}", constants.minimalAmountLabel()));
		}

		try {
			String maximalAmount = display.getMaximalAmount().getValue();
			if (!maximalAmount.isEmpty()) account.setMaximalAmount(Double.parseDouble(maximalAmount));
		} catch (NumberFormatException e) {
			errors.add(errorMessages.error_message_numberformat().replace("{0}", constants.maximalAmountLabel()));
		}
		
		
		// Check validation for Account
		errors.addAll(FieldVerifier.validate(account));
		if (!errors.isEmpty()) {
			WaitingFlyer.stop();
			MessageFlyer.error(errors);
			return;
		}
		
		// Save Account
		rcpAccountService.addOrUpdateEntity(account, new AsyncCallback<Account>() {
			
			@Override
			public void onSuccess(Account result) {
				Account account = result;
				NomosInjector.INSTANCE.getAccountTabbedPresenter().setCurrentAccount(account);
				eventBus.fireEvent(new LeftmenuEntityChangeEvent(account));
				History.newItem(result.getEntityType().name().toLowerCase()+"|"+result.getEntityId());
				WaitingFlyer.stop();
				MessageFlyer.info(
						errorMessages.info_message_entity_saveSuccessfully().replace("{0}", account.getAccountName()));
			}
			
			@Override
			public void onFailure(Throwable caught) {
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
