package com.marthym.oikonomos.shared.services;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.marthym.oikonomos.shared.exceptions.OikonomosException;
import com.marthym.oikonomos.shared.model.Transaction;
import com.marthym.oikonomos.shared.model.dto.Category;

@RemoteServiceRelativePath("../rpc/transactionService")
public interface TransactionService extends RemoteService {
	public long getCount() throws OikonomosException;
	public Transaction find(long id) throws OikonomosException;
	public List<Transaction> findAll() throws OikonomosException;
	public Transaction addOrUpdateEntity(Transaction transaction) throws OikonomosException;
	public Transaction addOrUpdateEntity(Transaction transaction, Category category) throws OikonomosException;
	public void delete(long id) throws OikonomosException;
}
