package com.marthym.oikonomos.main.client.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import com.marthym.oikonomos.main.client.components.LeftMenuEntityPanel;
import com.marthym.oikonomos.main.client.presenter.LeftMenuPresenter;
import com.marthym.oikonomos.main.client.resources.LeftMenuResource;
import com.marthym.oikonomos.shared.model.LeftMenuEntity;
import com.marthym.oikonomos.shared.view.data.EntityType;

public class LeftMenuView extends Composite implements LeftMenuPresenter.Display {	
	private final LeftMenuResource res = LeftMenuResource.INSTANCE;
	
	Map<EntityType, LeftMenuEntityPanel> entitiesPanels;
	
	public LeftMenuView() {
		res.style().ensureInjected();
		
		VerticalPanel viewPanel = new VerticalPanel();
		viewPanel.setStyleName(res.style().vnav());
		viewPanel.setWidth("100%");
		entitiesPanels = new HashMap<EntityType, LeftMenuEntityPanel>();
		for (EntityType type : EntityType.values()) {
			LeftMenuEntityPanel leftMenuEntity = new LeftMenuEntityPanel(type);
			viewPanel.add(leftMenuEntity);
			entitiesPanels.put(type, leftMenuEntity);
		}
		
		initWidget(viewPanel);
	}

	@Override
	public void setCount(EntityType entity, int count) {
		entitiesPanels.get(entity).setCount(count);
	}

	@Override
	public DisclosurePanel getDisclosurePanel(EntityType entity) {
		return entitiesPanels.get(entity).getDisclosurePanel();
	}

	@Override
	public void refreshEntityList(EntityType entity, List<? extends LeftMenuEntity> entities) {
		StringBuilder accountList = new StringBuilder();
		for (LeftMenuEntity myEntity : entities) {
			accountList.append(myEntity.getEntityDescription()).append("<br/>");
		}
		HTMLPanel content = new HTMLPanel(accountList.toString());
		entitiesPanels.get(entity).getDisclosurePanel().setContent(content);
	}

	
}
