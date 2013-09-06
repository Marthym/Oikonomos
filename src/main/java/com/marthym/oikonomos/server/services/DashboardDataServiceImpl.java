package com.marthym.oikonomos.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.marthym.oikonomos.main.client.services.AccountService;
import com.marthym.oikonomos.main.client.services.CategoryService;
import com.marthym.oikonomos.main.client.services.DashboardDataService;
import com.marthym.oikonomos.shared.exceptions.OikonomosException;
import com.marthym.oikonomos.shared.exceptions.OikonomosUnauthorizedException;
import com.marthym.oikonomos.shared.model.User;
import com.marthym.oikonomos.shared.view.data.DashboardData;
import com.marthym.oikonomos.shared.view.data.EntityType;
import com.marthym.oikonomos.shared.view.data.LeftMenuData;
import com.marthym.oikonomos.shared.view.data.TopNavigationData;

@Repository
@Service("dashboardDataService")
public class DashboardDataServiceImpl extends RemoteServiceServlet implements DashboardDataService {
	private static final long serialVersionUID = 1L;

	@Autowired
	AccountService accountService;
	
	@Autowired
	CategoryService categoryService;
	
	@Override
	@Secured("ROLE_USER")
	public DashboardData getDashboardData() throws OikonomosException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) throw new OikonomosUnauthorizedException("error.message.user.unauthorized", "No authentification found !");
		
		DashboardData data = new DashboardData();
		data.setTopNavigation(getTopNavigationData());
		data.setLeftMenuData(getLeftMenuData());

		return data;
	}

	@Override
	@Secured("ROLE_USER")
	public LeftMenuData getLeftMenuData() throws OikonomosException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) throw new OikonomosUnauthorizedException("error.message.user.unauthorized", "No authentification found !");
		
		LeftMenuData leftMenuData = new LeftMenuData();
		leftMenuData.addEntityCount(EntityType.ACCOUNT, (int)accountService.getCount());
		leftMenuData.addEntityCount(EntityType.CATEGORY, (int)categoryService.getCount());
		//TODO: Add all entity found
		
		return leftMenuData;
	}

	@Override
	@Secured("ROLE_USER")
	public TopNavigationData getTopNavigationData() throws OikonomosException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) throw new OikonomosUnauthorizedException("error.message.user.unauthorized", "No authentification found !");
		User authentifiedUser = (User)authentication.getPrincipal();
		TopNavigationData topNavigationData = new TopNavigationData();
		topNavigationData.setAuthentifiedUser(authentifiedUser);
		return topNavigationData;
	}

}
