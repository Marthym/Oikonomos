package com.marthym.oikonomos.client;

import com.marthym.oikonomos.client.pages.WelcomePage;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootLayoutPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Oikonomos implements EntryPoint {
  public void onModuleLoad() {
	  RootLayoutPanel.get().add(new WelcomePage());
  }
}
