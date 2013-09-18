package com.marthym.oikonomos.shared.services;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.marthym.oikonomos.shared.exceptions.OikonomosException;
import com.marthym.oikonomos.shared.view.data.DashboardData;
import com.marthym.oikonomos.shared.view.data.LeftMenuData;
import com.marthym.oikonomos.shared.view.data.TopNavigationData;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("../rpc/dashboardDataService")
public interface DashboardDataService extends RemoteService {
	public DashboardData getDashboardData() throws OikonomosException;
	public LeftMenuData getLeftMenuData() throws OikonomosException;
	public TopNavigationData getTopNavigationData() throws OikonomosException;
}
