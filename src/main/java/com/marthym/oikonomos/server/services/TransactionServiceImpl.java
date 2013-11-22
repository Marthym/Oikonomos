package com.marthym.oikonomos.server.services;

import java.util.LinkedList;
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
import org.springframework.transaction.annotation.Transactional;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.marthym.oikonomos.server.repositories.CategoryRepository;
import com.marthym.oikonomos.server.repositories.TransactionRepository;
import com.marthym.oikonomos.server.utils.SessionHelper;
import com.marthym.oikonomos.shared.exceptions.OikonomosException;
import com.marthym.oikonomos.shared.exceptions.OikonomosUnauthorizedException;
import com.marthym.oikonomos.shared.model.Account;
import com.marthym.oikonomos.shared.model.Transaction;
import com.marthym.oikonomos.shared.model.User;
import com.marthym.oikonomos.shared.model.dto.TransactionDTO;
import com.marthym.oikonomos.shared.services.TransactionService;

@Repository
@Service("transactionService")
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
	public TransactionDTO find(long id) throws OikonomosException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) throw new OikonomosUnauthorizedException("error.message.user.unauthorized", "No authentification found !");
		User authentifiedUser = (User)authentication.getPrincipal();
		String locale = SessionHelper.getSessionLocale();
		
		Transaction result = transactionRepository.findOne(id);
		if (result != null && result.getOwner().equals(authentifiedUser.getUserEmail())) {
			TransactionDTO dto = TransactionDTO.create(result, locale);
			return dto;
			
		} else {
			throw new OikonomosException(
					"error.message.account.notfound", 
					"Transaction "+id+" not fount for user "+authentifiedUser.getUserEmail());
		}
	}

	@Override
	@Secured("ROLE_USER")
	public List<TransactionDTO> findAllForAccount(Account account) throws OikonomosException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) throw new OikonomosUnauthorizedException("error.message.user.unauthorized", "No authentification found !");
		User authentifiedUser = (User)authentication.getPrincipal();
		String locale = SessionHelper.getSessionLocale();
		
		if (!authentifiedUser.getUserEmail().equals(account.getAccountOwner())) {
			throw new OikonomosException(
					"error.message.account.notowned", 
					"Account "+account.getAccountName()+" must be owned by "+authentifiedUser.getUserEmail());
		}
		List<Transaction> transactions = transactionRepository.findByAccount(account, sortByDate());
		List<TransactionDTO> dtos = new LinkedList<TransactionDTO>();
		for (Transaction dao : transactions) {
			dtos.add(TransactionDTO.create(dao, locale));
		}
		return dtos;
	}

	@Override
	@Secured("ROLE_USER")
	@Transactional
	public TransactionDTO addOrUpdateEntity(TransactionDTO dto) throws OikonomosException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) throw new OikonomosUnauthorizedException("error.message.user.unauthorized", "No authentification found !");
		User authentifiedUser = (User)authentication.getPrincipal();
		String locale = SessionHelper.getSessionLocale();
		
		if (!authentifiedUser.getUserEmail().equals(dto.getAccount().getAccountOwner())) {
			throw new OikonomosException(
					"error.message.account.notowned", 
					"Account "+dto.getAccount().getAccountName()+" must be owned by "+authentifiedUser.getUserEmail());
		}
		
		Transaction dao = null;
		com.marthym.oikonomos.shared.model.Category categoryDAO = null;
		if (dto.getCategory() != null) {
			categoryDAO = categoryRepository.findOne(dto.getCategory().getEntityId());
		}
		if (dto.getId() > 0) {
			dao = transactionRepository.findOne(dto.getId());
			
			if (dao == null) {
				throw new OikonomosException(
						"error.message.account.notfound", 
						"Transaction "+dto.getId()+" not fount for user "+authentifiedUser.getUserEmail());
			}
			
			if (!authentifiedUser.getUserEmail().equals(dao.getOwner())) {
				throw new OikonomosException(
						"error.message.account.notowned", 
						"Account "+dto.getAccount().getAccountName()+" must be owned by "+authentifiedUser.getUserEmail());
			}

		} else {
			dao = new Transaction(dto.getAccount());
		}
		
		dao.setAccountingDocument(dto.getAccountingDocument());
		dao.setBudgetLine(dto.getBudgetLine());
		dao.setCategory(categoryDAO);
		dao.setTransactionComment(dto.getTransactionComment());
		dao.setCredit(dto.getCredit());
		dao.setCurrency(dto.getCurrency());
		dao.setTransactionDate(dto.getTransactionDate());
		dao.setDebit(dto.getDebit());
		dao.setPaiementMean(dto.getPaiementMean());
		dao.setPayee(dto.getPayee());
		dao.setReconciliation(dto.getReconciliation());

		dao = transactionRepository.save(dao);
		
		return TransactionDTO.create(dao, locale);
	}

	@Override
	@Secured("ROLE_USER")
	@Transactional
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
