package com.marthym.oikonomos.server.services;

import static org.junit.Assert.*;

import java.util.Date;
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

import com.marthym.oikonomos.shared.exceptions.OikonomosException;
import com.marthym.oikonomos.shared.model.Payee;
import com.marthym.oikonomos.shared.model.User;
import com.marthym.oikonomos.shared.model.UserProfile;
import com.marthym.oikonomos.shared.services.PayeeService;

@ContextConfiguration(locations={"classpath:/applicationContext-test.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class TestPayeeService {
	private static final Logger LOGGER = LoggerFactory.getLogger(TestPayeeService.class);
	public static final Date USER_REGISTERED = new Date();
	public static final Date USER_LASTLOGIN = new Date();
	

	@Autowired
	private PayeeService payeeService;
	
	private static SecurityContext scUser;
	private static SecurityContext scAdmin;
	
	@BeforeClass
	public static void beforeClass() {
		System.out.println("------------TestPayeeService: start -------");		
	}
	
	@BeforeClass
	public static void initSecurityContext() {
		{
			User currentUser = new User("test@localhost.com", "marthym", "myhtram", "password");
			currentUser.setUserId(99L);
			// Create de session if user is valid
			List<GrantedAuthority> authorities = new LinkedList<GrantedAuthority>();
			authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
			Authentication auth = new UsernamePasswordAuthenticationToken(currentUser, "password", authorities);
			scUser = new SecurityContextImpl();
			scUser.setAuthentication(auth);
		}
		{
			User currentUser = new User("admin@localhost.com", "marthym", "myhtram", "password");
			currentUser.setUserId(98L);
			currentUser.setUserProfile(UserProfile.ADMIN);
			// Create de session if user is valid
			List<GrantedAuthority> authorities = new LinkedList<GrantedAuthority>();
			authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
			authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
			Authentication auth = new UsernamePasswordAuthenticationToken(currentUser, "password", authorities);
			scAdmin = new SecurityContextImpl();
			scAdmin.setAuthentication(auth);
		}
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
			payeeService.addOrUpdateEntity(null);
			fail("Security breach ...");
		} catch (AccessDeniedException e) {
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}

		try {
			payeeService.delete(1L);
			fail("Security breach ...");
		} catch (AccessDeniedException e) {
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		try {
			payeeService.find(1L);
			fail("Security breach ...");
		} catch (AccessDeniedException e) {
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		try {
			payeeService.findAll();
			fail("Security breach ...");
		} catch (AccessDeniedException e) {
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		try {
			payeeService.getCount();
			fail("Security breach ...");
		} catch (AccessDeniedException e) {
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		try {
			payeeService.getEntitiesByDescription(null);
			fail("Security breach ...");
		} catch (AccessDeniedException e) {
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		SecurityContextHolder.clearContext();
		
		try {
			payeeService.addOrUpdateEntity(null);
			fail("Security breach ...");
		} catch (AuthenticationCredentialsNotFoundException e) {
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}

		try {
			payeeService.delete(1L);
			fail("Security breach ...");
		} catch (AuthenticationCredentialsNotFoundException e) {
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		try {
			payeeService.find(1L);
			fail("Security breach ...");
		} catch (AuthenticationCredentialsNotFoundException e) {
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		try {
			payeeService.findAll();
			fail("Security breach ...");
		} catch (AuthenticationCredentialsNotFoundException e) {
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		try {
			payeeService.getCount();
			fail("Security breach ...");
		} catch (AuthenticationCredentialsNotFoundException e) {
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		try {
			payeeService.getEntitiesByDescription(null);
			fail("Security breach ...");
		} catch (AuthenticationCredentialsNotFoundException e) {
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
	}

	@Test
	public void testAddOrUpdateEntity() {
		SecurityContextHolder.setContext(scUser);
		{
			Payee payee = new Payee("Test Payee");
			
			try {
				payee = payeeService.addOrUpdateEntity(payee);
				assertNotNull(payee);
				LOGGER.info("New payee id: "+payee.getEntityId());
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
			Payee payee = payeeService.find(1L);
			assertNotNull(payee);
			assertEquals("Emy H. Tram", payee.getName());
		} catch (OikonomosException e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		SecurityContextHolder.clearContext();
	}
	
	@Test
	public void testFindAll() {
		SecurityContextHolder.setContext(scUser);
		try {
			List<Payee> findAll = payeeService.findAll();
			assertNotNull(findAll);
			assertEquals(2, findAll.size());
		} catch (OikonomosException e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		SecurityContextHolder.clearContext();
	}
	
	@Test
	public void testGetEntitiesByDescription() {
		SecurityContextHolder.setContext(scUser);
		try {
			List<Payee> findAll = payeeService.getEntitiesByDescription("tram");
			assertNotNull(findAll);
			assertEquals(1, findAll.size());
		} catch (OikonomosException e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		SecurityContextHolder.clearContext();
	}
	
	@Test
	public void testGetCount() {
		SecurityContextHolder.setContext(scUser);
		try {
			long count = payeeService.getCount();
			assertEquals(2, count);
		} catch (OikonomosException e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		SecurityContextHolder.clearContext();
	}
	
	@Test
	public void testDelete() {
		SecurityContextHolder.setContext(scUser);
		try {
			payeeService.find(2L);
			payeeService.delete(2L);
		} catch (OikonomosException e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		try {
			payeeService.find(2L);
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
		System.out.println("------------TestPayeeService: end -------");
	}
	
}
