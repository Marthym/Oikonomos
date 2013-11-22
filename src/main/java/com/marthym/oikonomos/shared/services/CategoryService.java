package com.marthym.oikonomos.shared.services;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.marthym.oikonomos.shared.exceptions.OikonomosException;
import com.marthym.oikonomos.shared.model.dto.CategoryDTO;

@RemoteServiceRelativePath("../rpc/categoryService")
public interface CategoryService extends RemoteService {
	public long getCount() throws OikonomosException;
	public long getCountRootEntities() throws OikonomosException;
	public List<CategoryDTO> getRootEntities() throws OikonomosException;
	public List<CategoryDTO> getEntitiesByParent(long entityId) throws OikonomosException;
	public List<CategoryDTO> getEntitiesByDescription(String descriptionQuery) throws OikonomosException;
	public CategoryDTO getEntityWithChild(long entityId) throws OikonomosException;
	public CategoryDTO getEntityWithoutChild(long entityId) throws OikonomosException;
	public CategoryDTO addOrUpdateEntity(CategoryDTO category) throws OikonomosException;
	public void delete(long categoryId) throws OikonomosException;
}
