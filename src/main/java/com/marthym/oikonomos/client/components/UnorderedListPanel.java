package com.marthym.oikonomos.client.components;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

public class UnorderedListPanel extends ComplexPanel {
	private static Logger LOG = Logger.getLogger(UnorderedListPanel.class.getName());
	
	public static class ListItemElement extends ComplexPanel {
	    public ListItemElement() {
	    	setElement((Element) Document.get().createLIElement().cast());
	    }
	 
	    public ListItemElement(String s) {
	        this();
	        getElement().setInnerText(s);
	    }
	 
	    public ListItemElement(Widget w) {
	    	this();
	        super.add(w, getElement());
	    }
	    
	    @Override
	    public void add(Widget w) {
	    	super.add(w, getElement());
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
	
	public List<ListItemElement> getListElements() {
		List<ListItemElement> lis = new LinkedList<UnorderedListPanel.ListItemElement>();
		for (Widget child : super.getChildren()) {
			lis.add((ListItemElement)child);
		}
		
		return lis;
	}

	@Override
	public void add(Widget w) {
		if (!w.getClass().getName().equals(ListItemElement.class.getName())) {
			LOG.severe("Only ListItemElement are authorized in UnorderedListPanel");
			return;
		}
		super.add(w, getElement());
	}
}
