package com.marthym.oikonomos.client.components;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.InlineHyperlink;
import com.marthym.oikonomos.client.resources.AnonymousNavigationBarRessource;

public class AnonymousNavigationBar extends Composite {

	public static enum ActivationType {
		CONNECT,
		REGISTER
	};
	
	private static AnonymousNavigationBarUiBinder uiBinder = GWT
			.create(AnonymousNavigationBarUiBinder.class);
	
	@UiField InlineHyperlink linkConnect;
	@UiField InlineHyperlink linkRegister;
	
	private ActivationType active;
	private final AnonymousNavigationBarRessource res = AnonymousNavigationBarRessource.INSTANCE;
	
	interface AnonymousNavigationBarUiBinder extends
		UiBinder<Widget, AnonymousNavigationBar> {
	}

	public AnonymousNavigationBar() {
		this(ActivationType.CONNECT);
	}

	public AnonymousNavigationBar(ActivationType activation) {
		res.style().ensureInjected();
		
		initWidget(uiBinder.createAndBindUi(this));
		
		this.active = activation;
		switch (active) {
		case CONNECT:
			linkConnect.addStyleName(res.style().focus());
			break;

		default:
			linkRegister.addStyleName(res.style().focus());
			break;
		}
	}
	
	public final HasClickHandlers getLoginLink() {
		return linkConnect;
	}
	
	public final HasClickHandlers getRegisterLink() {
		return linkRegister;
	}

	public void toggleMenuActivation(ActivationType activation) {
		switch (activation) {
		case REGISTER:
			linkConnect.removeStyleName(res.style().focus());
			linkRegister.addStyleName(res.style().focus());
			active = ActivationType.CONNECT;
			break;
		case CONNECT:
			linkRegister.removeStyleName(res.style().focus());
			linkConnect.addStyleName(res.style().focus());
			active = ActivationType.REGISTER;
			break;	
			
		}
	}
}
