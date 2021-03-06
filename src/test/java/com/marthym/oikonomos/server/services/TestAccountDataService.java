package com.marthym.oikonomos.server.services;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

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

import com.marthym.oikonomos.shared.exceptions.OikonomosException;
import com.marthym.oikonomos.shared.model.Account;
import com.marthym.oikonomos.shared.model.AccountType;
import com.marthym.oikonomos.shared.model.User;
import com.marthym.oikonomos.shared.services.AccountService;

@ContextConfiguration(locations={"classpath:/applicationContext-test.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class TestAccountDataService {
	private static final Logger LOGGER = LoggerFactory.getLogger(TestAccountDataService.class);
	
	@Autowired
	private AccountService accountService;
	
	private static SecurityContext scUser;
		
	@BeforeClass
	public static void beforeClass() {
		System.out.println("------------TestAccountDataService: start -------");
	}
	
	@BeforeClass
	public static void initSecurityContext() {
		User currentUser = new User("test@localhost.com", "marthym", "myhtram", "password");
		// Create de session if user is valid
		List<GrantedAuthority> authorities = new LinkedList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		Authentication auth = new UsernamePasswordAuthenticationToken(currentUser, "password", authorities);
		scUser = new SecurityContextImpl();
		scUser.setAuthentication(auth);
	}
	
	@Test
	public void testSecurityCredentials() {
		SecurityContextHolder.clearContext();
		User currentUser = new User("test@localhost.com", "marthym", "myhtram", "password");
		// Create de session if user is valid
		List<GrantedAuthority> authorities = new LinkedList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("ROLE_TEST"));
		Authentication auth = new UsernamePasswordAuthenticationToken(currentUser, null, authorities);
		SecurityContext scTest = new SecurityContextImpl();
		scTest.setAuthentication(auth);
		
		SecurityContextHolder.setContext(scTest);
		
		try {
			accountService.addOrUpdateEntity(null);
			fail("Security breach ...");
		} catch (AccessDeniedException e) {
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		try {
			accountService.delete(1L);
			fail("Security breach ...");
		} catch (AccessDeniedException e) {
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		try {
			accountService.getCount();
			fail("Security breach ...");
		} catch (AccessDeniedException e) {
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		try {
			accountService.getEntity(1L);
			fail("Security breach ...");
		} catch (AccessDeniedException e) {
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		try {
			accountService.getList(true);
			fail("Security breach ...");
		} catch (AccessDeniedException e) {
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		SecurityContextHolder.clearContext();
		
		try {
			accountService.addOrUpdateEntity(null);
			fail("Security breach ...");
		} catch (AuthenticationCredentialsNotFoundException e) {
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		try {
			accountService.delete(1L);
			fail("Security breach ...");
		} catch (AuthenticationCredentialsNotFoundException e) {
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		try {
			accountService.getCount();
			fail("Security breach ...");
		} catch (AuthenticationCredentialsNotFoundException e) {
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		try {
			accountService.getEntity(1L);
			fail("Security breach ...");
		} catch (AuthenticationCredentialsNotFoundException e) {
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		try {
			accountService.getList(true);
			fail("Security breach ...");
		} catch (AuthenticationCredentialsNotFoundException e) {
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
	}
		
	@Test
	@Transactional
	public void testAddOrUpdateEntity() {
		Account newAccount = new Account("test@localhost.com");
		newAccount.setAccountName("Test Account");
		newAccount.setAccountCurrency("EUR");
		newAccount.setAccountKey(12);
		newAccount.setAccountNumber(123456);
		newAccount.setAccountType(AccountType.BANK_ACCOUNT);
		newAccount.setCurrentAmount(1000.0);
		newAccount.setPointedAmount(900.0);
		newAccount.setInitialAmount(200.0);
		
		SecurityContextHolder.setContext(scUser);
		try {
			newAccount = accountService.addOrUpdateEntity(newAccount);
			assertNotNull(newAccount);
			LOGGER.info("New account id: "+newAccount.getId());
		} catch (OikonomosException e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		SecurityContextHolder.clearContext();
	}
	
	@Test
	public void testGetCount() {
		SecurityContextHolder.setContext(scUser);
		
		try {
			long count = accountService.getCount();
			assertEquals(1, count);
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		SecurityContextHolder.clearContext();
	}
	
	@Test
	public void testGetEntity() {
		SecurityContextHolder.setContext(scUser);
		
		try {
			Account account = accountService.getEntity(1L);
			assertNotNull(account);
			assertEquals(1L, (long)account.getId());
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		SecurityContextHolder.clearContext();
	}

	@Test
	public void testGetList() {
		SecurityContextHolder.setContext(scUser);
		
		try {
			List<Account> accounts = accountService.getList(true);
			assertNotNull(accounts);
			assertEquals(1, accounts.size());
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		SecurityContextHolder.clearContext();
	}
	
	@Test
	@Transactional
	public void testDelete() {
		SecurityContextHolder.setContext(scUser);
		
		try {
			accountService.delete(1L);
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		SecurityContextHolder.clearContext();
	}

	@AfterClass
	public static void clearSecurityContext() {
		SecurityContextHolder.clearContext();
	}
	
	@AfterClass
	public static void afterClass() {
		System.out.println("------------TestAccountDataService: end -------");
	}
	
}
