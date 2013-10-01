package com.marthym.oikonomos.main.client.view;

import java.util.logging.Logger;

import javax.inject.Inject;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.marthym.oikonomos.main.client.presenter.AccountTabbedPresenter;

public class AccountTabbedView extends Composite implements AccountTabbedPresenter.Display {
	private static final Logger LOG = Logger.getLogger(AccountTabbedView.class.getName());
	
	TabLayoutPanel tabPanel;

	@Inject
	public AccountTabbedView(EditAccountView editAccountView) {
		tabPanel = new TabLayoutPanel(3, Unit.EM);
		tabPanel.add(new HTML("Test transactions"), new HTML("&#x2631; Transaction"));
		tabPanel.add(new LayoutPanel(), new HTML("&#x270E; Properties"));
		
		initWidget(tabPanel);
		
		LOG.finer("AccountTabbedView: "+(editAccountView!=null));
	}

	@Override
	public HasWidgets getAccountPropertiesTab() {
		return (HasWidgets) tabPanel.getWidget(1);
	}

	@Override
	public HasWidgets getAccountTransactionsTab() {
		return (HasWidgets) tabPanel.getWidget(0);
	}

}
