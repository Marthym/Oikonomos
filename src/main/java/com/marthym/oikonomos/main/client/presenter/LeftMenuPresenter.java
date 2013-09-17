package com.marthym.oikonomos.main.client.presenter;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.marthym.oikonomos.client.components.MessageFlyer;
import com.marthym.oikonomos.client.components.UnorderedListPanel;
import com.marthym.oikonomos.client.components.UnorderedListPanel.ListItemElement;
import com.marthym.oikonomos.client.presenter.Presenter;
import com.marthym.oikonomos.main.client.components.LeftMenuEntityPanel;
import com.marthym.oikonomos.main.client.event.LeftmenuEntityChangeEvent;
import com.marthym.oikonomos.main.client.event.LeftmenuEntityChangeEventHandler;
import com.marthym.oikonomos.main.client.services.AccountServiceAsync;
import com.marthym.oikonomos.main.client.services.CategoryServiceAsync;
import com.marthym.oikonomos.shared.model.Account;
import com.marthym.oikonomos.shared.model.LeftMenuEntity;
import com.marthym.oikonomos.shared.model.dto.Category;
import com.marthym.oikonomos.shared.view.data.EntityType;
import com.marthym.oikonomos.shared.view.data.HasEntityCountData;

public class LeftMenuPresenter implements Presenter {
	private static final Logger LOG = Logger.getLogger(LeftMenuPresenter.class.getName());
	
	public interface Display {
		Widget asWidget();
		void setCount(EntityType entity, int count);
		DisclosurePanel getDisclosurePanel(EntityType entity);
		void activePanel(EntityType entity);
		void disactivePanel(EntityType entity);
		void refreshEntityList(List<? extends LeftMenuEntity> entities);
		void refreshEntityList(List<? extends LeftMenuEntity> entities, ClickHandler handler);
		void refreshEntitySublist(Anchor link, List<? extends LeftMenuEntity> entities);
	}
	
	private final EventBus eventBus;
	private final Display display;
	private HasEntityCountData data;

	@Inject
	public LeftMenuPresenter(EventBus eventBus, Display display) {
		this.eventBus = eventBus;
		this.display = display;
		bind();
	}
	
	private void bind() {
		eventBus.addHandler(LeftmenuEntityChangeEvent.TYPE, new LeftmenuEntityChangeEventHandler() {
			@Override
			public void onLeftmenuEntityChange(LeftmenuEntityChangeEvent event) {
				onEntityChange(event);
			}
		});
	}
	
	public void updateViewData(HasEntityCountData data) {
		this.data = data;
		for (EntityType entity : EntityType.values()) {
			display.setCount(entity, this.data.getCountFor(entity));
			
			display.getDisclosurePanel(entity).addOpenHandler(new OpenHandler<DisclosurePanel>() {
				
				@Override
				public void onOpen(OpenEvent<DisclosurePanel> event) {
					updateEntityList(event.getTarget());
				}
			});
			
			display.getDisclosurePanel(entity).addCloseHandler(new CloseHandler<DisclosurePanel>() {
				
				@Override
				public void onClose(CloseEvent<DisclosurePanel> event) {
					String entityName = event.getTarget().getElement().getAttribute(LeftMenuEntityPanel.ENTITY_TYPE_ATTRIBUTE);
					display.disactivePanel(EntityType.valueOf(entityName));
				}
			});
		}		
	}
	
	public void openEntityMenu(EntityType entity) {
		display.getDisclosurePanel(entity).setOpen(true);
	}
	
	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(display.asWidget());
	}

	//TODO: Optimize service for unify call EntityService
	private void updateEntityList(DisclosurePanel panel) {
		String entityName = panel.getElement().getAttribute(LeftMenuEntityPanel.ENTITY_TYPE_ATTRIBUTE);
		display.activePanel(EntityType.valueOf(entityName));
		
		if (panel.getContent() != null) return;
		
		switch (EntityType.valueOf(entityName)) {
		case ACCOUNT:
			AccountServiceAsync rpcAccount = AccountServiceAsync.Util.getInstance();
			rpcAccount.getList(true, new AsyncCallback<List<Account>>() {
				
				@Override
				public void onSuccess(List<Account> result) {
					display.refreshEntityList(result);
				}
				
				@Override
				public void onFailure(Throwable caught) {
					MessageFlyer.error(caught.getLocalizedMessage());
				}
			});
			break;
		case CATEGORY:
			CategoryServiceAsync rpcCategory = CategoryServiceAsync.Util.getInstance();
			final ClickHandler expandCollapseSubentitiesHandler = new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					onExpandCollapseSubentities((Anchor)event.getSource());
				}
			};
			rpcCategory.getRootEntities(LocaleInfo.getCurrentLocale().getLocaleName(), new AsyncCallback<List<Category>>() {

				@Override
				public void onFailure(Throwable caught) {
					MessageFlyer.error(caught.getLocalizedMessage());
				}

				@Override
				public void onSuccess(List<Category> result) {
					display.refreshEntityList(result, expandCollapseSubentitiesHandler);
				}
			});
			
			break;
		case BUDGETARY_LINE:
			break;
		case REPORT:
			break;
		case PAYEE :
			break;
		case SCHEDULER:
			break;
		}
		
	}
	
	private void onExpandCollapseSubentities(final Anchor link) {
		String historyToken = link.getElement().getAttribute("data-id");
		LOG.finer("onExpandCollapseSubentities: "+historyToken);
		
		ListItemElement liParent = (ListItemElement) link.getParent();
		int widgetCount = liParent.getWidgetCount();
		if (widgetCount >= 2) {
			UnorderedListPanel content = (UnorderedListPanel) liParent.getWidget(1);
			content.setVisible(!content.isVisible());
		} else {
		
			String entityId = historyToken.split("\\|")[1];
			CategoryServiceAsync rpcCategory = CategoryServiceAsync.Util.getInstance();
			rpcCategory.getEntitiesByParent(Long.parseLong(entityId), LocaleInfo.getCurrentLocale().getLocaleName(), new AsyncCallback<List<Category>>() {
				@Override public void onFailure(Throwable caught) {
					LOG.severe(caught.getLocalizedMessage());
				}
	
				@Override public void onSuccess(List<Category> result) {
					display.refreshEntitySublist(link, result);
				}
			});
			
		}
		History.newItem(historyToken);
	}
	
	private void onEntityChange(LeftmenuEntityChangeEvent event) {
		try {
			DisclosurePanel disclosurePanel = display.getDisclosurePanel(event.getEntity().getEntityType());
			disclosurePanel.clear();
			updateEntityList(disclosurePanel);
		} catch (Exception e) {
			LOG.warning(e.getClass()+": "+e.getLocalizedMessage());
		}
	}
}
