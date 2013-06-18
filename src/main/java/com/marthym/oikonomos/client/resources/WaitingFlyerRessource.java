package com.marthym.oikonomos.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

public interface WaitingFlyerRessource extends ClientBundle {
	public WaitingFlyerRessource INSTANCE = GWT.create(WaitingFlyerRessource.class);
	
	public interface WaitingFlyerRessourceCss extends CssResource {
	      String waitingFlyer();
	   }

	   @Source("WaitingFlyer.css")
	   WaitingFlyerRessourceCss style();
	   
	   @Source("loading.gif")
	   ImageResource loading();
}
