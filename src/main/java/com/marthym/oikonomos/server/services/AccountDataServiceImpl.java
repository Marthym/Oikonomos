package com.marthym.oikonomos.server.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.marthym.oikonomos.main.client.services.AccountDataService;
import com.marthym.oikonomos.server.repositories.AccountRepository;
import com.marthym.oikonomos.shared.exceptions.OikonomosException;
import com.marthym.oikonomos.shared.exceptions.OikonomosUnathorizedException;
import com.marthym.oikonomos.shared.model.Account;
import com.marthym.oikonomos.shared.model.User;

@Repository
@Service("accountDataService")
public class AccountDataServiceImpl extends RemoteServiceServlet implements AccountDataService {
	private static final long serialVersionUID = 1L;

	@Autowired
	private AccountRepository accountRepository;

	@Override
	@Secured("ROLE_USER")
	public long getCount() throws OikonomosException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) throw new OikonomosUnathorizedException("error.message.user.unauthorized", "No authentification found !");
		User authentifiedUser = (User)authentication.getPrincipal();
		return accountRepository.countByAccountOwner(authentifiedUser.getUserEmail());
	}



	@Override
	@Secured("ROLE_USER")
	public List<Account> getList(boolean sorted) throws OikonomosException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) throw new OikonomosUnathorizedException("error.message.user.unauthorized", "No authentification found !");
		User authentifiedUser = (User)authentication.getPrincipal();
		if (sorted) {
			return accountRepository.findByAccountOwnerOrderByAccountTypeDesc(authentifiedUser.getUserEmail());
		} else {
			return accountRepository.findByAccountOwner(authentifiedUser.getUserEmail());
		}
	}



	@Override
	@Secured("ROLE_USER")
	public Account getEntity(long accountId) throws OikonomosException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) throw new OikonomosUnathorizedException("error.message.user.unauthorized", "No authentification found !");
		User authentifiedUser = (User)authentication.getPrincipal();

		Account myAccount = accountRepository.findOne(accountId);
		if (myAccount.getAccountOwner().equals(authentifiedUser.getUserEmail())) {
			return myAccount;
			
		} else {
			throw new OikonomosException(
					"error.message.account.notfound", 
					"Account "+accountId+" not fount for user "+authentifiedUser.getUserEmail());
		}
	}



	@Override
	@Secured("ROLE_USER")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Account addOrUpdateEntity(Account account) throws OikonomosException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) throw new OikonomosUnathorizedException("error.message.user.unauthorized", "No authentification found !");
		User authentifiedUser = (User)authentication.getPrincipal();

		if (!authentifiedUser.getUserEmail().equals(account.getAccountOwner())) {
			throw new OikonomosException(
					"error.message.account.notowned", 
					"Account "+account.getAccountName()+" must be owned by "+authentifiedUser.getUserEmail());
		}
		
		return accountRepository.save(account);
	}

	@Override
	@Secured("ROLE_USER")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void delete(long accountId) throws OikonomosException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) throw new OikonomosUnathorizedException("error.message.user.unauthorized", "No authentification found !");
		User authentifiedUser = (User)authentication.getPrincipal();
		
		Account delAccount = accountRepository.findOne(accountId);
		if (!authentifiedUser.getUserEmail().equals(delAccount.getAccountOwner())) {
			throw new OikonomosException(
					"error.message.account.notowned", 
					"Account "+delAccount.getAccountName()+" must be owned by "+authentifiedUser.getUserEmail());
		}
		
		accountRepository.delete(delAccount);
	}

}
