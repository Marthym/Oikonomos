package com.marthym.oikonomos.client.i18n;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.google.gwt.validation.client.AbstractValidationMessageResolver;
import com.google.gwt.validation.client.UserValidationMessagesResolver;

public class OikonomosValidationMessagesResolver extends
		AbstractValidationMessageResolver implements
		UserValidationMessagesResolver {
	protected OikonomosValidationMessagesResolver() {
		super((ConstantsWithLookup) GWT.create(ValidatorMessages.class));
	}
}