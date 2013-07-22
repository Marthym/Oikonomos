package com.marthym.oikonomos.main.client.presenter;

import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.marthym.oikonomos.client.presenter.Presenter;
import com.marthym.oikonomos.main.client.event.LogoutEvent;
import com.marthym.oikonomos.shared.exceptions.OikonomosUnathorizedException;
import com.marthym.oikonomos.shared.view.data.HasCurrentUserData;

public class TopNavigationPresenter implements Presenter {
	public interface Display {
		HasClickHandlers getDashboardLink();
		HasClickHandlers getPropertiesLink();
		HasClickHandlers getLogoutLink();
		SpanElement getUserName();
		Widget asWidget();
	}
	
	private final HandlerManager eventBus;
	private final Display display;
	private HasCurrentUserData data;

	public TopNavigationPresenter(HandlerManager eventBus, HasCurrentUserData data, Display display) {
		this.eventBus = eventBus;
		this.display = display;
		this.data = data;
		bind();
	}
	
	private void bind() {
		try {
			display.getUserName().setInnerText(data.getCurrentUserData().getUserFirstname()+" "+data.getCurrentUserData().getUserLastname());
		} catch (OikonomosUnathorizedException e) {}
		
		this.display.getLogoutLink().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new LogoutEvent());
			}
		});
	}
	
	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(display.asWidget());
	}

}
