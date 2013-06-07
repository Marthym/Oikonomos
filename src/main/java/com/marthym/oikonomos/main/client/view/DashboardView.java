package com.marthym.oikonomos.main.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import com.marthym.oikonomos.main.client.presenter.DashboardPresenter;

public class DashboardView extends Composite implements DashboardPresenter.Display {
	private static DashboardViewUiBinder uiBinder = GWT.create(DashboardViewUiBinder.class);
	interface DashboardViewUiBinder extends UiBinder<Widget, DashboardView> {}
	
	public DashboardView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
