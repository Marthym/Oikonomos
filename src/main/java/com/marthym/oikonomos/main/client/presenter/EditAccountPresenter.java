package com.marthym.oikonomos.main.client.presenter;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.marthym.oikonomos.client.presenter.Presenter;
import com.marthym.oikonomos.shared.view.data.EditAccountData;

public class EditAccountPresenter implements Presenter {
	public interface Display {
		Widget asWidget();
		
		HasClickHandlers getValidateButton();
		HasClickHandlers getResetButton();
	}
	
	private final Display display;
	
	public EditAccountPresenter(Display display, EditAccountData datas) {
		this.display = display;
		bind(datas);
	}
	
	private void bind(EditAccountData datas) {
		
	}
	
	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(display.asWidget());
	}

}
