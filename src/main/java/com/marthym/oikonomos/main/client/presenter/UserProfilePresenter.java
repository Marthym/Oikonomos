package com.marthym.oikonomos.main.client.presenter;

import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.i18n.shared.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import com.marthym.oikonomos.main.client.NomosInjector;
import com.marthym.oikonomos.main.client.OikonomosController;
import com.marthym.oikonomos.main.client.event.UserUpdateEvent;
import com.marthym.oikonomos.shared.services.UserServiceAsync;
import com.marthym.oikonomos.shared.exceptions.OikonomosUnauthorizedException;
import com.marthym.oikonomos.shared.model.User;
import com.marthym.oikonomos.client.components.MessageFlyer;
import com.marthym.oikonomos.client.components.WaitingFlyer;
import com.marthym.oikonomos.client.i18n.OikonomosErrorMessages;
import com.marthym.oikonomos.client.presenter.Presenter;

public class UserProfilePresenter implements Presenter {
	public interface Display {
		Widget asWidget();
		HasText getUserLogin();
		HasValue<String> getUserLastName();
		HasValue<String> getUserFirstName();
		HasValue<String> getUserPassword();
		HasValue<String> getUserPasswordConfirm();
		HasText getUserRegistrationDate();
		HasText getUserLastLoginDate();

		void reset();
		HasClickHandlers getValidateButton();
		HasClickHandlers getResetButton();
	}
	
	private final Display display;
	private final EventBus eventBus;
	private User authentifiedUser;
	
	@Inject private OikonomosErrorMessages errorMessages;
	@Inject private UserServiceAsync rcpUserService;

	public static void createAsync(final Presenter.Callback callback) {
		GWT.runAsync(new RunAsyncCallback() {
			
			@Override
			public void onSuccess() {
				UserProfilePresenter instance = NomosInjector.INSTANCE.getUserProfilePresenter();
				callback.onCreate(instance);
			}
			
			@Override
			public void onFailure(Throwable reason) {
				callback.onCreateFailed();
			}
		});
	}
	
	@Inject
	private UserProfilePresenter(EventBus eventBus, Display display) {
		this.eventBus = eventBus;
		this.display = display;
		this.authentifiedUser = OikonomosController.getAuthentifiedUser();
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
		
		updateViewFromData();
	}
	
	private void updateViewFromData() {
		display.reset();
		
		DateTimeFormat format = DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM);
		
		display.getUserLogin().setText(authentifiedUser.getUserEmail());
		if (authentifiedUser.getUserFirstname() != null) display.getUserFirstName().setValue(authentifiedUser.getUserFirstname());
		if (authentifiedUser.getUserLastname() != null) display.getUserLastName().setValue(authentifiedUser.getUserLastname());
		display.getUserRegistrationDate().setText(format.format(authentifiedUser.getUserRegistrationDate()));
		display.getUserLastLoginDate().setText(format.format(authentifiedUser.getUserLastLoginDate()));
	}
	
	private void saveDataFromView() {
		WaitingFlyer.start();
		
		String newPassord = null;
		if (display.getUserPassword() != null && !display.getUserPassword().getValue().isEmpty()) {
			String password = display.getUserPassword().getValue();
			if(!password.equals(display.getUserPasswordConfirm().getValue())) {
				MessageFlyer.error(errorMessages.error_message_user_passwordConfirm());
				return;
			} else {
				newPassord = password;
			}
		}
		
		// Save User
		rcpUserService.updateUser(
				authentifiedUser.getUserEmail(), 
				display.getUserFirstName().getValue(), 
				display.getUserLastName().getValue(), 
				newPassord, 
				authentifiedUser.getUserLastLoginDate(),
				new AsyncCallback<User>() {

					@Override
					public void onFailure(Throwable caught) {
						WaitingFlyer.stop();
						if(caught instanceof OikonomosUnauthorizedException) { 
							OikonomosController.redirectToLogin();
						}
						MessageFlyer.error(caught.getLocalizedMessage());
					}

					@Override
					public void onSuccess(User result) {
						authentifiedUser = result;
						eventBus.fireEvent(new UserUpdateEvent(authentifiedUser));
						WaitingFlyer.stop();
						MessageFlyer.info(
								errorMessages.info_message_entity_saveSuccessfully().replace("{0}", authentifiedUser.getUserEmail()));
					}
				});
		
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(display.asWidget());
	}

}
