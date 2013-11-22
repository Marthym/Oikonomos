package com.marthym.oikonomos.shared.model.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.marthym.oikonomos.shared.model.LeftMenuEntity;
import com.marthym.oikonomos.shared.view.data.EntityType;

public class CategoryDTO extends LeftMenuEntity implements Serializable, Comparable<CategoryDTO> {
	private static final long serialVersionUID = 1L;

	@NotNull
	private Long id;
	private String owner;
	private Long parentId;
	private String parentDescription;
	private Set<CategoryDTO> childs;
	@Size(max=255)
	private String description;
	
	public CategoryDTO() {
		childs = new HashSet<CategoryDTO>();
		parentId = -1L;
		parentDescription = "";
	}
	
	public static CategoryDTO create(com.marthym.oikonomos.shared.model.Category dao, String locale, boolean withChilds) {
		CategoryDTO dto = new CategoryDTO();
		dto.id = dao.getId();
		dto.owner = dao.getOwner();
		dto.parentId = -1L;
		if (dao.getParent() != null) {
			dto.parentId = dao.getParent().getId();
			dto.parentDescription = dao.getParent().getDescription(locale);
		}
		
		dto.description = dao.getDescription(locale);
		
		if (withChilds) {
			dto.childs = new HashSet<CategoryDTO>();
			for (com.marthym.oikonomos.shared.model.Category childDao : dao.getChilds()) {
				dto.childs.add(create(childDao, locale, true));
			}
		}
		return dto;

	}
	
	public final Long getParentId() { return parentId; }
	public final String getParentDescription() { return parentDescription; }
	public void setParentId(Long parentId) {this.parentId = parentId;}
	
	public final Set<CategoryDTO> getChilds() { return childs; } 
	
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
	public int compareTo(CategoryDTO o) {
		return this.getAbsoluteDescription().compareToIgnoreCase(o.getAbsoluteDescription());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (!(obj instanceof CategoryDTO))
            return false;

        CategoryDTO cat = (CategoryDTO) obj;
        
        if (this.id != cat.id) {if (this.id == null || !this.id.equals(cat.id)) return false;}
        if (this.owner != cat.owner) {if (this.owner == null || !this.owner.equals(cat.owner)) return false;}
        if (this.parentId != cat.parentId) {if (this.parentId == null || !this.parentId.equals(cat.parentId)) return false;}
        if (this.description != cat.description) {if (this.description == null || !this.description.equals(cat.description)) return false;}
        
        return true;        		
	}

	@Override
	public int hashCode() {
	    int result = 7;
	    final int multiplier = 17;
	 
	    result = multiplier*result + (id==null ? 0 : id.hashCode());
	    result = multiplier*result + (owner==null ? 0 : owner.hashCode());
	    result = multiplier*result + (parentId==null ? 0 : parentId.hashCode());
	    result = multiplier*result + (description==null ? 0 : description.hashCode());
	 
	    return result;
	}

}
