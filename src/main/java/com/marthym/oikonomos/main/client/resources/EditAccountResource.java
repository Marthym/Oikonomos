package com.marthym.oikonomos.main.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface EditAccountResource extends ClientBundle {

	public EditAccountResource INSTANCE = GWT.create(EditAccountResource.class);
	
	public interface EditAccountResourceCss extends CssResource {
		String headerCell();
		String button();
	}

	@Source("EditAccountResource.css")
	EditAccountResourceCss style();

}
