package com.marthym.oikonomos.shared.services;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.marthym.oikonomos.shared.exceptions.OikonomosException;
import com.marthym.oikonomos.shared.model.dto.Category;

@RemoteServiceRelativePath("../rpc/categoryService")
public interface CategoryService extends RemoteService {
	public long getCount() throws OikonomosException;
	public long getCountRootEntities() throws OikonomosException;
	public List<Category> getRootEntities(String locale) throws OikonomosException;
	public List<Category> getEntitiesByParent(long entityId, String locale) throws OikonomosException;
	public Category getEntityWithChild(long entityId, String locale) throws OikonomosException;
	public Category getEntityWithoutChild(long entityId, String locale) throws OikonomosException;
	public Category addOrUpdateEntity(Category category, String locale) throws OikonomosException;
	public void delete(long categoryId) throws OikonomosException;
}
