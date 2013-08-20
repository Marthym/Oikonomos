package com.marthym.oikonomos.main.client.components;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.ListBox;

public class SingleValueListBox extends ListBox implements HasValue<String> {

	private boolean valueChangeHandlerInitialized;

	@Override
	public String getValue() {
		return getValue(getSelectedIndex());
	}

	@Override
	public void setValue(String value) {
		for (int i = 0; i < getItemCount(); i++) {
			if (getValue(i).equals(value)) {
				setSelectedIndex(i);
				break;
			}
		}
	}

	@Override
	public void setValue(String value, boolean arg1) {
		setValue(value);
		if (arg1)
			ValueChangeEvent.fire(this, value);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<String> handler) {

		if (!valueChangeHandlerInitialized) {
			valueChangeHandlerInitialized = true;
			addChangeHandler(new ChangeHandler() {
				public void onChange(ChangeEvent event) {
					ValueChangeEvent.fire(SingleValueListBox.this, getValue());
				}
			});
		}
		return addHandler(handler, ValueChangeEvent.getType());
	}

}