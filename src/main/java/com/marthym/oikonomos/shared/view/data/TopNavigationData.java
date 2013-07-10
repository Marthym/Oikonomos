package com.marthym.oikonomos.shared.view.data;

import java.io.Serializable;

import com.marthym.oikonomos.shared.model.User;

public class TopNavigationData implements HasCurrentUserData, Serializable {
	private static final long serialVersionUID = 3893907550303040141L;
	private User authentifiedUser;
	
	public void setAuthentifiedUser(User authentifiedUser) {
		this.authentifiedUser = authentifiedUser;
	}
	
	@Override
	public User getCurrentUserData() {
		return authentifiedUser;
	}
	
}
