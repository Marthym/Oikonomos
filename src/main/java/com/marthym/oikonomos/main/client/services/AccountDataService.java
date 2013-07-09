package com.marthym.oikonomos.main.client.services;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.marthym.oikonomos.shared.exceptions.OikonomosException;
import com.marthym.oikonomos.shared.model.Account;

@RemoteServiceRelativePath("../rpc/accountDataService")
public interface AccountDataService extends RemoteService {
	public long getCount();
	public List<Account> getList();
	public Account getEntity(long accountId) throws OikonomosException;
	public void addOrUpdateEntity(Account account) throws OikonomosException;
	public void delete(long accountId) throws OikonomosException;
}
