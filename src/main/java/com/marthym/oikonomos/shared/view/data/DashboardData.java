package com.marthym.oikonomos.shared.view.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.marthym.oikonomos.shared.exceptions.OikonomosUnauthorizedException;
import com.marthym.oikonomos.shared.model.Account;
import com.marthym.oikonomos.shared.model.User;

public class DashboardData implements HasCurrentUserData, HasEntityCountData, Serializable {
	private static final long serialVersionUID = -6176824519326945593L;
	
	private TopNavigationData topNavigation;
	private LeftMenuData leftMenuData;
	private List<Account> accountsList = new ArrayList<Account>();

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
	
	public void addAccount(Account account) {
		accountsList.add(account);
	}

	@Override
	public User getCurrentUserData() throws OikonomosUnauthorizedException {
		if (topNavigation != null) {
			return topNavigation.getCurrentUserData();
		} else {
			throw new OikonomosUnauthorizedException();
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
