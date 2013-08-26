package com.marthym.oikonomos.main.client.presenter;

import javax.inject.Inject;

import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
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
	
	private final EventBus eventBus;
	private final Display display;
	private HasCurrentUserData data;

	@Inject
	public TopNavigationPresenter(EventBus eventBus, Display display) {
		this.eventBus = eventBus;
		this.display = display;
		bind();
	}
	
	private void bind() {
		this.display.getLogoutLink().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new LogoutEvent());
			}
		});
	}
	
	public void updateViewData(HasCurrentUserData data) {
		this.data = data;
		try {
			display.getUserName().setInnerText(this.data.getCurrentUserData().getUserFirstname()+" "+this.data.getCurrentUserData().getUserLastname());
		} catch (OikonomosUnathorizedException e) {}
	}
	
	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(display.asWidget());
	}

}
