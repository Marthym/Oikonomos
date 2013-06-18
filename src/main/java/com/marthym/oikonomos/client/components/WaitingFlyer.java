package com.marthym.oikonomos.client.components;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.marthym.oikonomos.client.i18n.OikonomosConstants;
import com.marthym.oikonomos.client.resources.WaitingFlyerRessource;

public class WaitingFlyer extends PopupPanel {
	private static WaitingFlyer instance;
	private static final int TIME_OUT_WAITING = 2 * 60 * 1000;

		
	Timer closeTimer = new Timer() {
		@Override
		public void run() {
			instance.hide();
			cancel();
		}
	};

	private final static WaitingFlyerRessource res = WaitingFlyerRessource.INSTANCE;
	private final static OikonomosConstants translations = GWT.create(OikonomosConstants.class);

	private WaitingFlyer() {
		super(true);
		res.style().ensureInjected();
		
		setGlassEnabled(true);
		setAutoHideEnabled(false);
		center();
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel.add(new Image(res.loading()));
		verticalPanel.add(new Label(translations.waitplease()));
		setWidget(verticalPanel);
	}
	
	public static final WaitingFlyer start() {
		if (instance == null) {
			instance = new WaitingFlyer();
		}
		instance.closeTimer.schedule(TIME_OUT_WAITING);
		instance.show();
		return null;
	}
	
	public static final void stop() {
		if (instance != null) instance.hide();
	}
}
