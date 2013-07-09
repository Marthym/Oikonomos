package com.marthym.oikonomos.main.client.components;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import com.marthym.oikonomos.client.i18n.OikonomosConstants;
import com.marthym.oikonomos.shared.model.LeftMenuEntity;
import com.marthym.oikonomos.shared.view.data.EntityType;

public class LeftMenuEntityPanel extends Composite {
	public static final String ENTITY_TYPE_ATTRIBUTE = "data-type";
	
	private static LeftMenuViewUiBinder uiBinder = GWT.create(LeftMenuViewUiBinder.class);
	interface LeftMenuViewUiBinder extends UiBinder<Widget, LeftMenuEntityPanel> {}
	
	@UiField SpanElement entityCount;
	@UiField SpanElement entityName;
	@UiField DisclosurePanel entityDisPanel;
	
	@UiConstructor
	public LeftMenuEntityPanel(EntityType type) {
		initWidget(uiBinder.createAndBindUi(this));
		
		this.getElement().setAttribute(ENTITY_TYPE_ATTRIBUTE, type.name());
		OikonomosConstants trad = GWT.create(OikonomosConstants.class);
		this.entityName.setInnerText(getTranslation(type));
	}

	public void setCount(int count) {
		entityCount.setInnerHTML(Integer.toString(count));
	}

	public DisclosurePanel getDisclosurePanel() {
		return entityDisPanel;
	}

	public void refreshEntityList(List<LeftMenuEntity> entities) {
		StringBuilder entitiesList = new StringBuilder();
		for (LeftMenuEntity myAccount : entities) {
			entitiesList.append(myAccount.getEntityDescription()).append("<br/>");
		}
		HTMLPanel content = new HTMLPanel(entitiesList.toString());
		entityDisPanel.setContent(content);
	}

	private String getTranslation(EntityType type) {
		OikonomosConstants traduction = GWT.create(OikonomosConstants.class);
		switch (type) {
		case ACCOUNT:
			return traduction.accounts();
			
		case BUDGETARY_LINE:
			return traduction.budgetaryLines();
			
		case CATEGORY:
			return traduction.categories();
			
		case PAYEE:
			return traduction.payees();
			
		case REPORT:
			return traduction.reports();
			
		case SCHEDULER:
			return traduction.scheduler();
		}
		
		return "";
	}
}
