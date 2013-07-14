package com.marthym.oikonomos.client.components;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class UnorderedListPanel extends ComplexPanel {
	public static class ListItemElement extends SimplePanel {
	    public ListItemElement() {
	        super((Element) Document.get().createLIElement().cast());
	    }
	 
	    public ListItemElement(String s) {
	        this();
	        getElement().setInnerText(s);
	    }
	 
	    public ListItemElement(Widget w) {
	        this();
	        this.add(w);
	    }
	}
	
	public UnorderedListPanel() {
		setElement(Document.get().createULElement());
	}

	public void setId(String id) {
		getElement().setId(id);
	}

	public void setDir(String dir) {
		// Set an attribute specific to this tag
		((UListElement) getElement().cast()).setDir(dir);
	}

	@Override
	public void add(Widget w) {
		super.add(w, getElement());
	}
}
