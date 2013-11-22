package com.marthym.oikonomos.shared.model;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.marthym.oikonomos.server.repositories.CategoryRepository;

@ContextConfiguration(locations={"classpath:/applicationContext-test.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class TestCategory {

	@Autowired
	private CategoryRepository categoryRepository;

	@BeforeClass
	public static void beforeClass() {
		System.out.println("------------ TestCategory: start -------");		
	}

	@Test
	@Transactional
	public void testCreate() {
		long count = categoryRepository.count();
		assertEquals(2, count);
		
		Category cat1 = new Category();
		cat1.addDescription(Locale.FRENCH.getDisplayLanguage(), "Catégorie Un");
		cat1.addDescription(Locale.US.getDisplayLanguage(), "Category One");
		
		Category cat2 = new Category();
		cat2.addDescription(Locale.FRENCH.getDisplayLanguage(), "Catégorie Deux");
		cat2.addDescription(Locale.US.getDisplayLanguage(), "Category Two");
		cat2.setParent(cat1);
		cat2 = categoryRepository.save(cat2);
		List<Category> cat3 = categoryRepository.findByParentId("", cat1.getId());
		assertFalse(cat3.isEmpty());
		
		count = categoryRepository.count();
		assertEquals(4, count);
		
		cat1 = categoryRepository.findOne(cat1.getId());
		cat2 = categoryRepository.findOne(cat2.getId());
		Set<Category> childs = cat1.getChilds();
		assertFalse(childs.isEmpty());
	}
	
	@Test
	@Transactional
	public void testFindByParent() {
		long count = categoryRepository.count();
		assertEquals(2, count);
		
		Category cat1 = new Category();
		cat1.addDescription(Locale.FRENCH.getDisplayLanguage(), "Catégorie Un");
		cat1.addDescription(Locale.US.getDisplayLanguage(), "Category One");
		
		Category cat2 = new Category();
		cat2.addDescription(Locale.FRENCH.getDisplayLanguage(), "Catégorie Deux");
		cat2.addDescription(Locale.US.getDisplayLanguage(), "Category Two");
		cat2.setParent(cat1);
		cat2 = categoryRepository.save(cat2);
		
		List<Category> parents = categoryRepository.findByParentId("test@localhost.com", cat1.getId());
		assertEquals(1, parents.size());
	}
	
	@Test
	@Transactional
	public void testCreateDTO() {
		Category cat1 = new Category();
		cat1.addDescription(Locale.FRENCH.getDisplayLanguage(), "Catégorie Un");
		cat1.addDescription(Locale.US.getDisplayLanguage(), "Category One");
		
		Category cat2 = new Category();
		cat2.addDescription(Locale.FRENCH.getDisplayLanguage(), "Catégorie Deux");
		cat2.addDescription(Locale.US.getDisplayLanguage(), "Category Two");
		cat2.setParent(cat1);
		cat2 = categoryRepository.save(cat2);

		{
			com.marthym.oikonomos.shared.model.dto.CategoryDTO dto = 
					com.marthym.oikonomos.shared.model.dto.CategoryDTO.create(cat1, Locale.FRENCH.getDisplayLanguage(), false);
			assertNotNull(dto);
			assertEquals("Catégorie Un", dto.getEntityDescription());
			assertTrue(dto.getChilds().isEmpty());
		}
		{
			com.marthym.oikonomos.shared.model.dto.CategoryDTO dto = 
					com.marthym.oikonomos.shared.model.dto.CategoryDTO.create(cat1, Locale.US.getDisplayLanguage(), true);
			assertNotNull(dto);
			assertEquals("Category One", dto.getEntityDescription());
			assertNotNull(dto.getChilds());
			assertEquals("Category Two", dto.getChilds().iterator().next().getEntityDescription());
		}
	}
	
	@Test
	@Transactional
	public void testCountBy() {
		Category cat1 = new Category();
		cat1.addDescription(Locale.FRENCH.getDisplayLanguage(), "Catégorie Un");
		cat1.addDescription(Locale.US.getDisplayLanguage(), "Category One");
		
		Category cat2 = new Category();
		cat2.addDescription(Locale.FRENCH.getDisplayLanguage(), "Catégorie Deux");
		cat2.addDescription(Locale.US.getDisplayLanguage(), "Category Two");
		cat2.setParent(cat1);
		cat2 = categoryRepository.save(cat2);
		
		Category cat3 = new Category();
		cat3.addDescription(Locale.FRENCH.getDisplayLanguage(), "Catégorie Deux");
		cat3.setOwner("test@localhost.com");
		categoryRepository.save(cat3);
		
		{
			Long count = categoryRepository.countByOwnerIsNullOrOwner("test@localhost.com");
			assertNotNull(count);
			assertEquals(5, (long)count);
		}
		{
			Long count = categoryRepository.countByOwnerIsNullOrOwner("test2@localhost.com");
			assertNotNull(count);
			assertEquals(4, (long)count);
		}
	}

	@AfterClass
	public static void afterClass() {
		System.out.println("------------ TestCategory: end -------");
	}

}
