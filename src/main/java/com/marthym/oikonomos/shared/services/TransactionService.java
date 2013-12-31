package com.marthym.oikonomos.shared.services;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.marthym.oikonomos.shared.exceptions.OikonomosException;
import com.marthym.oikonomos.shared.model.Account;
import com.marthym.oikonomos.shared.model.dto.TransactionDTO;

@RemoteServiceRelativePath("../rpc/transactionService")
public interface TransactionService extends RemoteService {
	public long getCount(Account account) throws OikonomosException;
	public TransactionDTO find(long id) throws OikonomosException;
	public List<TransactionDTO> findAllForAccount(Account account) throws OikonomosException;
	public TransactionDTO addOrUpdateEntity(TransactionDTO transaction) throws OikonomosException;
	public Account delete(long id) throws OikonomosException;
}
