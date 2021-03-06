package com.marthym.oikonomos.shared.services;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.marthym.oikonomos.shared.exceptions.OikonomosException;
import com.marthym.oikonomos.shared.model.Account;

@RemoteServiceRelativePath("../rpc/accountService")
public interface AccountService extends RemoteService {
	public long getCount() throws OikonomosException;
	public List<Account> getList(boolean sorted) throws OikonomosException;
	public Account getEntity(long accountId) throws OikonomosException;
	public Account addOrUpdateEntity(Account account) throws OikonomosException;
	public void delete(long accountId) throws OikonomosException;
}
