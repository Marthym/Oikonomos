package com.marthym.oikonomos.main.client.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Anchor;
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
		refreshEntityList(entities, new ClickHandler() {
			@Override public void onClick(ClickEvent event) {
				String historyToken = ((Anchor)event.getSource()).getElement().getAttribute("data-id");
				History.newItem(historyToken);
			}
		});
	}

	@Override
	public void refreshEntityList(List<? extends LeftMenuEntity> entities, ClickHandler handler) {
		LeftMenuEntityPanel leftMenuEntityPanel = null;
		DisclosurePanel disclosurePanel = null;
		UnorderedListPanel content = null;
		List<ListItemElement> allListElements = null;
		EntityType currentType = null;
		
		for (final LeftMenuEntity entity : entities) {
			if (currentType == null || currentType != entity.getEntityType()) {
				if (currentType != null && allListElements != null) 
					setCount(currentType, allListElements.size());
				
				leftMenuEntityPanel = entitiesPanels.get(entity.getEntityType());
				disclosurePanel = leftMenuEntityPanel.getDisclosurePanel();
				content = (UnorderedListPanel)disclosurePanel.getContent();
				
				if (content == null) {
					content = new UnorderedListPanel();
					content.setStyleName(res.style().vnavSubnav());
					disclosurePanel.setContent(content);
				}
				
				currentType = entity.getEntityType();
			}
			
			String id = entity.getEntityType().name().toLowerCase()+"|"+entity.getEntityId();
			
			Anchor link = new Anchor(entity.getEntityDescription());
			link.getElement().setAttribute("data-id", id);
			ListItemElement entityLi = new ListItemElement(link);
			content.add(entityLi);
			
			link.addClickHandler(handler);
		}
		
		if (currentType != null && content != null && content.getListElements() != null) 
			setCount(currentType, content.getListElements().size());

	}
	
	@Override
	public void refreshEntitySublist(Anchor parentLink, List<? extends LeftMenuEntity> entities) {
		for (final LeftMenuEntity entity : entities) {
			String id = entity.getEntityType().name().toLowerCase()+"|"+entity.getEntityId();
			
			UnorderedListPanel content = null;
			
			ListItemElement liParent = (ListItemElement) parentLink.getParent();
			int widgetCount = liParent.getWidgetCount();
			if (widgetCount < 2) {
				content = new UnorderedListPanel();
				content.setStyleName(res.style().vnavSubnav());
				liParent.add(content);
			} else {
				content = (UnorderedListPanel) liParent.getWidget(1);
			}
			
			ListItemElement entityLi = null;
			List<ListItemElement> allListElements = content.getListElements();
			for (ListItemElement li : allListElements) {
				Hyperlink hyperlink = (Hyperlink)li.getWidget(0);
				String entityId = hyperlink.getTargetHistoryToken();
				if (entityId.equals(id)) {
					entityLi = li;
					break;
				}
			}
			
			Hyperlink link = new Hyperlink(entity.getEntityDescription(), id);
			if (entityLi == null) {
				entityLi = new ListItemElement(link);
				content.add(entityLi);
			} else {
				entityLi.clear();
				entityLi.add(link);
			}						
		}
	}
}
