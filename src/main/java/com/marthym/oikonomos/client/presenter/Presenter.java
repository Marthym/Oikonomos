package com.marthym.oikonomos.client.presenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public interface Presenter {
	public interface Callback {
		void onCreate(Presenter presenter);
		void onCreateFailed();
	}
	
	public void go(final HasWidgets container);
	public Widget getDisplay();
}
