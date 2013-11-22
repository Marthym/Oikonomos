package com.marthym.oikonomos.server.utils;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.marthym.oikonomos.shared.services.AuthenticationService;

@SuppressWarnings("unchecked")
public class SessionHelper {
	public static final String getSessionLocale() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Map<String, String> details = (Map<String, String>) authentication.getDetails();
		return details.get(AuthenticationService.SESSION_DETAIL_LOCALE);
	}
}
