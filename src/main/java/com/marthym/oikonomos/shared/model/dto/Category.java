package com.marthym.oikonomos.shared.model.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.marthym.oikonomos.shared.model.LeftMenuEntity;
import com.marthym.oikonomos.shared.view.data.EntityType;

public class Category extends LeftMenuEntity implements Serializable {
	private static final long serialVersionUID = -8492244149513545637L;

	private Long id;
	private String owner;
	private Long parentId;
	private Set<Category> childs;
	private String description;
	
	public Category() {
		childs = new HashSet<Category>();
	}
	
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
	public void addChild(Category child) {
		childs.add(child);
	}
	
	@Override
	public Long getEntityId() {
		return id;
	}

	public void setEntityOwner(String owner) {
		this.owner = owner;
	}
	
	@Override
	public String getEntityOwner() {
		return owner;
	}

	public void setEntityDescription(String description) {
		this.description = description;
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
