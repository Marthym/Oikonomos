package com.marthym.oikonomos.main.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import com.marthym.oikonomos.main.client.presenter.DashboardPresenter;

public class DashboardView extends Composite implements DashboardPresenter.Display {
	private static DashboardViewUiBinder uiBinder = GWT.create(DashboardViewUiBinder.class);
	interface DashboardViewUiBinder extends UiBinder<Widget, DashboardView> {}
	
	@UiField HTMLPanel topPanel;
	@UiField HTMLPanel centerPanel;
	
	public DashboardView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public HasWidgets getTopPanel() {
		return topPanel;
	}

	@Override
	public HasWidgets getCenterPanel() {
		return centerPanel;
	}
	
}
