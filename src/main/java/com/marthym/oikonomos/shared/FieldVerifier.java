package com.marthym.oikonomos.shared;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.i18n.shared.DateTimeFormat.PredefinedFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.validation.client.impl.Validation;

public class FieldVerifier {
	public static final String EMAIL_REGEXP = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.(?:[a-zA-Z]{2,6})$";

	public static boolean isValidName(String name) {
		if (name == null) {
			return false;
		}
		return name.length() > 3;
	}
	
	public static boolean isValidMail(String mail) {
		if(mail == null) return false;
        
        String emailPattern = "EMAIL_REGEXP";
        return mail.matches(emailPattern);
	}
	
	public static boolean isValidPassword(String password) {
		if (password == null) return false;
		
		return password.length() > 7;
	}
	
	public static boolean isValidDate(String date, PredefinedFormat format) {
		DateTimeFormat formatter = DateTimeFormat.getFormat(format);
		try {
			formatter.parse(date);
			return true;
		} catch (NumberFormatException e) {}
		return false;
	}
	
	public static boolean isValidNumeric(String numeric) {
		NumberFormat formatter = NumberFormat.getDecimalFormat();
		try {
			formatter.parse(numeric);
			return true;
		} catch (IllegalArgumentException e) {}
		return false;
	}
	
	public static final <T>  List<String> validate (T entity) {
		List<String> errors = new LinkedList<String>();
		
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<T>> violations = validator.validate(entity);
		for (ConstraintViolation<T> violation : violations) {
			errors.add(violation.getMessage());
		}

		return errors;
	}
}
