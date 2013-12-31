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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "TRANSACTION")
public class Transaction implements java.io.Serializable {
	private static final long serialVersionUID = -2688586694694115640L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@NotNull @ManyToOne(cascade=CascadeType.REFRESH, optional=false)
	private Account account;
		
	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private PaiementMeans paiementMean;
	
	@Temporal(TemporalType.DATE)
	@NotNull @Column(nullable = false)
	private Date transactionDate;
	
	@NotNull @ManyToOne(cascade=CascadeType.PERSIST, optional=false)
	private Payee payee;
	
	private Double credit;
	private Double debit;
	
	@Column(length = 3)
	private String currency;
	
	@ManyToOne(cascade=CascadeType.PERSIST)
	private Category category;
	
	@Column(length=255)
	private String accountingDocument;
	
	@ManyToOne(cascade=CascadeType.PERSIST)
	private BudgetaryLine budgetLine;
	
	private String transactionComment;
	
	@ManyToOne(cascade=CascadeType.PERSIST)
	private Reconciliation reconciliation;
	
	@Deprecated
	public Transaction(){}
	public Transaction(Account account) {
		this.account = account;
		this.currency = account.getAccountCurrency();
	}
	public final Account getAccount() {return account;}
	public PaiementMeans getPaiementMean() {
		return paiementMean;
	}
	public void setPaiementMean(PaiementMeans paiementMean) {
		this.paiementMean = paiementMean;
	}
	public Date getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(Date date) {
		this.transactionDate = date;
	}
	public Payee getPayee() {
		return payee;
	}
	public void setPayee(Payee payee) {
		this.payee = payee;
	}
	public Double getCredit() {
		return credit;
	}
	public void setCredit(Double credit) {
		if (credit != null) this.debit = null;
		this.credit = credit;
	}
	public Double getDebit() {
		return debit;
	}
	public void setDebit(Double debit) {
		if (debit != null) this.credit = null;
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
	public String getTransactionComment() {
		return transactionComment;
	}
	public void setTransactionComment(String comment) {
		this.transactionComment = comment;
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
