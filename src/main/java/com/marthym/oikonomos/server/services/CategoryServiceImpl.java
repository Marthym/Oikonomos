package com.marthym.oikonomos.server.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.marthym.oikonomos.server.repositories.CategoryRepository;
import com.marthym.oikonomos.server.utils.SessionHelper;
import com.marthym.oikonomos.shared.exceptions.OikonomosException;
import com.marthym.oikonomos.shared.exceptions.OikonomosUnauthorizedException;
import com.marthym.oikonomos.shared.model.Category;
import com.marthym.oikonomos.shared.model.User;
import com.marthym.oikonomos.shared.model.dto.CategoryDTO;
import com.marthym.oikonomos.shared.services.CategoryService;

@Repository
@Service("categoryService")
public class CategoryServiceImpl extends RemoteServiceServlet implements CategoryService {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImpl.class);

	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	@Secured("ROLE_USER")
	public long getCount() throws OikonomosException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) throw new OikonomosUnauthorizedException("error.message.user.unauthorized", "No authentification found !");
		User authentifiedUser = (User)authentication.getPrincipal();
		
		return categoryRepository.countByOwnerIsNullOrOwner(authentifiedUser.getUserEmail());
	}
	
	@Override
	@Secured("ROLE_USER")
	public long getCountRootEntities() throws OikonomosException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) throw new OikonomosUnauthorizedException("error.message.user.unauthorized", "No authentification found !");
		User authentifiedUser = (User)authentication.getPrincipal();
		
		return categoryRepository.countRootCategoriesByOwner(authentifiedUser.getUserEmail());
	}

	@Override
	@Secured("ROLE_USER")
	@Transactional
	public List<CategoryDTO> getRootEntities() throws OikonomosException {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication == null) throw new OikonomosUnauthorizedException("error.message.user.unauthorized", "No authentification found !");
			User authentifiedUser = (User)authentication.getPrincipal();
			String locale = SessionHelper.getSessionLocale();
			
			List<CategoryDTO> allCategoryDto = new LinkedList<CategoryDTO>();
			for (Category category : categoryRepository.findRootCategoriesByOwner(authentifiedUser.getUserEmail())) {
				allCategoryDto.add(CategoryDTO.create(category, locale, false));
			}
			
			return allCategoryDto;
		} catch (Exception e) {
			LOGGER.error(e.getClass()+": "+e.getLocalizedMessage());
			if (LOGGER.isDebugEnabled()) LOGGER.debug("STACKTRACE", e);
			throw new OikonomosException("error.message.unexpected", e.getLocalizedMessage());
		}
	}

	@Override
	@Transactional
	@Secured("ROLE_USER")
	public CategoryDTO getEntityWithChild(long entityId) throws OikonomosException {
		String locale = SessionHelper.getSessionLocale();
		return getEntity(entityId, locale, true);
	}



	@Override
	@Transactional
	@Secured("ROLE_USER")
	public CategoryDTO getEntityWithoutChild(long entityId) throws OikonomosException {
		String locale = SessionHelper.getSessionLocale();
		return getEntity(entityId, locale, false);
	}
	
	private CategoryDTO getEntity(long entityId, String locale, boolean withChilds) throws OikonomosException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) throw new OikonomosUnauthorizedException("error.message.user.unauthorized", "No authentification found !");
		User authentifiedUser = (User)authentication.getPrincipal();
		
		Category dao = categoryRepository.findOne(entityId);
		
		if (dao.getOwner() != null && !dao.getOwner().equals(authentifiedUser.getUserEmail())) {
			throw new OikonomosException(
					"error.message.entity.notfound", 
					"Category "+dao.getId()+" must be owned by "+authentifiedUser.getUserEmail());
		}
		CategoryDTO dto = CategoryDTO.create(dao, locale, withChilds);
		
		return dto;		
	}

	@Override
	@Transactional
	@Secured("ROLE_USER")
	public List<CategoryDTO> getEntitiesByDescription(String descriptionQuery) throws OikonomosException {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication == null) throw new OikonomosUnauthorizedException("error.message.user.unauthorized", "No authentification found !");
			User authentifiedUser = (User)authentication.getPrincipal();
			String locale = SessionHelper.getSessionLocale();
			
			Set<CategoryDTO> allCategoryDto = new HashSet<CategoryDTO>();
			List<Category> allCategoryDao = categoryRepository.findByDescription(descriptionQuery, authentifiedUser.getUserEmail(), locale);
			for (Category category : allCategoryDao) {
				allCategoryDto.add(CategoryDTO.create(category, locale, false));
				for (Category child : category.getChilds()) {
					allCategoryDto.add(CategoryDTO.create(child, locale, false));
				}
			}
			List<CategoryDTO> result = new ArrayList<CategoryDTO>(allCategoryDto);
			Collections.sort(result);
			return result;
			
		} catch (Exception e) {
			LOGGER.error(e.getClass()+": "+e.getLocalizedMessage());
			if (LOGGER.isDebugEnabled()) LOGGER.debug("STACKTRACE", e);
			throw new OikonomosException("error.message.unexpected", e.getLocalizedMessage());
		}
	}

	@Override
	@Transactional
	@Secured("ROLE_USER")
	public CategoryDTO addOrUpdateEntity(CategoryDTO dto) throws OikonomosException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) throw new OikonomosUnauthorizedException("error.message.user.unauthorized", "No authentification found !");
		User authentifiedUser = (User)authentication.getPrincipal();
		String locale = SessionHelper.getSessionLocale();
		
		if (dto.getEntityDescription() == null || dto.getEntityDescription().isEmpty()) {
			throw new OikonomosException("error.message.category.mandatoryDescription", "Description is mandatory !");
		}
		
		// Get DAO object
		com.marthym.oikonomos.shared.model.Category dao = null;
		if (dto.getEntityId() == null || dto.getEntityId() < 0) {
			dao = new com.marthym.oikonomos.shared.model.Category();
			dao.setOwner(authentifiedUser.getUserEmail());
		} else {
			dao = categoryRepository.findOne(dto.getEntityId());
		}
		
		// Check the Owner
		if (dao.getOwner() != null && !dao.getOwner().equals(authentifiedUser.getUserEmail())) {
			throw new OikonomosException(
					"error.message.user.unauthorizedAction", 
					"Category "+dao.getId()+" must be owned by "+authentifiedUser.getUserEmail());
		}
		
		// Set the parent if necessary
		if (dto.getParentId() != null && dto.getParentId() > 0) {
			if (dto.getEntityId()!= null && dto.getParentId() == dto.getEntityId()) {
				throw new OikonomosException("error.message.category.cycling", "Category ID and Parent ID must be different !");
			}
			
			com.marthym.oikonomos.shared.model.Category parentDao = categoryRepository.findOne(dto.getParentId());
			if (parentDao == null) {
				throw new OikonomosException(
						"error.message.entity.notfound", "Parent category "+dto.getParentId()+" not found !");
			}
			dao.setParent(parentDao);
		} else {
			dao.setParent(null);
		}
		
		dao.addDescription(locale, dto.getEntityDescription());
		
		dao = categoryRepository.save(dao);
		return CategoryDTO.create(dao, locale, false);
	}


	@Override
	@Secured("ROLE_USER")
	public void delete(long categoryId) throws OikonomosException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) throw new OikonomosUnauthorizedException("error.message.user.unauthorized", "No authentification found !");
		User authentifiedUser = (User)authentication.getPrincipal();
		
		com.marthym.oikonomos.shared.model.Category dao = categoryRepository.findOne(categoryId);
		if (dao == null) return;
		
		if (!authentifiedUser.getUserEmail().equals(dao.getOwner())) {
			throw new OikonomosException("error.message.user.unauthorizedAction", 
					"User "+authentifiedUser.getUserEmail()+" try to delete category "+dao.getId());
		}
		categoryRepository.delete(categoryId);
	}

	@Override
	@Secured("ROLE_USER")
	@Transactional
	public List<CategoryDTO> getEntitiesByParent(long entityId) throws OikonomosException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) throw new OikonomosUnauthorizedException("error.message.user.unauthorized", "No authentification found !");
		User authentifiedUser = (User)authentication.getPrincipal();
		String locale = SessionHelper.getSessionLocale();
		
		List<CategoryDTO> allCategoryDto = new LinkedList<CategoryDTO>();
		for (com.marthym.oikonomos.shared.model.Category category : categoryRepository.findByParentId(authentifiedUser.getUserEmail(), entityId)) {
			allCategoryDto.add(CategoryDTO.create(category, locale, false));
		}
		return allCategoryDto;
	}

}
