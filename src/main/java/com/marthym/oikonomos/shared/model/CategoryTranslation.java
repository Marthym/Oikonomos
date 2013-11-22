package com.marthym.oikonomos.shared.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CategoryTranslation implements Serializable {
	private static final long serialVersionUID = 2262275043224244064L;
	
	@Column(length=255)
	private String description;
	
	/**
	 * @deprecated for Serialization only
	 */
	@Deprecated
	public CategoryTranslation() {}
	
	public CategoryTranslation(String description) {
		this.description = description;
	}
	
	public String getDescription() { return description; }
}
