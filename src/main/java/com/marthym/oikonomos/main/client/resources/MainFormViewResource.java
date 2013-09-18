package com.marthym.oikonomos.main.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface MainFormViewResource extends ClientBundle {

	public MainFormViewResource INSTANCE = GWT.create(MainFormViewResource.class);
	
	public interface EditAccountResourceCss extends CssResource {
		String fieldTable();
		String headerCell();
		String actions();
		String textBox();
		String listBox();
		String accountNumber();
		String key();
		String checkBox();
		String categoryType();
		String deleteButton();
		@ClassName("public") String ctPublic();
		@ClassName("private") String ctPrivate();
		@ClassName("gradien-right") String gradienRight();
		@ClassName("title-image") String titleImage();
	}

	@Source("MainFormViewResource.css")
	EditAccountResourceCss style();

}
