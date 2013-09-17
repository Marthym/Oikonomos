package com.marthym.oikonomos.shared.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.marthym.oikonomos.shared.exceptions.OikonomosRuntimeException;

@Entity
@Table(name = "CATEGORY")
public class Category implements Serializable {
	private static final long serialVersionUID = 1828354006204630438L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

	@Column(length = 255)
	private String owner;

	@ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.PERSIST)
	private Category parent;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="parent", cascade=CascadeType.ALL)
	private Set<Category> childs;
		
	@ElementCollection(fetch=FetchType.EAGER)
	@CollectionTable(name="CATEGORY_TRANSLATION",joinColumns={@JoinColumn(name = "id")})
	@MapKeyColumn(name="locale", insertable = false, updatable = false)
	Map<String, CategoryTranslation> translations;
	
	public Category() {
		childs = new HashSet<Category>();
		translations = new HashMap<String, CategoryTranslation>();
	}
	public Long getId() { return id; }
	public String getOwner() { return owner; }
	public Category getParent() { return parent; }
	public Set<Category> getChilds() { return childs; }
	public String getDescription(String locale) {
		CategoryTranslation translation = translations.get(locale);
		if (translation != null) return translation.getDescription();
		else return "@category"+id;
	}
	
	public void setParent(Category parent) {
		if (!childs.isEmpty()) {
			throw new OikonomosRuntimeException("error.message.category.cycling", "Cycling category detected '"+((parent!=null)?parent.getId():"null")+"' !");
		}
		
		if (this.parent != null) this.parent.childs.remove(this);
		this.parent = parent;
		if (this.parent != null) this.parent.childs.add(this);
	}
	
	public void addDescription(String locale, String description) {
		this.translations.put(locale, new CategoryTranslation(description));
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
}
