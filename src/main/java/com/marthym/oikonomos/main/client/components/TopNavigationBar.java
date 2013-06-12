package com.marthym.oikonomos.main.client.components;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.InlineHyperlink;

import com.marthym.oikonomos.main.client.resources.TopNavigationBarRessource;
import com.marthym.oikonomos.main.client.presenter.TopNavigationPresenter;

public class TopNavigationBar extends Composite implements TopNavigationPresenter.Display {
	private static TopNavigationBarUiBinder uiBinder = GWT.create(TopNavigationBarUiBinder.class);
	
	@UiField HTMLPanel userName;
	@UiField InlineHyperlink linkDashboard;
	@UiField InlineHyperlink linkUserProperties;
	@UiField InlineHyperlink linkLogout;
	
	private final TopNavigationBarRessource res = TopNavigationBarRessource.INSTANCE;
	
	interface TopNavigationBarUiBinder extends
		UiBinder<Widget, TopNavigationBar> {
	}

	public TopNavigationBar() {
		res.style().ensureInjected();
		
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public final HasClickHandlers getDashboardLink() {
		return linkDashboard;
	}
	
	public final HasClickHandlers getPropertiesLink() {
		return linkUserProperties;
	}
	
	
	public final HasClickHandlers getLogoutLink() {
		return linkLogout;
	}
	
	public final HTMLPanel getUserName() {
		return userName;
	}
	
}
