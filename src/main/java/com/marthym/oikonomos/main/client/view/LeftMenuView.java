package com.marthym.oikonomos.main.client.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.VerticalPanel;

import com.marthym.oikonomos.client.components.UnorderedListPanel;
import com.marthym.oikonomos.client.components.UnorderedListPanel.ListItemElement;
import com.marthym.oikonomos.client.i18n.OikonomosConstants;
import com.marthym.oikonomos.main.client.components.LeftMenuEntityPanel;
import com.marthym.oikonomos.main.client.presenter.LeftMenuPresenter;
import com.marthym.oikonomos.main.client.resources.LeftMenuResource;
import com.marthym.oikonomos.shared.model.LeftMenuEntity;
import com.marthym.oikonomos.shared.view.data.EntityType;

public class LeftMenuView extends Composite implements LeftMenuPresenter.Display {	
	private final LeftMenuResource res = LeftMenuResource.INSTANCE;
	private final OikonomosConstants translations = GWT.create(OikonomosConstants.class);
	
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
		UnorderedListPanel content = new UnorderedListPanel();
		content.setStyleName(res.style().vnavSubnav());
		for (LeftMenuEntity myEntity : entities) {
			content.add(new ListItemElement(myEntity.getEntityDescription()));
		}
		Hyperlink link = new Hyperlink(translations.add()+" ...", "account");
		content.add(new ListItemElement(link));
		entitiesPanels.get(entity).getDisclosurePanel().setContent(content);
	}

	@Override
	public void activePanel(EntityType entity) {
		entitiesPanels.get(entity).addStyleName(res.style().active());
	}

	@Override
	public void disactivePanel(EntityType entity) {
		entitiesPanels.get(entity).removeStyleName(res.style().active());
	}

	
}
