package com.marthym.oikonomos.main.client.presenter;

import java.util.List;

import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.marthym.oikonomos.main.client.NomosInjector;
import com.marthym.oikonomos.main.client.OikonomosController;
import com.marthym.oikonomos.main.client.event.LeftmenuEntityChangeEvent;
import com.marthym.oikonomos.shared.FieldVerifier;
import com.marthym.oikonomos.shared.exceptions.OikonomosUnauthorizedException;
import com.marthym.oikonomos.shared.model.Payee;
import com.marthym.oikonomos.shared.services.PayeeServiceAsync;
import com.marthym.oikonomos.shared.view.data.EntityType;
import com.marthym.oikonomos.client.components.MessageFlyer;
import com.marthym.oikonomos.client.components.WaitingFlyer;
import com.marthym.oikonomos.client.i18n.OikonomosErrorMessages;
import com.marthym.oikonomos.client.presenter.Presenter;

public class EditPayeePresenter implements Presenter {
	
	public interface Display {
		Widget asWidget();
		HasText getPayeeId();
		HasValue<String> getPayeeName();
		void reset();
		HasClickHandlers getValidateButton();
		HasClickHandlers getResetButton();
		HasClickHandlers getDeleteButton();
		void setDeleteVisible(boolean isVisible);
	}
	
	private final Display display;
	private final EventBus eventBus;
	private static EditPayeePresenter instance = null;
	private Payee payee;
	
	@Inject private OikonomosErrorMessages errorMessages;
	@Inject private PayeeServiceAsync rpcPayeeService;

	public static void createAsync(final Presenter.Callback callback) {
		GWT.runAsync(new RunAsyncCallback() {
			
			@Override
			public void onSuccess() {
				WaitingFlyer.start();

				if (instance == null) {
					instance = NomosInjector.INSTANCE.getEditPayeePresenter();
				}
				
				String[] splitHistoryToken = History.getToken().split(DashboardPresenter.HISTORY_PARAM_SEPARATOR);
				try {
					long payeeId = Long.parseLong(splitHistoryToken[1]);
					instance.getRemoteData(payeeId, callback);
				} catch (Exception e) {
					instance.payee = new Payee();
					instance.updateViewFromData();
					callback.onCreate(instance);
				}
				WaitingFlyer.stop();
			}
			
			@Override
			public void onFailure(Throwable reason) {
				WaitingFlyer.stop();
				callback.onCreateFailed();
			}
		});
	}
	
	@Inject
	private EditPayeePresenter(EventBus eventBus, Display display) {
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
		
		this.display.getDeleteButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				onDeletePayee();
			}
		});

		updateViewFromData();
	}

	private final void getRemoteData(long payeeId, final Presenter.Callback callback) {
		rpcPayeeService.find(payeeId, new AsyncCallback<Payee>() {
			@Override
			public void onSuccess(Payee result) {
				payee = result;
				updateViewFromData();
				callback.onCreate(instance);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				WaitingFlyer.stop();
				MessageFlyer.error(caught.getLocalizedMessage());
			}
		});		
	}
	
	private void updateViewFromData() {
		display.reset();
		
		if (payee == null) return;
		
		if (payee.getEntityId() != null) {
			display.getPayeeId().setText(payee.getEntityId().toString());
			display.setDeleteVisible(true);
		} else {
			display.setDeleteVisible(false);
		}

		display.getPayeeName().setValue(payee.getEntityDescription());
		
	}
	
	private void saveDataFromView() {
		WaitingFlyer.start();
		payee.setName(display.getPayeeName().getValue());
				
		List<String> errors = FieldVerifier.validate(payee);
		if (!errors.isEmpty()) {
			WaitingFlyer.stop();
			MessageFlyer.error(errors);
			return;
		}

		rpcPayeeService.addOrUpdateEntity(payee, new AsyncCallback<Payee>() {

					@Override
					public void onFailure(Throwable caught) {
						WaitingFlyer.stop();
						if(caught instanceof OikonomosUnauthorizedException) { 
							OikonomosController.redirectToLogin();
						}
						MessageFlyer.error(caught.getLocalizedMessage());
					}

					@Override
					public void onSuccess(Payee result) {
						payee = result;
						eventBus.fireEvent(new LeftmenuEntityChangeEvent(payee));
						History.newItem(result.getEntityType().name().toLowerCase()+"|"+result.getEntityId());
						WaitingFlyer.stop();
						MessageFlyer.info(
								errorMessages.info_message_entity_saveSuccessfully().replace("{0}", payee.getEntityDescription()));
					}
				});
		
	}
	
	private void onDeletePayee() {
		WaitingFlyer.start();
		final String payeeName = payee.getEntityDescription();
		rpcPayeeService.delete(payee.getEntityId(), new AsyncCallback<Void>() {

			@Override public void onFailure(Throwable caught) {
				WaitingFlyer.stop();
				MessageFlyer.error(caught.getLocalizedMessage());
			}

			@Override public void onSuccess(Void result) {
				History.newItem(EntityType.PAYEE.name().toLowerCase());
				eventBus.fireEvent(new LeftmenuEntityChangeEvent(payee));
				WaitingFlyer.stop();
				MessageFlyer.info(
						errorMessages.info_message_entity_deleteSuccessfully().replace("{0}", payeeName));
			}
		});
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(display.asWidget());
	}
}
