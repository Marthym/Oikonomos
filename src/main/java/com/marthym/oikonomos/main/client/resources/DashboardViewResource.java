package com.marthym.oikonomos.main.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;

public interface DashboardViewResource extends ClientBundle {
	public DashboardViewResource INSTANCE = GWT.create(DashboardViewResource.class);

	@Source("img/logout.png")
	@ImageOptions(height=32, width=32)
	ImageResource logout();
	
	@Source("img/profile.png")
	ImageResource profile();

	@Source("img/profile.png")
	@ImageOptions(height=32, width=32)
	ImageResource profile32();

	public interface DashboardViewCss extends CssResource {
	      String content();
	      String logout();
	      String clear();
	      @ClassName("profile-main-view") String profileMainView();
	      @ClassName("profile-nav-top")String profileNavBar();
	   }

	@Source("DashboardView.css")
	DashboardViewCss style();
}
