package com.marthym.oikonomos.main.client.components;

import java.io.Serializable;

import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.marthym.oikonomos.shared.model.dto.Category;

public class CategorySuggestion implements Suggestion, Serializable {
	private static final long serialVersionUID = 1L;

	private Category category;
	
	public CategorySuggestion (Category category) {
		this.category = category;
	}
	
	@Override
	public String getDisplayString() {
		return category.getAbsoluteDescription();
	}

	@Override
	public String getReplacementString() {
		return category.getAbsoluteDescription();
	}

	public Category getCategory() {
		return category;
	}

}
