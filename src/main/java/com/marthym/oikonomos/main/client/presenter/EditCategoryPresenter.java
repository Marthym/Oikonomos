package com.marthym.oikonomos.main.client.presenter;

import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.marthym.oikonomos.main.client.NomosInjector;
import com.marthym.oikonomos.main.client.OikonomosController;
import com.marthym.oikonomos.main.client.event.LeftmenuEntityChangeEvent;
import com.marthym.oikonomos.main.client.services.CategoryServiceAsync;
import com.marthym.oikonomos.shared.exceptions.OikonomosUnauthorizedException;
import com.marthym.oikonomos.shared.model.User;
import com.marthym.oikonomos.shared.model.dto.Category;
import com.marthym.oikonomos.client.components.MessageFlyer;
import com.marthym.oikonomos.client.components.WaitingFlyer;
import com.marthym.oikonomos.client.i18n.OikonomosErrorMessages;
import com.marthym.oikonomos.client.presenter.Presenter;

public class EditCategoryPresenter implements Presenter {
	public interface Display {
		Widget asWidget();
		HasText getCategoryId();
		HasValue<String> getCategoryDescription();
		
		void setCategoryPrivate(boolean isPrivate);
		void reset();
		HasClickHandlers getValidateButton();
		HasClickHandlers getResetButton();
	}
	
	private final Display display;
	private final EventBus eventBus;
	private static EditCategoryPresenter instance = null;
	private Category category;
	
	@Inject private OikonomosErrorMessages errorMessages;
	@Inject private CategoryServiceAsync rpcCategoryService;

	public static void createAsync(final Presenter.Callback callback) {
		GWT.runAsync(new RunAsyncCallback() {
			
			@Override
			public void onSuccess() {
				if (instance == null) {
					instance = NomosInjector.INSTANCE.getEditCategoryPresenter();
				}
				
				String[] splitHistoryToken = History.getToken().split("\\|");
				try {
					long accountId = Long.parseLong(splitHistoryToken[1]);
					instance.getRemoteData(accountId, callback);
				} catch (Exception e) {
					User authentifiedUser = OikonomosController.getAuthentifiedUser();
					instance.category = new Category();
					instance.category.setEntityOwner(authentifiedUser.getUserEmail());
					instance.updateViewFromData();
					callback.onCreate(instance);
				}
			}
			
			@Override
			public void onFailure(Throwable reason) {
				callback.onCreateFailed();
			}
		});
	}
	
	@Inject
	private EditCategoryPresenter(EventBus eventBus, Display display) {
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
		
		updateViewFromData();
	}

	private final void getRemoteData(long categoryId, final Presenter.Callback callback) {
		rpcCategoryService.getEntityWithChild(categoryId, LocaleInfo.getCurrentLocale().getLocaleName(), new AsyncCallback<Category>() {
			@Override
			public void onSuccess(Category result) {
				category = result;
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
		
		if (category == null) return;
		
		if (category.getEntityId() != null)
			display.getCategoryId().setText(category.getEntityId().toString());

		if (category.getEntityOwner() == null || category.getEntityOwner().isEmpty())
			display.setCategoryPrivate(false);

		display.getCategoryDescription().setValue(category.getEntityDescription());
		
	}
	
	private void saveDataFromView() {
		if (display.getCategoryDescription().getValue().isEmpty()) {
			MessageFlyer.error(errorMessages.error_message_category_mandatoryDescription());
		}
		
		WaitingFlyer.start();
		category.setEntityDescription(display.getCategoryDescription().getValue());
		
		rpcCategoryService.addOrUpdateEntity(category, LocaleInfo.getCurrentLocale().getLocaleName(), new AsyncCallback<Category>() {

					@Override
					public void onFailure(Throwable caught) {
						WaitingFlyer.stop();
						if(caught instanceof OikonomosUnauthorizedException) { 
							OikonomosController.redirectToLogin();
						}
						MessageFlyer.error(caught.getLocalizedMessage());
					}

					@Override
					public void onSuccess(Category result) {
						category = result;
						eventBus.fireEvent(new LeftmenuEntityChangeEvent(category));
						History.newItem(result.getEntityType().name().toLowerCase()+"|"+result.getEntityId());
						WaitingFlyer.stop();
						MessageFlyer.info(
								errorMessages.info_message_entity_saveSuccessfully().replace("{0}", category.getEntityDescription()));
					}
				});
		
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(display.asWidget());
	}

}
