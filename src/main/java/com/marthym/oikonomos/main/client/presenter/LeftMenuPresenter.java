package com.marthym.oikonomos.main.client.presenter;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;

import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.marthym.oikonomos.client.components.MessageFlyer;
import com.marthym.oikonomos.client.presenter.Presenter;
import com.marthym.oikonomos.main.client.components.LeftMenuEntityPanel;
import com.marthym.oikonomos.main.client.event.LeftmenuEntityChangeEvent;
import com.marthym.oikonomos.main.client.event.LeftmenuEntityChangeEventHandler;
import com.marthym.oikonomos.main.client.services.AccountServiceAsync;
import com.marthym.oikonomos.shared.model.Account;
import com.marthym.oikonomos.shared.model.LeftMenuEntity;
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
	
	private void onEntityChange(LeftmenuEntityChangeEvent event) {
		try {
			List<LeftMenuEntity> entities = new LinkedList<LeftMenuEntity>();
			entities.add(event.getEntity());
			display.refreshEntityList(entities);
		} catch (Exception e) {
			LOG.warning(e.getClass()+": "+e.getLocalizedMessage());
		}
	}
}
