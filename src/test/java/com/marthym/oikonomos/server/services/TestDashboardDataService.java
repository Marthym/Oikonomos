package com.marthym.oikonomos.server.services;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
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

import com.marthym.oikonomos.main.client.services.DashboardDataService;
import com.marthym.oikonomos.shared.exceptions.OikonomosException;
import com.marthym.oikonomos.shared.model.User;
import com.marthym.oikonomos.shared.view.data.DashboardData;
import com.marthym.oikonomos.shared.view.data.EntityType;
import com.marthym.oikonomos.shared.view.data.LeftMenuData;
import com.marthym.oikonomos.shared.view.data.TopNavigationData;

@ContextConfiguration(locations={"classpath:/applicationContext-test.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class TestDashboardDataService {
	@Autowired
	private DashboardDataService dashboardDataService;
	
	private static SecurityContext scUser;

	@BeforeClass
	public static void beforeClass() {
		System.out.println("------------TestDashboardDataService: start -------");		
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
		User currentUser = new User("test@localhost.com", "marthym", "myhtram", "password");
		// Create de session if user is valid
		List<GrantedAuthority> authorities = new LinkedList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("ROLE_TEST"));
		Authentication auth = new UsernamePasswordAuthenticationToken(currentUser, "password", authorities);
		SecurityContext scTest = new SecurityContextImpl();
		scTest.setAuthentication(auth);
		
		SecurityContextHolder.setContext(scTest);
		
		try {
			dashboardDataService.getDashboardData();
			fail("Security breach ...");
		} catch (AccessDeniedException e) {
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		try {
			dashboardDataService.getLeftMenuData();
			fail("Security breach ...");
		} catch (AccessDeniedException e) {
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		try {
			dashboardDataService.getTopNavigationData();
			fail("Security breach ...");
		} catch (AccessDeniedException e) {
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		SecurityContextHolder.clearContext();
		
		try {
			dashboardDataService.getDashboardData();
			fail("Security breach ...");
		} catch (AuthenticationCredentialsNotFoundException e) {
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		try {
			dashboardDataService.getLeftMenuData();
			fail("Security breach ...");
		} catch (AuthenticationCredentialsNotFoundException e) {
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		try {
			dashboardDataService.getTopNavigationData();
			fail("Security breach ...");
		} catch (AuthenticationCredentialsNotFoundException e) {
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
	}
	
	@Test
	public void testGetCountFor() {
		SecurityContextHolder.setContext(scUser);
		try {
			DashboardData dashboardData = dashboardDataService.getDashboardData();
			assertEquals(1, dashboardData.getCountFor(EntityType.ACCOUNT));
			assertNotNull(dashboardData.getCurrentUserData());
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		SecurityContextHolder.clearContext();
	}

	@Test
	public void testGetLeftMenuData() {
		SecurityContextHolder.setContext(scUser);
		try {
			LeftMenuData leftMenuData = dashboardDataService.getLeftMenuData();
			assertNotNull(leftMenuData);
		} catch (OikonomosException e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		SecurityContextHolder.clearContext();
	}
	
	@Test
	public void testGetTopNavigationData() {
		SecurityContextHolder.setContext(scUser);
		try {
			TopNavigationData topNavigationData = dashboardDataService.getTopNavigationData();
			assertNotNull(topNavigationData);
			assertEquals("test@localhost.com", topNavigationData.getCurrentUserData().getUserEmail());
		} catch (OikonomosException e) {
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
		System.out.println("------------TestDashboardDataService: end -------");
	}
	
}
