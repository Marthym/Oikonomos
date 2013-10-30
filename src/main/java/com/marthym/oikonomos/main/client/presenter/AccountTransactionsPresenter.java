package com.marthym.oikonomos.main.client.presenter;

import javax.inject.Inject;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.marthym.oikonomos.main.client.NomosInjector;
import com.marthym.oikonomos.main.client.i18n.AccountTransactionsConstants;
import com.marthym.oikonomos.shared.services.AccountServiceAsync;
import com.marthym.oikonomos.client.i18n.OikonomosErrorMessages;
import com.marthym.oikonomos.client.presenter.Presenter;

public class AccountTransactionsPresenter implements Presenter {
	
	public interface Display {
		Widget asWidget();
		
		void reset();
		HasClickHandlers getValidateButton();
		HasClickHandlers getResetButton();
	}
	
	private final Display display;
	private final EventBus eventBus;
	private static AccountTransactionsPresenter instance = null;
	
	@Inject private OikonomosErrorMessages errorMessages;
	@Inject private AccountTransactionsConstants constants;
	@Inject private AccountServiceAsync rcpAccountService;
	
	public static AccountTransactionsPresenter create(HasWidgets container) {
		if (instance == null) {
			instance = NomosInjector.INSTANCE.getAccountTransactionsPresenter();
		}
		
		instance.go(container);
		
		return instance;
	}
	
	@Inject
	private AccountTransactionsPresenter(EventBus eventBus, Display display) {
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
	}
	
	private void updateViewFromData() {
		display.reset();
	}
	
	private void saveDataFromView() {
		
	}
	
	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(display.asWidget());
	}

}