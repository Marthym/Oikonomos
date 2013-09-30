package com.marthym.oikonomos.main.client.view;

import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.marthym.oikonomos.client.i18n.OikonomosConstants;
import com.marthym.oikonomos.main.client.presenter.EditPayeePresenter;
import com.marthym.oikonomos.main.client.resources.MainFormViewResource;

public class EditPayeeView extends Composite implements EditPayeePresenter.Display {
	private static EditPayeeViewUiBinder uiBinder = GWT.create(EditPayeeViewUiBinder.class);

	interface EditPayeeViewUiBinder extends UiBinder<Widget, EditPayeeView> {}
	
	@UiField FormPanel formPayee;
	@UiField Label payeeId;
	@UiField TextBox payeeName;
	@UiField Button resetButton;
	@UiField Button submitButton;
	@UiField Button deleteButton;
	
	@Inject OikonomosConstants constants;
	@Inject MainFormViewResource res;

	public EditPayeeView() {
		MainFormViewResource.INSTANCE.style().ensureInjected();
		
		initWidget(uiBinder.createAndBindUi(this));		
	}
	
	@Override
	public void reset() {
		formPayee.reset();
		payeeId.setText("");
		deleteButton.setVisible(false);
	}

	@Override
	public HasClickHandlers getValidateButton() {
		return submitButton;
	}

	@Override
	public HasClickHandlers getResetButton() {
		return resetButton;
	}
	
	@Override
	public HasClickHandlers getDeleteButton() {
		return deleteButton;
	}

	@Override
	public HasText getPayeeId() {
		return payeeId;
	}

	@Override
	public HasValue<String> getPayeeName() {
		return payeeName;
	}

	@Override
	public void setDeleteVisible(boolean isVisible) {
		deleteButton.setVisible(isVisible);
	}
}
