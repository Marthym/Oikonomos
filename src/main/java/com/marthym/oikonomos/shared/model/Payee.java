package com.marthym.oikonomos.shared.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.marthym.oikonomos.shared.view.data.EntityType;

@Entity
@Table(name = "PAYEE")
public class Payee extends LeftMenuEntity implements java.io.Serializable {
	private static final long serialVersionUID = 319810501859117827L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@NotNull @Column(nullable = false, length = 255)
	private String name;
	
	
	public Payee(){}
	public Payee(String name) {
		this.name = name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public final String getName() {
		return name;
	}
	
	@Override
	public Long getEntityId() {
		return id;
	}
	@Override
	public String getEntityOwner() {
		return "";
	}
	@Override
	public String getEntityDescription() {
		return name;
	}
	@Override
	public EntityType getEntityType() {
		return EntityType.PAYEE;
	}
	
	
}
