package com.marthym.oikonomos.client.presenter;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.validation.client.impl.Validation;
import com.marthym.oikonomos.client.components.AnonymousNavigationBar;
import com.marthym.oikonomos.client.components.MessageFlyer;
import com.marthym.oikonomos.client.components.AnonymousNavigationBar.ActivationType;
import com.marthym.oikonomos.client.services.UserServiceAsync;
import com.marthym.oikonomos.client.view.WelcomeView.DeckWidgetIndex;
import com.marthym.oikonomos.shared.exceptions.OikonomosException;
import com.marthym.oikonomos.shared.model.User;

public class WelcomePresenter implements Presenter {
	public interface Display {
		HasClickHandlers getRegisterButton();
		HasClickHandlers getLoginButton();
		HasClickHandlers getLoginLink();
		HasClickHandlers getRegisterLink();
		HasValue<String> getFirstName();
		HasValue<String> getLastName();
		HasValue<String> getEmailAddress();
		HasValue<String> getPassword();
		HasValue<String> getPasswordConfirm();
		DeckLayoutPanel  getDeckLayoutPanel(); 
		AnonymousNavigationBar getNavigationBar();
		Widget asWidget();
	}

	private final UserServiceAsync rpcService;
	private final HandlerManager eventBus;
	private final Display display;

	public WelcomePresenter(HandlerManager eventBus, Display display) {
		this.rpcService = UserServiceAsync.Util.getInstance();
		this.eventBus = eventBus;
		this.display = display;
		bind();
	}

	private void bind() {
		this.display.getLoginButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// TODO: Implement
			}
		});

		this.display.getRegisterButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				doRegister();
			}
		});
		
		this.display.getLoginLink().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				displayLogin();
			}
		});
		
		this.display.getRegisterLink().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				displayRegister();	
			}
		});
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(display.asWidget());
		
		String token = History.getToken();
		if ("login".equals(token)) {
			displayLogin();
			
		} else if ("register".equals(token)) {
			displayRegister();
			
		}
	}
	
	private void displayLogin() {
		display.getNavigationBar().toggleMenuActivation(ActivationType.CONNECT);
		display.getDeckLayoutPanel().showWidget(DeckWidgetIndex.CONNECT_WIDGET.ordinal());
	}
	
	private void displayRegister() {
		display.getNavigationBar().toggleMenuActivation(ActivationType.REGISTER);
		display.getDeckLayoutPanel().showWidget(DeckWidgetIndex.REGISTER_WIDGET.ordinal());
	}
	
	private void doRegister() {
		User user = new User(
				display.getEmailAddress().getValue(), 
				display.getFirstName().getValue(), 
				display.getLastName().getValue(), 
				display.getPassword().getValue());
		
		
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<User>> violations = validator.validate(user);
		if (!violations.isEmpty()) {
			List<String> errors = new LinkedList<String>();
			for (ConstraintViolation<User> violation : violations) {
				errors.add(violation.getMessage());
			}
			// Affiche un message d'erreur avec les contraintes non respectées
			MessageFlyer.error(errors);
			return;
		}	
		
		rpcService.saveUser(user, new AsyncCallback<User>() {
				public void onFailure(Throwable caught) {
					if (caught instanceof OikonomosException) {
						MessageFlyer.error(((OikonomosException)caught).getLocalizedMessage());
					}
				}
				public void onSuccess(User user) {
					displayLogin();
					MessageFlyer.info("Utilisateur "+user.getUserEmail()+" enregisté !");
				}
			});

	}
}
