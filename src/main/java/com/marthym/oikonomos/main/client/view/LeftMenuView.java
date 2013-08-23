package com.marthym.oikonomos.main.client.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.VerticalPanel;

import com.marthym.oikonomos.client.components.UnorderedListPanel;
import com.marthym.oikonomos.client.components.UnorderedListPanel.ListItemElement;
import com.marthym.oikonomos.main.client.components.LeftMenuEntityPanel;
import com.marthym.oikonomos.main.client.presenter.LeftMenuPresenter;
import com.marthym.oikonomos.main.client.resources.LeftMenuResource;
import com.marthym.oikonomos.shared.model.LeftMenuEntity;
import com.marthym.oikonomos.shared.view.data.EntityType;

public class LeftMenuView extends Composite implements LeftMenuPresenter.Display {
	private static final Logger LOG = Logger.getLogger(LeftMenuView.class.getName());
	
	private static final LeftMenuResource res = LeftMenuResource.INSTANCE;
	
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
	public void activePanel(EntityType entity) {
		entitiesPanels.get(entity).addStyleName(res.style().active());
	}

	@Override
	public void disactivePanel(EntityType entity) {
		entitiesPanels.get(entity).removeStyleName(res.style().active());
	}

	@Override
	public void refreshEntityList(List<? extends LeftMenuEntity> entities) {
		for (LeftMenuEntity entity : entities) {
			LeftMenuEntityPanel leftMenuEntityPanel = entitiesPanels.get(entity.getEntityType());
			DisclosurePanel disclosurePanel = leftMenuEntityPanel.getDisclosurePanel();
			String id = entity.getEntityType().name().toLowerCase()+"|"+entity.getEntityId();
			LOG.finer("Entity id: "+id);
			
			UnorderedListPanel content = (UnorderedListPanel)disclosurePanel.getContent();
			if (content == null) {
				content = new UnorderedListPanel();
				content.setStyleName(res.style().vnavSubnav());
				disclosurePanel.setContent(content);
			}
			
			ListItemElement entityLi = null;
			List<ListItemElement> allListElements = content.getListElements();
			int count = allListElements.size();
			for (ListItemElement li : allListElements) {
				Hyperlink hyperlink = (Hyperlink)li.getWidget();
				String targetHistoryToken = hyperlink.getTargetHistoryToken();
				if (targetHistoryToken.equals(id)) {
					entityLi = li;
					break;
				}
			}
			
			Hyperlink link = new Hyperlink(entity.getEntityDescription(), id);
			if (entityLi == null) {
				entityLi = new ListItemElement(link);
				content.add(entityLi);
				++count;
			} else {
				entityLi.clear();
				entityLi.add(link);
			}
			
			leftMenuEntityPanel.setCount(count);
		}
	}

	
}
