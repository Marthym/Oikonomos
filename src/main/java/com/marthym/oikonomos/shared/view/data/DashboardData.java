package com.marthym.oikonomos.shared.view.data;

import java.io.Serializable;

import com.marthym.oikonomos.shared.exceptions.OikonomosUnathorizedException;
import com.marthym.oikonomos.shared.model.User;

public class DashboardData implements HasCurrentUserData, HasEntityCountData, Serializable {
	private static final long serialVersionUID = 3041318166635371951L;

	private TopNavigationData topNavigation;
	private LeftMenuData leftMenuData;

	public TopNavigationData getTopNavigation() {
		return topNavigation;
	}

	public void setTopNavigation(TopNavigationData topNavigation) {
		this.topNavigation = topNavigation;
	}

	public void setLeftMenuData(LeftMenuData leftMenuData) {
		this.leftMenuData = leftMenuData;
	}
	
	public LeftMenuData getLeftMenuData() {
		return leftMenuData;
	}

	@Override
	public User getCurrentUserData() throws OikonomosUnathorizedException {
		if (topNavigation != null) {
			return topNavigation.getCurrentUserData();
		} else {
			throw new OikonomosUnathorizedException();
		}
	}

	@Override
	public int getCountFor(EntityType entityName) {
		if (leftMenuData != null) {
			return leftMenuData.getCountFor(entityName);
		} else {
			return 0;
		}
	}
	
}
