package com.marthym.oikonomos.main.client.presenter;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.marthym.oikonomos.client.presenter.Presenter;
import com.marthym.oikonomos.shared.view.data.EntityType;
import com.marthym.oikonomos.shared.view.data.HasEntityCountData;

public class LeftMenuPresenter implements Presenter {
	public interface Display {
		Widget asWidget();
		void setCount(EntityType entity, int count);
	}
	
	private final HandlerManager eventBus;
	private final Display display;
	private HasEntityCountData data;

	public LeftMenuPresenter(HandlerManager eventBus, HasEntityCountData data, Display display) {
		this.eventBus = eventBus;
		this.display = display;
		this.data = data;
		bind();
	}
	
	private void bind() {
		for (EntityType entity :EntityType.values()) {
			display.setCount(entity, data.getCountFor(entity));
		}
	}
	
	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(display.asWidget());
	}

}
