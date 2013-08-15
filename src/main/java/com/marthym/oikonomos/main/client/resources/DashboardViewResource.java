package com.marthym.oikonomos.main.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface DashboardViewResource extends ClientBundle {
	public DashboardViewResource INSTANCE = GWT.create(DashboardViewResource.class);
	
	public interface DashboardViewCss extends CssResource {
	      String content();
	   }

	   @Source("DashboardView.css")
	   DashboardViewCss style();
}
