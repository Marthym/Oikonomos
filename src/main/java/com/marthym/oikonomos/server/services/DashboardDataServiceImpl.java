package com.marthym.oikonomos.server.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.marthym.oikonomos.main.client.services.DashboardDataService;
import com.marthym.oikonomos.shared.model.User;
import com.marthym.oikonomos.shared.view.data.DashboardData;
import com.marthym.oikonomos.shared.view.data.EntityType;
import com.marthym.oikonomos.shared.view.data.LeftMenuData;
import com.marthym.oikonomos.shared.view.data.TopNavigationData;

@Repository
@Service("dashboardDataService")
public class DashboardDataServiceImpl extends RemoteServiceServlet implements DashboardDataService {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(DashboardDataServiceImpl.class);

	@Override
	@Secured("ROLE_USER")
	public DashboardData getDashboardData() {
		DashboardData data = new DashboardData();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User authentifiedUser = (User)authentication.getPrincipal();
		data.setAuthentifiedUser(authentifiedUser);
		
		data.setTopNavigation(new TopNavigationData());
		LeftMenuData leftMenuData = new LeftMenuData();
		data.setLeftMenuData(leftMenuData);
		
		leftMenuData.addEntityCount(EntityType.ACCOUNT, 5);
		leftMenuData.addEntityCount(EntityType.REPORT, 2);
		
		return data;
	}

	@Override
	@Secured("ROLE_USER")
	public LeftMenuData getLeftMenuData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Secured("ROLE_USER")
	public TopNavigationData getTopNavigationData() {
		// TODO Auto-generated method stub
		return null;
	}



}
