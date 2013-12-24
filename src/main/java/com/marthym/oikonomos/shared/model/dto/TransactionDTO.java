package com.marthym.oikonomos.shared.model.dto;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.google.gwt.view.client.ProvidesKey;
import com.marthym.oikonomos.shared.model.Account;
import com.marthym.oikonomos.shared.model.BudgetaryLine;
import com.marthym.oikonomos.shared.model.dto.CategoryDTO;
import com.marthym.oikonomos.shared.model.PaiementMeans;
import com.marthym.oikonomos.shared.model.Payee;
import com.marthym.oikonomos.shared.model.Reconciliation;
import com.marthym.oikonomos.shared.model.Transaction;

public class TransactionDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	@NotNull
	private Long id;
	
	@NotNull
	private Account account;
	private PaiementMeans paiementMean;
	
	@NotNull
	private Date transactionDate;
	
	@NotNull
	private Payee payee;
	private Long credit;
	private Long debit;
	
	@Size(min=3, max=3)
	private String currency;
	private CategoryDTO category;
	
	@Size(max=255)
	private String accountingDocument;
	private BudgetaryLine budgetLine;
	private String transactionComment;	
	private Reconciliation reconciliation;
	
	public static final ProvidesKey<TransactionDTO> KEY_PROVIDER = new ProvidesKey<TransactionDTO>() {
		@Override
		public Object getKey(TransactionDTO item) {
			return item.getId();
		}
	};
	
	@Deprecated
	public TransactionDTO(){}
	
	public TransactionDTO(Account account) {
		this.id = -1L;
		this.account = account;
		this.currency = account.getAccountCurrency();
	}
	
	public static final TransactionDTO create(Transaction dao, String locale) {
		TransactionDTO dto = new TransactionDTO(dao.getAccount());
		dto.id = dao.getId();
		dto.paiementMean = dao.getPaiementMean();
		dto.transactionDate = dao.getTransactionDate();
		dto.payee = dao.getPayee();
		dto.credit = dao.getCredit();
		dto.debit = dao.getDebit();
		dto.currency = dao.getCurrency();
		dto.category = CategoryDTO.create(dao.getCategory(), locale, false);
		dto.accountingDocument = dao.getAccountingDocument();
		dto.budgetLine = dao.getBudgetLine();
		dto.transactionComment = dao.getTransactionComment();
		dto.reconciliation = dao.getReconciliation();
		
		return dto;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public PaiementMeans getPaiementMean() {
		return paiementMean;
	}

	public void setPaiementMean(PaiementMeans paiementMean) {
		this.paiementMean = paiementMean;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
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
		if (credit != null) this.debit = null;
		this.credit = credit;
	}

	public Long getDebit() {
		return debit;
	}

	public void setDebit(Long debit) {
		if (debit != null) this.credit = null;
		this.debit = debit;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public CategoryDTO getCategory() {
		return category;
	}

	public void setCategory(CategoryDTO category) {
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

	public void setTransactionComment(String transactionComment) {
		this.transactionComment = transactionComment;
	}

	public Reconciliation getReconciliation() {
		return reconciliation;
	}

	public void setReconciliation(Reconciliation reconciliation) {
		this.reconciliation = reconciliation;
	}

	public long getId() {
		return id;
	}
}
