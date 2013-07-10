package com.marthym.oikonomos.server.services;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.marthym.oikonomos.main.client.services.UserService;
import com.marthym.oikonomos.shared.model.User;

@ContextConfiguration(locations={"classpath:/applicationContext-test.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class TestUserService {
	private static final Logger LOGGER = LoggerFactory.getLogger(TestUserService.class);
	public static final Date USER_REGISTERED = new Date();
	public static final Date USER_LASTLOGIN = new Date();
	

	@Autowired
	private UserService userService;
	
	private static SecurityContext scUser;
	
	@BeforeClass
	public static void beforeClass() {
		System.out.println("------------TestUserService: start -------");		
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
	public void testSaveUser() {
		SecurityContextHolder.setContext(scUser);
		try {
			userService.saveUser("test@test.test", "test", "test", "test", USER_REGISTERED, USER_LASTLOGIN);
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getLocalizedMessage());
		}
		SecurityContextHolder.clearContext();
	}

	@Test
	public void testFindUserByMail() {
		SecurityContextHolder.setContext(scUser);
		
		User user = userService.findUserByMail("test@localhost.com");
		assertNotNull(user);
		assertEquals("marthym", user.getUserFirstname());
		LOGGER.info(user.toString());
		
		SecurityContextHolder.clearContext();
	}
	
	@Test
	public void testFindUserById() {
		SecurityContextHolder.setContext(scUser);
		
		User user = userService.findUserById(1L);
		assertNotNull(user);
		assertEquals("marthym", user.getUserFirstname());
		
		LOGGER.info(user.toString());
		SecurityContextHolder.clearContext();
	}

	@Test
	public void testUpdateUser() {
		SecurityContextHolder.setContext(scUser);
		try {
			Calendar calendar = GregorianCalendar.getInstance();
			calendar.add(Calendar.DAY_OF_YEAR, 2);
		
			userService.updateUser("test@test.test", "test", "test", "test", calendar.getTime());
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getLocalizedMessage());
		}
		SecurityContextHolder.clearContext();
	}

	@Test
	public void testDeleteUser() {
		SecurityContextHolder.setContext(scUser);
		try {
			userService.deleteUser("test@test.test");
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getLocalizedMessage());
		}
		SecurityContextHolder.clearContext();
	}

	@AfterClass
	public static void clearSecurityContext() {
		SecurityContextHolder.clearContext();
	}
	
	@AfterClass
	public static void afterClass() {
		System.out.println("------------TestUserService: end -------");
	}
	
}
