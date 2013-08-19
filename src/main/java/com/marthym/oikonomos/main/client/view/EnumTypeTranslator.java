package com.marthym.oikonomos.main.client.view;

import com.google.gwt.core.shared.GWT;
import com.marthym.oikonomos.client.i18n.OikonomosConstants;

public abstract class EnumTypeTranslator {
	public static final OikonomosConstants translations = GWT.create(OikonomosConstants.class);
	public static <E extends Enum<E>> String getTranslation(E enumValue) {
		String[] name = enumValue.name().split("_");
		StringBuilder bld = new StringBuilder(enumValue.name().length()-(name.length-1));
		bld.append(name[0].toLowerCase());
		for (int i = 1; i < name.length; i++) {
			bld.append(name[i].substring(0, 1).toUpperCase()+name[i].substring(1).toLowerCase());
		}
		
		String translationKey = bld.toString();
		return translations.getString(translationKey);
	}
}
