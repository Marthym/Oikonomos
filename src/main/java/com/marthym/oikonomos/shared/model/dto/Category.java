package com.marthym.oikonomos.shared.model.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.marthym.oikonomos.shared.model.LeftMenuEntity;
import com.marthym.oikonomos.shared.view.data.EntityType;

//TODO: Implement equals and hashCode for Set unicity
public class Category extends LeftMenuEntity implements Serializable, Comparable<Category> {
	private static final long serialVersionUID = -8492244149513545637L;

	private Long id;
	private String owner;
	private Long parentId;
	private String parentDescription;
	private Set<Category> childs;
	private String description;
	
	public Category() {
		childs = new HashSet<Category>();
		parentId = -1L;
		parentDescription = "";
	}
	
	public static Category create(com.marthym.oikonomos.shared.model.Category dao, String locale, boolean withChilds) {
		Category dto = new Category();
		dto.id = dao.getId();
		dto.owner = dao.getOwner();
		dto.parentId = -1L;
		if (dao.getParent() != null) {
			dto.parentId = dao.getParent().getId();
			dto.parentDescription = dao.getParent().getDescription(locale);
		}
		
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
	public final String getParentDescription() { return parentDescription; }
	public void setParentId(Long parentId) {this.parentId = parentId;}
	
	public final Set<Category> getChilds() { return childs; } 
	
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
	
	public final String getAbsoluteDescription() {
		StringBuffer descr = new StringBuffer();
		if (parentDescription != null && !parentDescription.isEmpty()) {
			descr.append(parentDescription).append(" : ");
		}
		descr.append(description);
		return descr.toString();
	}

	@Override
	public int compareTo(Category o) {
		return this.getAbsoluteDescription().compareTo(o.getAbsoluteDescription());
	}
}
