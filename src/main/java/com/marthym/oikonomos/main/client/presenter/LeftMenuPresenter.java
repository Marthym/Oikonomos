package com.marthym.oikonomos.main.client.presenter;

import java.util.List;

import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.marthym.oikonomos.client.components.MessageFlyer;
import com.marthym.oikonomos.client.presenter.Presenter;
import com.marthym.oikonomos.main.client.components.LeftMenuEntityPanel;
import com.marthym.oikonomos.main.client.services.AccountDataServiceAsync;
import com.marthym.oikonomos.shared.model.Account;
import com.marthym.oikonomos.shared.model.LeftMenuEntity;
import com.marthym.oikonomos.shared.view.data.EntityType;
import com.marthym.oikonomos.shared.view.data.HasEntityCountData;

public class LeftMenuPresenter implements Presenter {
	public interface Display {
		Widget asWidget();
		void setCount(EntityType entity, int count);
		DisclosurePanel getDisclosurePanel(EntityType entity);
		void refreshEntityList(EntityType entity, List<? extends LeftMenuEntity> entities);
	}
	
	private final HandlerManager eventBus;
	private final Display display;
	private HasEntityCountData data;

	public LeftMenuPresenter(HandlerManager eventBus, HasEntityCountData data, Display display) {
		this.eventBus = eventBus;
		this.display = display;
		this.data = data;
		bind();
	}
	
	private void bind() {
		for (EntityType entity : EntityType.values()) {
			display.setCount(entity, data.getCountFor(entity));
			
			display.getDisclosurePanel(entity).addOpenHandler(new OpenHandler<DisclosurePanel>() {
				
				@Override
				public void onOpen(OpenEvent<DisclosurePanel> event) {
					updateEntityList(event.getTarget());
				}
			});
		}
	}
	
	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(display.asWidget());
	}

	private void updateEntityList(DisclosurePanel panel) {
		String entityName = panel.getElement().getAttribute(LeftMenuEntityPanel.ENTITY_TYPE_ATTRIBUTE);
		
		switch (EntityType.valueOf(entityName)) {
		case ACCOUNT:
			AccountDataServiceAsync rpcAccountData = AccountDataServiceAsync.Util.getInstance();
			rpcAccountData.getList(new AsyncCallback<List<Account>>() {
				
				@Override
				public void onSuccess(List<Account> result) {
					display.refreshEntityList(EntityType.ACCOUNT, result);
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
}
