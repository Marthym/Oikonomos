package com.marthym.oikonomos.shared.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.marthym.oikonomos.shared.exceptions.OikonomosRuntimeException;

@Entity
@Table(name = "BUDGETARY_LINE")
public class BudgetaryLine implements Serializable {
	private static final long serialVersionUID = -5726934872431175527L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

	@Column(length = 255)
	private String owner;

	@ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.PERSIST)
	private BudgetaryLine parent;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="parent", cascade=CascadeType.REMOVE)
	private Set<BudgetaryLine> childs;
		
	private String description;
	
	public BudgetaryLine() {
		childs = new HashSet<BudgetaryLine>();
	}
	public Long getId() { return id; }
	public String getOwner() { return owner; }
	public BudgetaryLine getParent() { return parent; }
	public Set<BudgetaryLine> getChilds() { return childs; }
	public String getDescription(String locale) {
		return description;
	}
	
	public void setParent(BudgetaryLine parent) {
		if (!childs.isEmpty()) {
			throw new OikonomosRuntimeException("error.message.category.cycling", "Cycling category detected '"+((parent!=null)?parent.getId():"null")+"' !");
		}
		
		if (this.parent != null) this.parent.childs.remove(this);
		this.parent = parent;
		if (this.parent != null) this.parent.childs.add(this);
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
}
