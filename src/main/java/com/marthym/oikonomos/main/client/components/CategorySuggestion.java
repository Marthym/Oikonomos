package com.marthym.oikonomos.main.client.components;

import java.io.Serializable;

import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.marthym.oikonomos.shared.model.dto.CategoryDTO;

public class CategorySuggestion implements Suggestion, Serializable {
	private static final long serialVersionUID = 1L;

	private CategoryDTO category;
	
	public CategorySuggestion (CategoryDTO category) {
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

	public final CategoryDTO getCategory() {
		return category;
	}

}
