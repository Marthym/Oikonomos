package com.marthym.oikonomos.shared.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "RECONCILIATION")
public class Reconciliation implements java.io.Serializable {
	private static final long serialVersionUID = 5776634869326659578L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@NotNull @Column(nullable = false, length = 255)
	private String owner;

	@NotNull @Column(nullable = false)
	private String label;
	
	private Date beginDate;
	private Date endDate;
	
	private Long beginAmount;
	private Long endAmount;
	private Long pointedAmount;
	
	private boolean validate;
	
	
	/**
	 * @deprecated Only for serialization
	 */
	@Deprecated
	public Reconciliation(){}
	
	public Reconciliation(String owner, String label) {
		this.owner = owner;
		this.label = label;
	}

	public void setName(String label) {
		this.label = label;
	}
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Long getBeginAmount() {
		return beginAmount;
	}
	public void setBeginAmount(Long beginAmount) {
		this.beginAmount = beginAmount;
	}
	public Long getEndAmount() {
		return endAmount;
	}
	public void setEndAmount(Long endAmount) {
		this.endAmount = endAmount;
	}
	public Long getPointedAmount() {
		return pointedAmount;
	}
	public void setPointedAmount(Long pointedAmount) {
		this.pointedAmount = pointedAmount;
	}
	public boolean isValidate() {
		return validate;
	}
	public void setValidate(boolean validate) {
		this.validate = validate;
	}
	public Long getId() {
		return id;
	}
	public String getOwner() {
		return owner;
	}
	public String getLabel() {
		return label;
	}
	
	
}
