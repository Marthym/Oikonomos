package com.marthym.oikonomos.server.services;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.marthym.oikonomos.server.repositories.CategoryRepository;
import com.marthym.oikonomos.server.repositories.TransactionRepository;
import com.marthym.oikonomos.shared.exceptions.OikonomosException;
import com.marthym.oikonomos.shared.exceptions.OikonomosUnauthorizedException;
import com.marthym.oikonomos.shared.model.Account;
import com.marthym.oikonomos.shared.model.Transaction;
import com.marthym.oikonomos.shared.model.User;
import com.marthym.oikonomos.shared.model.dto.Category;
import com.marthym.oikonomos.shared.services.TransactionService;

@Repository
@Service("TransactionService")
public class TransactionServiceImpl extends RemoteServiceServlet implements TransactionService {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionServiceImpl.class);

	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Override
	@Secured("ROLE_USER")
	public long getCount(Account account) throws OikonomosException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) throw new OikonomosUnauthorizedException("error.message.user.unauthorized", "No authentification found !");
		User authentifiedUser = (User)authentication.getPrincipal();
		if (!authentifiedUser.getUserEmail().equals(account.getAccountOwner())) {
			throw new OikonomosException(
					"error.message.account.notowned", 
					"Account "+account.getAccountName()+" must be owned by "+authentifiedUser.getUserEmail());
		}
		
		return transactionRepository.countByAccount(account);
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
			throw new OikonomosException(
					"error.message.account.notfound", 
					"Transaction "+id+" not fount for user "+authentifiedUser.getUserEmail());
		}
	}

	@Override
	@Secured("ROLE_USER")
	public List<Transaction> findAllForAccount(Account account) throws OikonomosException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) throw new OikonomosUnauthorizedException("error.message.user.unauthorized", "No authentification found !");
		User authentifiedUser = (User)authentication.getPrincipal();
		
		if (!authentifiedUser.getUserEmail().equals(account.getAccountOwner())) {
			throw new OikonomosException(
					"error.message.account.notowned", 
					"Account "+account.getAccountName()+" must be owned by "+authentifiedUser.getUserEmail());
		}
		List<Transaction> transactions = transactionRepository.findByAccount(account, sortByDate());
		return transactions;
	}

	@Override
	@Secured("ROLE_USER")
	public Transaction addOrUpdateEntity(Transaction transaction) throws OikonomosException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) throw new OikonomosUnauthorizedException("error.message.user.unauthorized", "No authentification found !");
		User authentifiedUser = (User)authentication.getPrincipal();
		
		if (!authentifiedUser.getUserEmail().equals(transaction.getOwner())) {
			throw new OikonomosException(
					"error.message.account.notowned", 
					"Account "+transaction.getAccount().getAccountName()+" must be owned by "+authentifiedUser.getUserEmail());
		}
		return transactionRepository.save(transaction);
	}

	@Override
	@Secured("ROLE_USER")
	public Transaction addOrUpdateEntity(Transaction transaction, Category dtoCategory) throws OikonomosException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) throw new OikonomosUnauthorizedException("error.message.user.unauthorized", "No authentification found !");
		User authentifiedUser = (User)authentication.getPrincipal();

		if (!authentifiedUser.getUserEmail().equals(transaction.getOwner())) {
			throw new OikonomosException(
					"error.message.account.notowned", 
					"Account "+transaction.getAccount().getAccountName()+" must be owned by "+authentifiedUser.getUserEmail());
		}
		
		if (dtoCategory.getEntityOwner() != null && !dtoCategory.getEntityOwner().equals(authentifiedUser.getUserEmail())) {
			throw new OikonomosException(
					"error.message.entity.notfound", 
					"Category "+dtoCategory.getEntityId()+" must be owned by "+authentifiedUser.getUserEmail());
		}
		
		com.marthym.oikonomos.shared.model.Category category = categoryRepository.findOne(dtoCategory.getEntityId());
		if (category == null) {
			throw new OikonomosException(
					"error.message.category.notfound", 
					Arrays.asList(new String[]{dtoCategory.getEntityId()+"-"+dtoCategory.getAbsoluteDescription()}),
					"Category "+dtoCategory.getEntityId()+" not found in database !");
		}
		
		transaction.setCategory(category);
		return transactionRepository.save(transaction);
	}

	@Override
	@Secured("ROLE_USER")
	public void delete(long id) throws OikonomosException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) throw new OikonomosUnauthorizedException("error.message.user.unauthorized", "No authentification found !");
		User authentifiedUser = (User)authentication.getPrincipal();
		
		Transaction transaction = transactionRepository.findOne(id);
		if (!authentifiedUser.getUserEmail().equals(transaction.getOwner())) {
			throw new OikonomosException(
					"error.message.account.notowned", 
					"Account "+transaction.getAccount().getAccountName()+" must be owned by "+authentifiedUser.getUserEmail());
		}
		
		if (transaction.getReconciliation() != null) {
			LOGGER.warn("{} delete transaction '{}' which is reconciliate !", authentifiedUser.getUserEmail(), transaction.getId());
		}
		
		transactionRepository.delete(transaction);
	}

	private static final Sort sortByDate() {
		return new Sort(Direction.ASC, "transactionDate");
	}

}
