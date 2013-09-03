package com.marthym.oikonomos.shared.view.data;

import com.marthym.oikonomos.shared.exceptions.OikonomosUnauthorizedException;
import com.marthym.oikonomos.shared.model.User;

public interface HasCurrentUserData {
	public User getCurrentUserData() throws OikonomosUnauthorizedException;
}
