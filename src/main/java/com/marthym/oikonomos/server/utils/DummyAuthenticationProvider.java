package com.marthym.oikonomos.server.utils;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class DummyAuthenticationProvider implements AuthenticationProvider {

	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		throw new IllegalStateException("This implementation is a dummy class, created purely so that "
				+ "spring security namespace tags can be used in application context, and this method should "
				+ "never be called");
	}

	public boolean supports(Class<?> clazz) {

		throw new IllegalStateException("This implementation is a dummy class, created purely so that "
				+ "spring security namespace tags can be used in application context, and this method should "
				+ "never be called");

	}
}
