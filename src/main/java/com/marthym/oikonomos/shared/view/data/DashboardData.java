package com.marthym.oikonomos.shared.view.data;

import com.marthym.oikonomos.shared.model.User;

public class DashboardData implements HasCurrentUserData{
	private TopNavigationData topNavigation;
	private User authentifiedUser;

	public TopNavigationData getTopNavigation() {
		return topNavigation;
	}

	public void setTopNavigation(TopNavigationData topNavigation) {
		this.topNavigation = topNavigation;
	}

	public void setAuthentifiedUser(User authentifiedUser) {
		this.authentifiedUser = authentifiedUser;
	}

	@Override
	public User getCurrentUserData() {
		// TODO Auto-generated method stub
		return authentifiedUser;
	}
	
}
