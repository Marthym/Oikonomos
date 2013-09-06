package com.marthym.oikonomos.shared.model.dto;

import java.io.Serializable;
import java.util.Set;

import com.google.gwt.dev.util.collect.HashSet;
import com.marthym.oikonomos.shared.model.LeftMenuEntity;
import com.marthym.oikonomos.shared.view.data.EntityType;

public class Category extends LeftMenuEntity implements Serializable {
	private static final long serialVersionUID = -8492244149513545637L;

	private Long id;
	private String owner;
	private Long parentId;
	private Set<Category> childs;
	private String description;
	
	public Category() {}
	
	public static Category create(com.marthym.oikonomos.shared.model.Category dao, String locale, boolean withChilds) {
		Category dto = new Category();
		dto.id = dao.getId();
		dto.owner = dao.getOwner();
		dto.parentId = -1L;
		if (dao.getParent() != null)
			dto.parentId = dao.getParent().getId();
		
		dto.description = dao.getDescription(locale);
		
		if (withChilds) {
			dto.childs = new HashSet<Category>();
			for (com.marthym.oikonomos.shared.model.Category childDao : dao.getChilds()) {
				dto.childs.add(create(childDao, locale, true));
			}
		}
		return dto;

	}
	
	public final Long getParentId() { return parentId; }
	
	public final Set<Category> getChilds() { return childs; } 
	
	@Override
	public long getEntityId() {
		return id;
	}

	@Override
	public String getEntityOwner() {
		return owner;
	}

	@Override
	public String getEntityDescription() {
		return description;
	}

	@Override
	public EntityType getEntityType() {
		return EntityType.CATEGORY;
	}
	
	
}
