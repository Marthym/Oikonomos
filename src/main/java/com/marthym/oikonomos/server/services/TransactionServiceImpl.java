package com.marthym.oikonomos.server.services;

import java.util.ArrayList;
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
import com.marthym.oikonomos.server.repositories.AccountRepository;
import com.marthym.oikonomos.server.repositories.CategoryRepository;
import com.marthym.oikonomos.server.repositories.PayeeRepository;
import com.marthym.oikonomos.server.repositories.TransactionRepository;
import com.marthym.oikonomos.server.utils.SessionHelper;
import com.marthym.oikonomos.shared.exceptions.OikonomosException;
import com.marthym.oikonomos.shared.exceptions.OikonomosUnauthorizedException;
import com.marthym.oikonomos.shared.model.Account;
import com.marthym.oikonomos.shared.model.Category;
import com.marthym.oikonomos.shared.model.Payee;
import com.marthym.oikonomos.shared.model.Transaction;
import com.marthym.oikonomos.shared.model.User;
import com.marthym.oikonomos.shared.model.dto.CategoryDTO;
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
	
	@Autowired
	private PayeeRepository payeeRepository;
	
	@Autowired
	private AccountRepository accountRepository;
	
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
	@Transactional
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
	@Transactional
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
		List<TransactionDTO> dtos = new ArrayList<TransactionDTO>();
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
		double oldTransactionAmount = 0;
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
			
			Double credit = dao.getCredit();
			if (credit == null) credit = 0D;
			Double debit = dao.getDebit();
			if (debit == null) debit = 0D;
			oldTransactionAmount = debit-credit;

		} else {
			dao = new Transaction(dto.getAccount());			
		}
		
		Category categoryDAO = dao.getCategory();
		if (isCategoryMustBeReloaded(dto.getCategory(), dao.getCategory())) {
			categoryDAO = categoryRepository.findOne(dto.getCategory().getEntityId());
		}
		
		Payee payeeDAO = dto.getPayee();
		if (dao.getPayee() != null) {
			payeeDAO = dao.getPayee();
			if (isPayeeMustBeReloaded(dto.getPayee(), dao.getPayee())){
				payeeDAO = payeeRepository.findOne(dto.getPayee().getEntityId());
			}
		}
		
		Account account = dao.getAccount();
		double currentAmount = account.getCurrentAmount();
		currentAmount += oldTransactionAmount;
		
		dao.setAccountingDocument(dto.getAccountingDocument());
		dao.setBudgetLine(dto.getBudgetLine());
		dao.setCategory(categoryDAO);
		dao.setTransactionComment(dto.getTransactionComment());
		dao.setCredit(dto.getCredit());
		dao.setCurrency(dto.getCurrency());
		dao.setTransactionDate(dto.getTransactionDate());
		dao.setDebit(dto.getDebit());
		dao.setPaiementMean(dto.getPaiementMean());
		dao.setPayee(payeeDAO);
		dao.setReconciliation(dto.getReconciliation());

		Double credit = dao.getCredit();
		if (credit == null) credit = 0D;
		Double debit = dao.getDebit();
		if (debit == null) debit = 0D;
		currentAmount += credit-debit;
		account.setCurrentAmount(currentAmount);
		
		account = accountRepository.save(account);
		dao = transactionRepository.save(dao);
		
		return TransactionDTO.create(dao, locale);
	}

	@Override
	@Secured("ROLE_USER")
	@Transactional
	public Account delete(long id) throws OikonomosException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) throw new OikonomosUnauthorizedException("error.message.user.unauthorized", "No authentification found !");
		User authentifiedUser = (User)authentication.getPrincipal();
		
		Transaction transaction = transactionRepository.findOne(id);
		Account account = transaction.getAccount();
		if (!authentifiedUser.getUserEmail().equals(transaction.getOwner())) {
			throw new OikonomosException(
					"error.message.account.notowned", 
					"Account "+account.getAccountName()+" must be owned by "+authentifiedUser.getUserEmail());
		}
		
		if (transaction.getReconciliation() != null) {
			LOGGER.warn("{} delete transaction '{}' which is reconciliate !", authentifiedUser.getUserEmail(), transaction.getId());
		}
		
		// Update account current amount
		double currentAmount = account.getCurrentAmount();
		Double credit = transaction.getCredit();
		if (credit == null) credit = 0D;
		Double debit = transaction.getDebit();
		if (debit == null) debit = 0D;
		currentAmount -= credit-debit;
		account.setCurrentAmount(currentAmount);
		account = accountRepository.save(account);

		transactionRepository.delete(transaction);
		
		return account;
	}

	private static final Sort sortByDate() {
		return new Sort(Direction.ASC, "transactionDate");
	}

	private static boolean isCategoryMustBeReloaded(CategoryDTO dto, Category dao) {
		if (dto == null) return false;
		else if (dto.getEntityId() == null || dto.getEntityId() < 0) return false;
		else if (dto != null && dao == null) return true;
		else if (dto.getEntityId() != dao.getId()) return true;
		else return false;
	}
	
	private static boolean isPayeeMustBeReloaded(Payee dto, Payee dao) {
		if (dto == null) return false;
		else if (dto.getEntityId() == null || dto.getEntityId() < 0) return false;
		else if (dto != null && dao == null) return true;
		else if (dto.getEntityId() != dao.getEntityId()) return true;
		else return false;
	}
}
