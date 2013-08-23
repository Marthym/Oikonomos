package com.marthym.oikonomos.main.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface LeftMenuResource extends ClientBundle {

	public LeftMenuResource INSTANCE = GWT.create(LeftMenuResource.class);
	
	public interface LeftMenuStackCss extends CssResource {
		@ClassName("vnav")
		String vnav();
		
		@ClassName("vnav-item")
		String vnavItem();
		
		@ClassName("vnav-counter")
		String vnavCounter();
		
		@ClassName("vnav-add-link")
		String vnavAddLink();
		
		@ClassName("vnav-subnav")
		String vnavSubnav();
		
		String active();
		String links();
	}

	@Source("LeftMenuResource.css")
	LeftMenuStackCss style();

}
