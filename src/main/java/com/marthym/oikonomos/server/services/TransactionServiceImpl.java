package com.marthym.oikonomos.server.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.marthym.oikonomos.server.repositories.TransactionRepository;
import com.marthym.oikonomos.shared.exceptions.OikonomosException;
import com.marthym.oikonomos.shared.exceptions.OikonomosUnauthorizedException;
import com.marthym.oikonomos.shared.model.Transaction;
import com.marthym.oikonomos.shared.model.User;
import com.marthym.oikonomos.shared.services.TransactionService;

public class TransactionServiceImpl extends RemoteServiceServlet implements TransactionService {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionServiceImpl.class);

	@Autowired
	private TransactionRepository transactionRepository;
	
	@Override
	@Secured("ROLE_USER")
	public long getCount() throws OikonomosException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) throw new OikonomosUnauthorizedException("error.message.user.unauthorized", "No authentification found !");
		User authentifiedUser = (User)authentication.getPrincipal();
		
		return transactionRepository.countByOwner(authentifiedUser.getUserEmail());
	}

	@Override
	@Secured("ROLE_USER")
	public Transaction find(long id) throws OikonomosException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) throw new OikonomosUnauthorizedException("error.message.user.unauthorized", "No authentification found !");
		User authentifiedUser = (User)authentication.getPrincipal();
		
		Transaction result = transactionRepository.findOne(id);
		if (result != null && result.getOwner().equals(authentifiedUser.getUserEmail())) {
			return result;
			
		} else {
			LOGGER.info("Try to load transaction owned by other !");
			throw new OikonomosException(
					"error.message.account.notfound", 
					"Transaction "+id+" not fount for user "+authentifiedUser.getUserEmail());
		}
	}

	@Override
	@Secured("ROLE_USER")
	public List<Transaction> findAll() throws OikonomosException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) throw new OikonomosUnauthorizedException("error.message.user.unauthorized", "No authentification found !");
		User authentifiedUser = (User)authentication.getPrincipal();

		List<Transaction> transactions = transactionRepository.findByOwnerOrderByDateDesc(authentifiedUser.getUserEmail());
		return transactions;
	}

	@Override
	@Secured("ROLE_USER")
	public Transaction addOrUpdateEntity(Transaction transaction) throws OikonomosException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Secured("ROLE_USER")
	public void delete(long id) throws OikonomosException {
		// TODO Auto-generated method stub
		
	}

}
