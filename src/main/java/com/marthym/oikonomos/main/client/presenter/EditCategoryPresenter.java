package com.marthym.oikonomos.main.client.presenter;

import java.util.List;
import java.util.logging.Logger;

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
import com.marthym.oikonomos.shared.exceptions.OikonomosUnauthorizedException;
import com.marthym.oikonomos.shared.model.User;
import com.marthym.oikonomos.shared.model.dto.Category;
import com.marthym.oikonomos.shared.services.CategoryServiceAsync;
import com.marthym.oikonomos.shared.view.data.EntityType;
import com.marthym.oikonomos.client.components.MessageFlyer;
import com.marthym.oikonomos.client.components.WaitingFlyer;
import com.marthym.oikonomos.client.i18n.OikonomosErrorMessages;
import com.marthym.oikonomos.client.presenter.Presenter;

public class EditCategoryPresenter implements Presenter {
	private static final Logger LOG = Logger.getLogger(EditCategoryPresenter.class.getName());
	
	public interface Display {
		Widget asWidget();
		HasText getCategoryId();
		HasValue<String> getCategoryDescription();
		HasValue<String> getCategoryParent();
		void enableCategoryParent(boolean isEnable);
		boolean populateParentList(List<Category> parents);
		void hideCurrentOption(String valueHide);
		void setCategoryPrivate(boolean isPrivate);
		void reset();
		HasClickHandlers getValidateButton();
		HasClickHandlers getResetButton();
		HasClickHandlers getDeleteButton();
	}
	
	private final Display display;
	private final EventBus eventBus;
	private static EditCategoryPresenter instance = null;
	private Category category;
	private boolean isParentLoaded = false;
	
	@Inject private OikonomosErrorMessages errorMessages;
	@Inject private CategoryServiceAsync rpcCategoryService;

	public static void createAsync(final Presenter.Callback callback) {
		GWT.runAsync(new RunAsyncCallback() {
			
			@Override
			public void onSuccess() {
				WaitingFlyer.start();

				if (instance == null) {
					instance = NomosInjector.INSTANCE.getEditCategoryPresenter();
				}
				
				instance.getRemoteViewInformations();
				
				String[] splitHistoryToken = History.getToken().split(DashboardPresenter.HISTORY_PARAM_SEPARATOR);
				try {
					long categoryId = Long.parseLong(splitHistoryToken[1]);
					instance.getRemoteData(categoryId, callback);
				} catch (Exception e) {
					User authentifiedUser = OikonomosController.getAuthentifiedUser();
					instance.category = new Category();
					instance.category.setEntityOwner(authentifiedUser.getUserEmail());
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
		
		this.display.getDeleteButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				onDeleteCategory();
			}
		});

		updateViewFromData();
	}

	private final void getRemoteViewInformations() {
		if (!isParentLoaded) {
			LOG.finer("getRemoteViewInformations start");
			rpcCategoryService.getRootEntities(LocaleInfo.getCurrentLocale().getLocaleName(), new AsyncCallback<List<Category>>() {
	
				@Override public void onFailure(Throwable caught) {
					WaitingFlyer.stop();
					MessageFlyer.error(caught.getLocalizedMessage());
				}
	
				@Override public void onSuccess(List<Category> result) {
					LOG.finer("getRemoteViewInformations result");
					isParentLoaded = display.populateParentList(result);
					String[] splitHistoryToken = History.getToken().split(DashboardPresenter.HISTORY_PARAM_SEPARATOR);
					display.hideCurrentOption(
							(splitHistoryToken.length < 2)?"-1":splitHistoryToken[1]);
					LOG.finer("getRemoteViewInformations end");
				}
			});
		}
	}
	
	private final void getRemoteData(long categoryId, final Presenter.Callback callback) {
		rpcCategoryService.getEntityWithoutChild(categoryId, LocaleInfo.getCurrentLocale().getLocaleName(), new AsyncCallback<Category>() {
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
		
		if (category.getEntityId() != null) {
			display.getCategoryId().setText(category.getEntityId().toString());
		}

		if (isParentLoaded) {
			display.hideCurrentOption(
				(category.getEntityId()==null)?"-1":category.getEntityId().toString()
			);
		}

		if (category.getParentId() > 0) {
			display.getCategoryParent().setValue(category.getParentId().toString());
		} else {
			display.getCategoryParent().setValue("ROOT");
			if (!category.getChilds().isEmpty()) {
				display.enableCategoryParent(false); // Only 2 level supported
			}
		}
		
		if (category.getEntityOwner() == null || category.getEntityOwner().isEmpty()) {
			display.setCategoryPrivate(false);
		} else if (category.getEntityId() != null) {
			display.setCategoryPrivate(true);
		}

		display.getCategoryDescription().setValue(category.getEntityDescription());
		
	}
	
	private void saveDataFromView() {
		if (display.getCategoryDescription().getValue().isEmpty()) {
			MessageFlyer.error(errorMessages.error_message_mandatoryDescription());
		}
		if (!OikonomosController.getAuthentifiedUser().getUserEmail().equals(category.getEntityOwner())) {
			MessageFlyer.error(errorMessages.error_message_category_publicNotUpdatable());
		}
		
		WaitingFlyer.start();
		category.setEntityDescription(display.getCategoryDescription().getValue());
		Long parentId = -1L;
		if (!display.getCategoryParent().getValue().equals("ROOT"))
			parentId = Long.parseLong(display.getCategoryParent().getValue());
		category.setParentId(parentId);
		
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
						isParentLoaded = false;
						eventBus.fireEvent(new LeftmenuEntityChangeEvent(category));
						History.newItem(result.getEntityType().name().toLowerCase()+"|"+result.getEntityId());
						WaitingFlyer.stop();
						MessageFlyer.info(
								errorMessages.info_message_entity_saveSuccessfully().replace("{0}", category.getEntityDescription()));
					}
				});
		
	}
	
	private void onDeleteCategory() {
		WaitingFlyer.start();
		final String categoryName = category.getEntityDescription();
		rpcCategoryService.delete(category.getEntityId(), new AsyncCallback<Void>() {

			@Override public void onFailure(Throwable caught) {
				WaitingFlyer.stop();
				MessageFlyer.error(caught.getLocalizedMessage());
			}

			@Override public void onSuccess(Void result) {
				History.newItem(EntityType.CATEGORY.name().toLowerCase());
				eventBus.fireEvent(new LeftmenuEntityChangeEvent(category));
				WaitingFlyer.stop();
				MessageFlyer.info(
						errorMessages.info_message_entity_deleteSuccessfully().replace("{0}", categoryName));
			}
		});
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(display.asWidget());
	}

	@Override
	public Widget getDisplay() {
		return display.asWidget();
	}

}
