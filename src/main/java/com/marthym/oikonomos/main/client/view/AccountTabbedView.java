package com.marthym.oikonomos.main.client.view;

import javax.inject.Inject;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.marthym.oikonomos.client.i18n.OikonomosConstants;
import com.marthym.oikonomos.main.client.presenter.AccountTabbedPresenter;
import com.marthym.oikonomos.main.client.resources.DashboardViewResource;
import com.marthym.oikonomos.main.client.resources.MainFormViewResource;

public class AccountTabbedView extends Composite implements AccountTabbedPresenter.Display {
	
	private TabLayoutPanel tabPanel;
	private OikonomosConstants trad;

	@Inject
	public AccountTabbedView(EditAccountView editAccountView, OikonomosConstants trad) {
		this.trad = trad;
		
		tabPanel = new TabLayoutPanel(3, Unit.EM);
		tabPanel.add(new LayoutPanel(), new HTML("&#x2631; "+this.trad.transactions()));
		tabPanel.add(new LayoutPanel(), new HTML("&#x270E; "+this.trad.properties()));
		
		initWidget(tabPanel);
		
		DashboardViewResource.INSTANCE.style().ensureInjected();
		MainFormViewResource.INSTANCE.style().ensureInjected();
	}

	@Override
	public HasWidgets getAccountPropertiesTab() {
		return (HasWidgets) tabPanel.getWidget(1);
	}

	@Override
	public HasWidgets getAccountTransactionsTab() {
		return (HasWidgets) tabPanel.getWidget(0);
	}

	@Override
	public void displayTransactionsListTab() {
		tabPanel.selectTab(0);
	}

	@Override
	public void displayAccountPropertiesTab() {
		tabPanel.selectTab(1);
	}

}
