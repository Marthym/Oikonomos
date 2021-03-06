package com.marthym.oikonomos.server.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.marthym.oikonomos.shared.exceptions.OikonomosException;
import com.marthym.oikonomos.shared.exceptions.OikonomosUnauthorizedException;
import com.marthym.oikonomos.shared.model.Account;
import com.marthym.oikonomos.shared.model.User;
import com.marthym.oikonomos.shared.model.dto.TransactionDTO;
import com.marthym.oikonomos.shared.services.AccountService;
import com.marthym.oikonomos.shared.services.CategoryService;
import com.marthym.oikonomos.shared.services.TransactionService;
import com.marthym.oikonomos.shared.services.ViewsDataService;
import com.marthym.oikonomos.shared.services.PayeeService;
import com.marthym.oikonomos.shared.view.data.AccountTabbedData;
import com.marthym.oikonomos.shared.view.data.DashboardData;
import com.marthym.oikonomos.shared.view.data.EntityType;
import com.marthym.oikonomos.shared.view.data.LeftMenuData;
import com.marthym.oikonomos.shared.view.data.TopNavigationData;

@Repository
@Service("viewsDataService")
public class ViewsDataServiceImpl extends RemoteServiceServlet implements ViewsDataService {
	private static final long serialVersionUID = 1L;

	@Autowired AccountService accountService;
	@Autowired CategoryService categoryService;
	@Autowired PayeeService payeeService;
	@Autowired TransactionService transactionService;
	
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
		leftMenuData.addEntityCount(EntityType.CATEGORY, (int)categoryService.getCountRootEntities());
		leftMenuData.addEntityCount(EntityType.PAYEE, (int)payeeService.getCount());
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

	@Override
	@Secured("ROLE_USER")
	public AccountTabbedData getAccountTabbedData(long accountId) throws OikonomosException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) throw new OikonomosUnauthorizedException("error.message.user.unauthorized", "No authentification found !");
		
		Account account = accountService.getEntity(accountId);
		List<TransactionDTO> transactions = transactionService.findAllForAccount(account);
		
		AccountTabbedData data = new AccountTabbedData();
		data.setAccount(account);
		data.setTransactions(transactions);
		
		return data;
	}

}
