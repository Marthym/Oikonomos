package com.marthym.oikonomos.main.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import com.marthym.oikonomos.main.client.presenter.LeftMenuPresenter;
import com.marthym.oikonomos.main.client.resources.LeftMenuResource;
import com.marthym.oikonomos.shared.view.data.EntityType;

public class LeftMenuView extends Composite implements LeftMenuPresenter.Display {
	private static LeftMenuViewUiBinder uiBinder = GWT.create(LeftMenuViewUiBinder.class);
	interface LeftMenuViewUiBinder extends UiBinder<Widget, LeftMenuView> {}
	
	private final LeftMenuResource res = LeftMenuResource.INSTANCE;
	
	@UiField SpanElement accountsCount;
	@UiField SpanElement reportsCount;
	@UiField SpanElement categoriesCount;
	@UiField SpanElement schedulerCount;
	@UiField SpanElement payeesCount;
	@UiField SpanElement budgetaryLinesCount;
	
	public LeftMenuView() {
		res.style().ensureInjected();
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setCount(EntityType entity, int count) {
		switch (entity) {
		case ACCOUNT:
			accountsCount.setInnerHTML(Integer.toString(count));
			break;
		case CATEGORY:
			categoriesCount.setInnerHTML(Integer.toString(count));
			break;
		case BUDGETARY_LINE:
			budgetaryLinesCount.setInnerHTML(Integer.toString(count));
			break;
		case REPORT:
			reportsCount.setInnerHTML(Integer.toString(count));
			break;
		case PAYEE :
			payeesCount.setInnerHTML(Integer.toString(count));
			break;
		case SCHEDULER:
			schedulerCount.setInnerHTML(Integer.toString(count));
			break;
		}
		
	}

	
}
