package com.marthym.oikonomos.shared.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "TRANSACTION")
public class Transaction implements java.io.Serializable {
	private static final long serialVersionUID = 9079967378219903068L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@NotNull @ManyToOne(cascade=CascadeType.REFRESH, optional=false)
	private Account account;
		
	@Column(nullable = false, length = 20)
	@Enumerated(EnumType.STRING)
	private PaiementMeans paiementMean;
	
	@NotNull @Column(nullable = false)
	private Date date;
	
	@NotNull @ManyToOne(cascade=CascadeType.PERSIST, optional=false)
	private Payee payee;
	
	private Long credit;
	private Long debit;
	
	@Column(length = 3)
	private String currency;
	
	@ManyToOne(cascade=CascadeType.PERSIST)
	private Category category;
	@Transient private Long categoryId;
	
	@Column(length=255)
	private String accountingDocument;
	
	@ManyToOne(cascade=CascadeType.PERSIST)
	private BudgetaryLine budgetLine;
	
	private String comment;
	
	@ManyToOne(cascade=CascadeType.PERSIST)
	private Reconciliation reconciliation;
	
	@Deprecated
	public Transaction(){}
	public Transaction(Account account) {
		this.account = account;
	}
	public final Account getAccount() {return account;}
	public PaiementMeans getPaiementMean() {
		return paiementMean;
	}
	public void setPaiementMean(PaiementMeans paiementMean) {
		this.paiementMean = paiementMean;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Payee getPayee() {
		return payee;
	}
	public void setPayee(Payee payee) {
		this.payee = payee;
	}
	public Long getCredit() {
		return credit;
	}
	public void setCredit(Long credit) {
		this.debit = 0L;
		this.credit = credit;
	}
	public Long getDebit() {
		return debit;
	}
	public void setDebit(Long debit) {
		this.credit = 0L;
		this.debit = debit;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public void setCategory(Long categoryId) {
		this.categoryId = categoryId;
	}
	public String getAccountingDocument() {
		return accountingDocument;
	}
	public void setAccountingDocument(String accountingDocument) {
		this.accountingDocument = accountingDocument;
	}
	public BudgetaryLine getBudgetLine() {
		return budgetLine;
	}
	public void setBudgetLine(BudgetaryLine budgetLine) {
		this.budgetLine = budgetLine;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Long getId() {
		return id;
	}
	public String getOwner() {
		return account.getAccountOwner();
	}
	public Reconciliation getReconciliation() {
		return reconciliation;
	}
	public void setReconciliation(Reconciliation reconciliation) {
		this.reconciliation = reconciliation;
	}
}
