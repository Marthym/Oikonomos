package com.marthym.oikonomos.server.services;

import java.util.LinkedList;
import java.util.List;

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
import com.marthym.oikonomos.main.client.services.CategoryService;
import com.marthym.oikonomos.server.repositories.CategoryRepository;
import com.marthym.oikonomos.shared.exceptions.OikonomosException;
import com.marthym.oikonomos.shared.exceptions.OikonomosUnauthorizedException;
import com.marthym.oikonomos.shared.model.User;
import com.marthym.oikonomos.shared.model.dto.Category;

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
	@Transactional
	public List<Category> getRootEntities(String locale) throws OikonomosException {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication == null) throw new OikonomosUnauthorizedException("error.message.user.unauthorized", "No authentification found !");
			User authentifiedUser = (User)authentication.getPrincipal();
			
			List<Category> allCategoryDto = new LinkedList<Category>();
			for (com.marthym.oikonomos.shared.model.Category category : categoryRepository.findRootCategoriesByOwner(authentifiedUser.getUserEmail())) {
				allCategoryDto.add(Category.create(category, locale, false));
			}
			
			return allCategoryDto;
		} catch (Exception e) {
			LOGGER.error(e.getClass()+": "+e.getLocalizedMessage());
			if (LOGGER.isDebugEnabled()) LOGGER.debug("STACKTRACE", e);
			throw new OikonomosException("error.message.unexpected", e.getLocalizedMessage());
		}
	}

	@Override
	@Secured("ROLE_USER")
	public List<Category> getTree(boolean sorted, String locale) throws OikonomosException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) throw new OikonomosUnauthorizedException("error.message.user.unauthorized", "No authentification found !");
		User authentifiedUser = (User)authentication.getPrincipal();
		return null;
	}

	@Override
	@Transactional
	@Secured("ROLE_USER")
	public Category getEntityWithChild(long entityId, String locale) throws OikonomosException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) throw new OikonomosUnauthorizedException("error.message.user.unauthorized", "No authentification found !");
		User authentifiedUser = (User)authentication.getPrincipal();

		com.marthym.oikonomos.shared.model.Category dao = categoryRepository.findOne(entityId);
		if (dao.getOwner() != null && !dao.getOwner().equals(authentifiedUser.getUserEmail())) {
			throw new OikonomosException(
					"error.message.entity.notfound", 
					"Category "+dao.getId()+" must be owned by "+authentifiedUser.getUserEmail());
		}
		Category dto = Category.create(dao, locale, true);
		
		return dto;
	}



	@Override
	public Category getEntityWithoutChild(long entityId, String locale) throws OikonomosException {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public Category addOrUpdateEntity(Category category, String locale) throws OikonomosException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void delete(long categoryId) throws OikonomosException {
		// TODO Auto-generated method stub
		
	}

	@Override
	@Secured("ROLE_USER")
	@Transactional
	public List<Category> getEntitiesByParent(String locale, Long entityId) throws OikonomosException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) throw new OikonomosUnauthorizedException("error.message.user.unauthorized", "No authentification found !");
		User authentifiedUser = (User)authentication.getPrincipal();
		
		List<Category> allCategoryDto = new LinkedList<Category>();
		for (com.marthym.oikonomos.shared.model.Category category : categoryRepository.findByParentId(authentifiedUser.getUserEmail(), entityId)) {
			allCategoryDto.add(Category.create(category, locale, false));
		}
		return allCategoryDto;
	}

}
