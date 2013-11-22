package com.marthym.oikonomos.client;

import javax.validation.Validator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.validation.client.AbstractGwtValidatorFactory;
import com.google.gwt.validation.client.GwtValidation;
import com.google.gwt.validation.client.impl.AbstractGwtValidator;
import com.marthym.oikonomos.shared.model.Account;
import com.marthym.oikonomos.shared.model.Payee;
import com.marthym.oikonomos.shared.model.User;
import com.marthym.oikonomos.shared.model.dto.CategoryDTO;
import com.marthym.oikonomos.shared.model.dto.TransactionDTO;

public final class SimpleValidatorFactory extends AbstractGwtValidatorFactory {

	@GwtValidation(value={User.class, Account.class, Payee.class, CategoryDTO.class, TransactionDTO.class})
	public interface GwtValidator extends Validator {
	}

	@Override
	public AbstractGwtValidator createValidator() {
		return GWT.create(GwtValidator.class);
	}
}