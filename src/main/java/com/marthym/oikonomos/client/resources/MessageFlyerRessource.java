package com.marthym.oikonomos.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface MessageFlyerRessource extends ClientBundle {
	public MessageFlyerRessource INSTANCE = GWT.create(MessageFlyerRessource.class);
	
	public interface MessageFlyerRessourceCss extends CssResource {
	      String messageFlyer();
	      String messageFlyerBody();
	   }

	   @Source("MessageFlyer.css")
	   MessageFlyerRessourceCss style();
}
