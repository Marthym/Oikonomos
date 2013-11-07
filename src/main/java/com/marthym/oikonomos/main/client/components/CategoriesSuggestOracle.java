package com.marthym.oikonomos.main.client.components;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.marthym.oikonomos.client.components.MessageFlyer;
import com.marthym.oikonomos.shared.model.dto.Category;
import com.marthym.oikonomos.shared.services.CategoryServiceAsync;

public class CategoriesSuggestOracle extends SuggestOracle {
	
	@Inject private CategoryServiceAsync categoryService;

	@Override
	public void requestSuggestions(final Request request, final Callback callback) {
		String categoryQuery = request.getQuery();
		if (categoryQuery.length() > 2) {
			categoryService.getEntitiesByDescription(categoryQuery, LocaleInfo.getCurrentLocale().getLocaleName(), new AsyncCallback<List<Category>>() {
				
				@Override public void onFailure(Throwable caught) {
					MessageFlyer.error(caught.getLocalizedMessage());
				}
				
				@Override
				public void onSuccess(List<Category> result) {
					Collection<Suggestion> list = new LinkedList<Suggestion>();
					for (Category cat : result) {
						list.add(new CategorySuggestion(cat));
					}
					Response response = new Response(list);
                    callback.onSuggestionsReady(request, response);
				}
				
			});
			
			
		} else {
			Response response = new Response(
                    Collections.<Suggestion> emptyList());
            callback.onSuggestionsReady(request, response);
		}
		
	}


}
