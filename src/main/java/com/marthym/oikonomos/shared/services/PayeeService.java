package com.marthym.oikonomos.shared.services;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.marthym.oikonomos.shared.exceptions.OikonomosException;
import com.marthym.oikonomos.shared.model.Payee;

@RemoteServiceRelativePath("../rpc/payeeService")
public interface PayeeService extends RemoteService {
	public long getCount() throws OikonomosException;
	public Payee find(long id) throws OikonomosException;
	public List<Payee> findAll() throws OikonomosException;
	public Payee addOrUpdateEntity(Payee payee) throws OikonomosException;
	public void delete(long payeeId) throws OikonomosException;
	public List<Payee> getEntitiesByDescription(String search) throws OikonomosException;
}
