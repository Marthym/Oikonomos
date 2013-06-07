package com.marthym.oikonomos.main.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootLayoutPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Oikonomos implements EntryPoint {
	public void onModuleLoad() {
	    OikonomosController appViewer = new OikonomosController();
	    appViewer.go(RootLayoutPanel.get());
	}
	
	public static final boolean isAssignableFrom(Object object, Class<?> clazz) {
		if (clazz == null) {
            return false;
        }

        if (clazz.equals(object.getClass())) {
            return true;
        }

        Class<?> currentSuperClass = clazz.getSuperclass();
        while (currentSuperClass != null) {
            if (currentSuperClass.equals(clazz)) {
                return true;
            }
            currentSuperClass = currentSuperClass.getSuperclass();
        }
        return false;
	}
}
