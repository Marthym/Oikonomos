package com.marthym.oikonomos.main.client.components;

import java.io.Serializable;

import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.marthym.oikonomos.shared.model.Payee;

public class PayeeSuggestion implements Suggestion, Serializable {
	private static final long serialVersionUID = 1L;

	private Payee payee;
	
	public PayeeSuggestion (Payee category) {
		this.payee = category;
	}
	
	@Override
	public String getDisplayString() {
		return payee.getEntityDescription();
	}

	@Override
	public String getReplacementString() {
		return payee.getEntityDescription();
	}

	public final Payee getPayee() {
		return payee;
	}

}
