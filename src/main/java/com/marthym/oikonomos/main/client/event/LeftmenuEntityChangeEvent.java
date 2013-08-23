package com.marthym.oikonomos.main.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.marthym.oikonomos.shared.model.LeftMenuEntity;

public class LeftmenuEntityChangeEvent extends GwtEvent<LeftmenuEntityChangeEventHandler> {
	public static Type<LeftmenuEntityChangeEventHandler> TYPE = new Type<LeftmenuEntityChangeEventHandler>();

	private final LeftMenuEntity entity;
	
	public LeftmenuEntityChangeEvent(LeftMenuEntity account) {
		this.entity = account;
	}
	
	@Override
	public Type<LeftmenuEntityChangeEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(LeftmenuEntityChangeEventHandler handler) {
		handler.onLeftmenuEntityChange(this);
	}

	public LeftMenuEntity getEntity() {
		return entity;
	}
}
