package com.marthym.oikonomos.main.client.view;

import java.util.List;

import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
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
import com.marthym.oikonomos.main.client.components.SingleValueListBox;
import com.marthym.oikonomos.main.client.presenter.EditCategoryPresenter;
import com.marthym.oikonomos.main.client.resources.MainFormViewResource;
import com.marthym.oikonomos.shared.model.dto.Category;

public class EditCategoryView extends Composite implements EditCategoryPresenter.Display {
	private static EditCategoryViewUiBinder uiBinder = GWT.create(EditCategoryViewUiBinder.class);

	interface EditCategoryViewUiBinder extends UiBinder<Widget, EditCategoryView> {}
	
	@UiField FormPanel formUser;
	@UiField Label categoryId;
	@UiField Label categoryType;
	@UiField TextBox categoryDescription;
	@UiField SingleValueListBox categoryParent;
	@UiField Button resetButton;
	@UiField Button submitButton;
	@UiField Button deleteButton;
	
	@Inject OikonomosConstants constants;
	@Inject MainFormViewResource res;

	public EditCategoryView() {
		MainFormViewResource.INSTANCE.style().ensureInjected();
		
		initWidget(uiBinder.createAndBindUi(this));		
	}
	
	@Override
	public void reset() {
		formUser.reset();
		categoryId.setText("");
		categoryParent.setValue("ROOT");
		setCategoryPrivate(true);
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
	public HasText getCategoryId() {
		return categoryId;
	}

	@Override
	public HasValue<String> getCategoryDescription() {
		return categoryDescription;
	}

	@Override
	public void setCategoryPrivate(boolean isPrivate) {
		if (isPrivate) {
			categoryType.setText(constants.view_editcategory_type_private());
			categoryType.removeStyleName(res.style().ctPublic());
			categoryType.addStyleName(res.style().ctPrivate());
			categoryDescription.setEnabled(true);
			categoryParent.setEnabled(true);
			deleteButton.setVisible(true);
		} else {
			categoryType.setText(constants.view_editcategory_type_public());
			categoryType.removeStyleName(res.style().ctPrivate());
			categoryType.addStyleName(res.style().ctPublic());
			categoryDescription.setEnabled(false);
			categoryParent.setEnabled(false);
			deleteButton.setVisible(false);
		}
	}

	@Override
	public HasValue<String> getCategoryParent() {
		return categoryParent;
	}

	@Override
	public boolean populateParentList(List<Category> parents) {
		categoryParent.clear();
		categoryParent.addItem("ROOT");
		for (Category parent : parents) {
			categoryParent.addItem(parent.getEntityDescription(), parent.getEntityId().toString());
		}
		return true;
	}

	@Override
	public void hideCurrentOption(String valueHide) {
        Element element = categoryParent.getElement().getFirstChildElement();
        while (element != null){
        	if (element.getAttribute("value").equals(valueHide))
        		element.setAttribute("style", "display:none;");
        	else
        		element.removeAttribute("style");
        	
            element = element.getNextSiblingElement();
        }
	}
	
	@Override
	public void enableCategoryParent(boolean isEnable) {
		categoryParent.setEnabled(isEnable);
	}
}
