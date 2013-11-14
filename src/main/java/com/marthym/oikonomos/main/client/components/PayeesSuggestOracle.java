package com.marthym.oikonomos.main.client.components;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.marthym.oikonomos.client.components.MessageFlyer;
import com.marthym.oikonomos.shared.model.Payee;
import com.marthym.oikonomos.shared.services.PayeeServiceAsync;

public class PayeesSuggestOracle extends SuggestOracle {
	
	@Inject private PayeeServiceAsync payeeService;
	
	@Override
	public void requestSuggestions(final Request request, final Callback callback) {
		String payeeQuery = request.getQuery();
		if (payeeQuery.length() > 2) {
	    	  payeeService.getEntitiesByDescription(payeeQuery, new AsyncCallback<List<Payee>>() {
					
					@Override public void onFailure(Throwable caught) {
						MessageFlyer.error(caught.getLocalizedMessage());
					}
					
					@Override
					public void onSuccess(List<Payee> result) {
						Collection<Suggestion> list = new LinkedList<Suggestion>();
						for (Payee payee : result) {
							list.add(new PayeeSuggestion(payee));
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
