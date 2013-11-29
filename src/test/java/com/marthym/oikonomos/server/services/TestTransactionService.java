package com.marthym.oikonomos.server.services;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.marthym.oikonomos.server.repositories.AccountRepository;
import com.marthym.oikonomos.server.repositories.CategoryRepository;
import com.marthym.oikonomos.server.repositories.PayeeRepository;
import com.marthym.oikonomos.shared.exceptions.OikonomosException;
import com.marthym.oikonomos.shared.model.Account;
import com.marthym.oikonomos.shared.model.Category;
import com.marthym.oikonomos.shared.model.Payee;
import com.marthym.oikonomos.shared.model.User;
import com.marthym.oikonomos.shared.model.dto.TransactionDTO;
import com.marthym.oikonomos.shared.services.AuthenticationService;
import com.marthym.oikonomos.shared.services.TransactionService;

@ContextConfiguration(locations={"classpath:/applicationContext-test.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class TestTransactionService {
	private static final Logger LOGGER = LoggerFactory.getLogger(TestTransactionService.class);	

	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private PayeeRepository payeeRepository;
	
	private static SecurityContext scUser;
	
	@BeforeClass
	public static void beforeClass() {
		System.out.println("------------TestTransactionService: start -------");		
	}
	
	@BeforeClass
	public static void initSecurityContext() {
		Map<String, String> details = new HashMap<String, String>();
		details.put(AuthenticationService.SESSION_DETAIL_LOCALE, "us");
		
		User currentUser = new User("test@localhost.com", "marthym", "myhtram", "password");
		// Create de session if user is valid
		List<GrantedAuthority> authorities = new LinkedList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		Authentication auth = new UsernamePasswordAuthenticationToken(currentUser, "password", authorities);
		((UsernamePasswordAuthenticationToken)auth).setDetails(details);
		scUser = new SecurityContextImpl();
		scUser.setAuthentication(auth);
	}
	
	@Test
	public void testSecurityCredentials() {	
		User currentUser = new User("test@localhost.com", "marthym", "myhtram", "password");
		// Create session if user is valid
		List<GrantedAuthority> authorities = new LinkedList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("ROLE_TEST"));
		Authentication auth = new UsernamePasswordAuthenticationToken(currentUser, "password", authorities);
		SecurityContext scTest = new SecurityContextImpl();
		scTest.setAuthentication(auth);
		
		SecurityContextHolder.setContext(scTest);
		
		try {
			transactionService.addOrUpdateEntity(null);
			fail("Security breach ...");
		} catch (AccessDeniedException e) {
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}

		try {
			transactionService.delete(1L);
			fail("Security breach ...");
		} catch (AccessDeniedException e) {
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		try {
			transactionService.find(1L);
			fail("Security breach ...");
		} catch (AccessDeniedException e) {
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		try {
			transactionService.findAllForAccount(null);
			fail("Security breach ...");
		} catch (AccessDeniedException e) {
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		try {
			transactionService.getCount(null);
			fail("Security breach ...");
		} catch (AccessDeniedException e) {
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		
		SecurityContextHolder.clearContext();
		
		try {
			transactionService.addOrUpdateEntity(null);
			fail("Security breach ...");
		} catch (AuthenticationCredentialsNotFoundException e) {
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}

		try {
			transactionService.delete(1L);
			fail("Security breach ...");
		} catch (AuthenticationCredentialsNotFoundException e) {
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		try {
			transactionService.find(1L);
			fail("Security breach ...");
		} catch (AuthenticationCredentialsNotFoundException e) {
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		try {
			transactionService.findAllForAccount(null);
			fail("Security breach ...");
		} catch (AuthenticationCredentialsNotFoundException e) {
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		try {
			transactionService.getCount(null);
			fail("Security breach ...");
		} catch (AuthenticationCredentialsNotFoundException e) {
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}

	}

	@Test
	@Transactional
	public void testAddOrUpdateEntity() {
		SecurityContextHolder.setContext(scUser);
		Account account = accountRepository.findOne(1L);
		Category daoCategory = categoryRepository.findOne(1L);
		
		{
			com.marthym.oikonomos.shared.model.dto.CategoryDTO dtoCategory = com.marthym.oikonomos.shared.model.dto.CategoryDTO.create(daoCategory, "fr", false);
			Payee payee = new Payee("Test Payee");
			TransactionDTO trans = new TransactionDTO(account);
			trans.setTransactionDate(new Date());
			trans.setPayee(payee);
			trans.setCategory(dtoCategory);
			try {
				trans = transactionService.addOrUpdateEntity(trans);
				assertNotNull(trans);
				LOGGER.info("New transaction id: "+trans.getId());
			} catch (OikonomosException e) {
				fail(e.getClass()+": "+e.getMessage());
			}
		}
		
		{			
			com.marthym.oikonomos.shared.model.dto.CategoryDTO dtoCategory = com.marthym.oikonomos.shared.model.dto.CategoryDTO.create(daoCategory, "fr", false);
			Payee payee = payeeRepository.findOne(1L);
			TransactionDTO trans = new TransactionDTO(account);
			trans.setTransactionDate(new Date());
			trans.setPayee(payee);
			trans.setCategory(dtoCategory);
			try {
				trans = transactionService.addOrUpdateEntity(trans);
				assertNotNull(trans);
				LOGGER.info("New transaction id: "+trans.getId());
			} catch (OikonomosException e) {
				fail(e.getClass()+": "+e.getMessage());
			}
		}
		
		SecurityContextHolder.clearContext();
	}
	
	@Test
	public void testFind() {
		SecurityContextHolder.setContext(scUser);
		try {
			TransactionDTO transaction = transactionService.find(1L);
			assertNotNull(transaction);
			assertEquals("Emy H. Tram", transaction.getPayee().getName());
		} catch (OikonomosException e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		SecurityContextHolder.clearContext();
	}
	
	@Test
	public void testFindAllForAccount() {
		SecurityContextHolder.setContext(scUser);
		Account account = accountRepository.findOne(1L);
		try {
			List<TransactionDTO> transactions = transactionService.findAllForAccount(account);
			assertNotNull(transactions);
			assertEquals(2, transactions.size());
		} catch (OikonomosException e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		SecurityContextHolder.clearContext();
	}
	
	@Test
	public void testGetCount() {
		SecurityContextHolder.setContext(scUser);
		Account account = accountRepository.findOne(1L);
		try {
			long count = transactionService.getCount(account);
			assertEquals(2, count);
		} catch (OikonomosException e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		SecurityContextHolder.clearContext();
	}
	
	@Test
	@Transactional
	public void testDelete() {
		SecurityContextHolder.setContext(scUser);
		try {
			transactionService.find(2L);
			transactionService.delete(2L);
		} catch (OikonomosException e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		try {
			transactionService.find(2L);
			fail("Not deleted !");
		} catch (OikonomosException e) {}
		
		SecurityContextHolder.clearContext();
	}

	@AfterClass
	public static void clearSecurityContext() {
		SecurityContextHolder.clearContext();
	}
	
	@AfterClass
	public static void afterClass() {
		System.out.println("------------TestTransactionService: end -------");
	}
	
}
