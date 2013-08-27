package com.marthym.oikonomos.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface MessageFlyerResource extends ClientBundle {
	public MessageFlyerResource INSTANCE = GWT.create(MessageFlyerResource.class);
	
	public interface MessageFlyerRessourceCss extends CssResource {
	      String messageFlyer();
	      String messageFlyerBody();
	      String info();
	      String error();
	   }

	   @Source("MessageFlyer.css")
	   MessageFlyerRessourceCss style();
}
