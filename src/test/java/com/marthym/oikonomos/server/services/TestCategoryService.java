package com.marthym.oikonomos.server.services;

import static org.junit.Assert.*;

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

import com.marthym.oikonomos.shared.exceptions.OikonomosException;
import com.marthym.oikonomos.shared.model.User;
import com.marthym.oikonomos.shared.model.dto.CategoryDTO;
import com.marthym.oikonomos.shared.services.AuthenticationService;
import com.marthym.oikonomos.shared.services.CategoryService;

@ContextConfiguration(locations={"classpath:/applicationContext-test.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class TestCategoryService {
	private static final Logger LOGGER = LoggerFactory.getLogger(TestCategoryService.class);
	
	@Autowired
	private CategoryService categoryService;
	
	private static SecurityContext scUser;
	
	@BeforeClass
	public static void beforeClass() {
		LOGGER.warn("------------TestCategoryService: start -------");		
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
			categoryService.addOrUpdateEntity(null);
			fail("Security breach ...");
		} catch (AccessDeniedException e) {
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		try {
			categoryService.delete(1L);
			fail("Security breach ...");
		} catch (AccessDeniedException e) {
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		try {
			categoryService.getCount();
			fail("Security breach ...");
		} catch (AccessDeniedException e) {
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		try {
			categoryService.getCountRootEntities();
			fail("Security breach ...");
		} catch (AccessDeniedException e) {
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		try {
			categoryService.getEntitiesByParent(-1L);
			fail("Security breach ...");
		} catch (AccessDeniedException e) {
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		try {
			categoryService.getEntityWithChild(-1L);
			fail("Security breach ...");
		} catch (AccessDeniedException e) {
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		try {
			categoryService.getEntityWithoutChild(-1L);
			fail("Security breach ...");
		} catch (AccessDeniedException e) {
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		try {
			categoryService.getEntitiesByDescription("");
			fail("Security breach ...");
		} catch (AccessDeniedException e) {
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		SecurityContextHolder.clearContext();
		
		try {
			categoryService.addOrUpdateEntity(null);
			fail("Security breach ...");
		} catch (AuthenticationCredentialsNotFoundException e) {
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		try {
			categoryService.delete(1L);
			fail("Security breach ...");
		} catch (AuthenticationCredentialsNotFoundException e) {
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		try {
			categoryService.getCount();
			fail("Security breach ...");
		} catch (AuthenticationCredentialsNotFoundException e) {
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		try {
			categoryService.getCountRootEntities();
			fail("Security breach ...");
		} catch (AuthenticationCredentialsNotFoundException e) {
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		try {
			categoryService.getEntitiesByParent(-1L);
			fail("Security breach ...");
		} catch (AuthenticationCredentialsNotFoundException e) {
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		try {
			categoryService.getEntityWithChild(-1L);
			fail("Security breach ...");
		} catch (AuthenticationCredentialsNotFoundException e) {
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		try {
			categoryService.getEntityWithoutChild(-1L);
			fail("Security breach ...");
		} catch (AuthenticationCredentialsNotFoundException e) {
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		try {
			categoryService.getEntitiesByDescription("");
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
		{
			CategoryDTO category = new CategoryDTO();
			category.setEntityDescription("Test category");
			category.setEntityOwner("test@localhost.com");
			
			try {
				category = categoryService.addOrUpdateEntity(category);
				assertNotNull(category);
				LOGGER.info("New category id: "+category.getEntityId());
			} catch (OikonomosException e) {
				fail(e.getClass()+": "+e.getMessage());
			}
		}
		
		SecurityContextHolder.clearContext();
	}
	
	@Test
	public void testGetCount() {
		SecurityContextHolder.setContext(scUser);
		
		try {
			long count = categoryService.getCount();
			assertEquals(3, count);
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		SecurityContextHolder.clearContext();
	}
	
	@Test
	public void testGetCountRootEntities() {
		SecurityContextHolder.setContext(scUser);
		
		try {
			long count = categoryService.getCountRootEntities();
			assertEquals(1, count);
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		SecurityContextHolder.clearContext();
	}
	
	@Test
	public void testGetEntity() {
		SecurityContextHolder.setContext(scUser);
		
		{
			try {
				CategoryDTO category = categoryService.getEntityWithoutChild(1L);
				assertNotNull(category);
				assertEquals(1L, (long)category.getEntityId());
				assertTrue(category.getChilds().isEmpty());
			} catch (Exception e) {
				fail(e.getClass()+": "+e.getMessage());
			}
		}
		
		{
			try {
				CategoryDTO category = categoryService.getEntityWithChild(1L);
				assertNotNull(category);
				assertEquals(1L, (long)category.getEntityId());
				assertFalse(category.getChilds().isEmpty());
			} catch (Exception e) {
				fail(e.getClass()+": "+e.getMessage());
			}
		}
		SecurityContextHolder.clearContext();
	}

	@Test
	public void testGetEntitiesByParent() {
		SecurityContextHolder.setContext(scUser);
		
		try {
			List<CategoryDTO> categories = categoryService.getEntitiesByParent(1L);
			assertNotNull(categories);
			assertEquals(2, categories.size());
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		SecurityContextHolder.clearContext();
	}
	
	@Test
	public void testGetRootEntities() {
		SecurityContextHolder.setContext(scUser);
		
		try {
			List<CategoryDTO> categories = categoryService.getRootEntities();
			assertNotNull(categories);
			assertEquals(1, categories.size());
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		SecurityContextHolder.clearContext();
	}
	
	@Test
	public void testDelete() {
		SecurityContextHolder.setContext(scUser);
		
		try {
			categoryService.delete(1L);
			fail("Check identity failed !");
		} catch (Exception e) {}
		
		try {
			categoryService.delete(3L);
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		SecurityContextHolder.clearContext();
	}

	
	@Test
	public void testFindbyDescription() {
		SecurityContextHolder.setContext(scUser);
		
		try {
			List<CategoryDTO> categories = categoryService.getEntitiesByDescription("Food");
			assertNotNull(categories);
			assertEquals(3, categories.size()); // Food, Food : Bar, Food : Baker
			assertEquals((Long)1L, (Long)categories.get(0).getEntityId());
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		try {
			List<CategoryDTO> categories = categoryService.getEntitiesByDescription("Ba");
			assertNotNull(categories);
			assertEquals(2, categories.size());
			assertEquals((Long)3L, (Long)categories.get(0).getEntityId());
		} catch (Exception e) {
			fail(e.getClass()+": "+e.getMessage());
		}
		
		try {
			List<CategoryDTO> categories = categoryService.getEntitiesByDescription("foo");
			assertNotNull(categories);
			assertEquals(3, categories.size());
			assertEquals((Long)1L, (Long)categories.get(0).getEntityId());
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
		LOGGER.warn("------------TestCategoryService: end -------");
	}
	
}
