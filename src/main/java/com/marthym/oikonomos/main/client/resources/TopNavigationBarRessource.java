package com.marthym.oikonomos.main.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface TopNavigationBarRessource extends ClientBundle {
	public TopNavigationBarRessource INSTANCE = GWT.create(TopNavigationBarRessource.class);
	
	public interface TopNavigationBarCss extends CssResource {
	      String topNavigation();
	      String focus();
	      String right();
	      String button();
	      String logout();
	      String properties();
	   }

	   @Source("TopNavigationBar.css")
	   TopNavigationBarCss style();
}
