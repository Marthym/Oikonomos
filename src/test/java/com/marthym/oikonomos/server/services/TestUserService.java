package com.marthym.oikonomos.server.services;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.marthym.oikonomos.client.services.UserService;
import com.marthym.oikonomos.shared.model.User;

/**TODO
 * @author fcombes
 * Re-pass the tests alls ares down since User Id has change from mail to ID
 */

@ContextConfiguration(locations={"classpath:/applicationContext-test.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class TestUserService {
	public static final Date USER_REGISTERED = new Date();
	public static final Date USER_LASTLOGIN = new Date();
	

	@Autowired
	private UserService userService;
	
	@BeforeClass
	public static void beforeClass() {
		System.out.println("------------TestUserService: start -------");		
	}
	
	@Test
	public void testSaveUser() {
		try {
			userService.saveUser("test@test.test", "test", "test", "test", USER_REGISTERED, USER_LASTLOGIN);
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getLocalizedMessage());
		}
		
		try {
			User testUser = new User("test2@test.test", "test", "test", "test");
			userService.saveUser(testUser);
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getLocalizedMessage());
		}
	}

	@Test
	public void testFindUser() {
		User user = userService.findUser("marthym");
		assertNotNull(user);
		assertEquals(user.getUserFirstname(), "marthym");
		System.out.println(user);
	}

	@Test
	public void testUpdateUser() {
		try {
			Calendar calendar = GregorianCalendar.getInstance();
			calendar.add(Calendar.DAY_OF_YEAR, 2);
		
			userService.updateUser("test@test.test", "test", "test", "test", calendar.getTime());
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getLocalizedMessage());
		}
	}

	@Test
	public void testSaveOrUpdateUser() {
		try {
			userService.saveOrUpdateUser("test@test.test", "test", "test", "test", USER_REGISTERED, USER_LASTLOGIN);
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getLocalizedMessage());
		}
	}

	@Test
	public void testDeleteUser() {
		try {
			userService.deleteUser("1");
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getLocalizedMessage());
		}
	}

	@AfterClass
	public static void afterClass() {
		System.out.println("------------TestUserService: end -------");
	}
	
}
