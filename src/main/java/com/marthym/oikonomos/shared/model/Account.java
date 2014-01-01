package com.marthym.oikonomos.shared.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.marthym.oikonomos.shared.view.data.EntityType;

@Entity
@Table(name = "ACCOUNT")
public class Account extends LeftMenuEntity implements java.io.Serializable {
	private static final long serialVersionUID = -390708280442917715L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@NotNull @Column(name = "owner", nullable = false, length = 255)
	private String accountOwner;
	
	@NotNull @Column(name = "name", nullable = false, length = 255)
	@Size(min = 3, max = 255, message="{validator.message.account.name.size}")
	private String accountName;
	
	@Column(name = "type", nullable = false, length = 20)
	@Enumerated(EnumType.STRING)
	private AccountType accountType;
	
	@Column(name = "currency", nullable = false, length = 3)
	private String accountCurrency;
	
	@Column(name = "closed", nullable = false, 
			columnDefinition="boolean default false")
	private boolean isClosed;
	
	@Column(name = "bank_name", length = 80)
	private String bankName;
	
	@Column(name = "bank_code", length = 5)
	private int bankCode = -1;
	
	@Column(name = "bank_desk", length = 5)
	private int bankDesk = -1;
	
	@Column(name = "account_number", length = 11)
	private long accountNumber = -1;
	
	@Column(name = "account_key", length = 3)
	private int accountKey = -1;
	
	@Column(name = "initial_amount", nullable = false)
	@DecimalMin(value="0", message="{validator.message.account.initialAmount.min}")
	private double initialAmount = -1;
	
	@Column(name = "minimal_amount")
	private double minimalAmount = -1;
	@Column(name = "maximal_amount")
	private double maximalAmount = -1;
	@Column(name = "current_amount", nullable = false)
	private double currentAmount = 0;
	@Column(name = "pointed_amount", nullable = false)
	private double pointedAmount = 0;
	
	@Deprecated
	public Account(){}
	public Account(String owner, double initialAmount) {
		this.accountOwner = owner;
		this.initialAmount = initialAmount;
		this.currentAmount = initialAmount;
	}
	
	public String getAccountOwner() {
		return accountOwner;
	}
	public void setAccountOwner(String accountOwner) {
		this.accountOwner = accountOwner;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public AccountType getAccountType() {
		return accountType;
	}
	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}
	public String getAccountCurrency() {
		return accountCurrency;
	}
	public void setAccountCurrency(String accountCurrency) {
		this.accountCurrency = accountCurrency;
	}
	public boolean isClosed() {
		return isClosed;
	}
	public void setClosed(boolean isClosed) {
		this.isClosed = isClosed;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public int getBankCode() {
		return bankCode;
	}
	public void setBankCode(int bankCode) {
		this.bankCode = bankCode;
	}
	public int getBankDesk() {
		return bankDesk;
	}
	public void setBankDesk(int bankDesk) {
		this.bankDesk = bankDesk;
	}
	public long getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(long accountNumber) {
		this.accountNumber = accountNumber;
	}
	public int getAccountKey() {
		return accountKey;
	}
	public void setAccountKey(int accountKey) {
		this.accountKey = accountKey;
	}
	public double getMinimalAmount() {
		return minimalAmount;
	}
	public void setMinimalAmount(double minimalAmount) {
		this.minimalAmount = minimalAmount;
	}
	public double getMaximalAmount() {
		return maximalAmount;
	}
	public void setMaximalAmount(double maximalAmount) {
		this.maximalAmount = maximalAmount;
	}
	public double getCurrentAmount() {
		return currentAmount;
	}
	public void setCurrentAmount(double currentAmount) {
		this.currentAmount = currentAmount;
	}
	public double getPointedAmount() {
		return pointedAmount;
	}
	public void setPointedAmount(double pointedAmount) {
		this.pointedAmount = pointedAmount;
	}
	public Long getId() {
		return id;
	}
	public double getInitialAmount() {
		return initialAmount;
	}

	@Override
	public Long getEntityId() {
		return getId();
	}
	@Override
	public String getEntityOwner() {
		return getAccountOwner();
	}
	@Override
	public String getEntityDescription() {
		return getAccountName();
	}
	@Override
	public EntityType getEntityType() {
		return EntityType.ACCOUNT;
	}
	
	
}
